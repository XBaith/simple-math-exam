package com.baith.util;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 访问数据库工具类
 */
public class SQLClient {
    private static Connection conn = null;
    private final static String URL = "jdbc:sqlite:math.db";
    private static Statement statement = null;
    static {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(URL);
            statement = conn.createStatement();
            statement.setQueryTimeout(10);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Connect database successfully");
    }

    public static void createTable(){
        try {
            statement.execute("drop table if exists t_question");
            StringBuilder builder = new StringBuilder("create table t_question(");
            builder.append("id integer PRIMARY KEY autoincrement,") //id
                    .append("question varchar(50) NOT NULL,")   //问题
                    .append("answer varchar(50),")  //填写答案
                    .append("state integer NOT NULL default(2),")   //1:正确，0:错误，2:没填
                    .append("round integer,")   //答题轮数
                    .append("FOREIGN KEY(round) REFERENCES t_round(round)") //与轮数表关联
            .append(")");
            statement.executeUpdate(builder.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createRoundTable() {
        try {
            statement.execute("drop table if exists t_round");
            StringBuilder builder = new StringBuilder("create table t_round(")
                    .append("round integer PRIMARY KEY autoincrement,")    //答题轮数
//                    .append("total integer NOT NULL,")  //题目数量
                    .append("right integer NOT NULL,")   //正确数
                    .append("wrong integer NOT NULL,")   //错误数
                    .append("space integer NOT NULL,")  //没填数
                    .append("time text ") //答题所花费时间
                    .append("answer_time datetime default(datetime('now', 'localtime'))")
                    .append(")");
            statement.executeUpdate(builder.toString());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * 插入答题结果
     * @param question
     * @param answer
     * @param state
     */
    public static void putAnswer(String question, String answer, int state) {
        String sql = "insert into t_question(question, answer, state, round) values(?,?,?,?)";
        try(PreparedStatement prestmt = conn.prepareStatement(sql)) {
            prestmt.setString(1, question);
            prestmt.setString(2, answer);
            prestmt.setInt(3, state);
            prestmt.setInt(4, lastRound());
            prestmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入本轮答题信息
     * @param right
     * @param wrong
     * @param space
     * @param time
     */
    public static void putRound(int right, int wrong, int space, long time) {
        String sql = "insert into t_round(right, wrong, space, time) values(?,?,?,?)";
        try(PreparedStatement prestmt = conn.prepareStatement(sql)) {
            prestmt.setInt(1, right);
            prestmt.setInt(2, wrong);
            prestmt.setInt(3, space);
            prestmt.setString(4, String.valueOf(time));
            prestmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查找最新轮数
     * @return
     */
    public static int lastRound() {
        try {
            ResultSet rs = statement.executeQuery("select round from t_round order by round desc limit 0,1");
            if(rs == null) return 1;
            if(rs.next())
                return rs.getInt("round");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static Map<String, Integer> getAnalysis() {
        Map<String, Integer> res = new HashMap<>();
        String sql = "select sum(right) as r_sum, sum(wrong) as w_sum, sum(space) as s_sum from t_round ";
        try {
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()) {
                res.put("right", rs.getInt("r_sum"));
                res.put("wrong", rs.getInt("w_sum"));
                res.put("space", rs.getInt("s_sum"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static Map<String, String> getWrongList() {
        Map<String, String> res = new HashMap<>();
        String sql = "select question, answer from t_question where state=0 OR state=2";
        try {
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()) {
                res.put(rs.getString("question"), rs.getString("answer"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}
