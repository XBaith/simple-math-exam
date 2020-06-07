package com.baith.ui;

import com.baith.generator.Generator;
import com.baith.util.SQLClient;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.util.HashMap;
import java.util.Map;

/**
 * 问题列表
 */
public class QuestionsTable {

    private GridPane gridPane;
    private HBox[][] qstHBoxes;
    private Label[][] qstLabels;
    private TextField[][] qstTexts;
    private double hBoxWidth, hBoxHeight, hBoxSpacing;

    private final Generator generator = new Generator();
    private Map<String, Integer> rightAnswers = new HashMap<>();
    private Map<String, Integer> wrongAnswers = new HashMap<>();
    private Map<String, Integer> noAnswers = new HashMap<>();

    private long startTime = 0L;    //开始答题的时间

    private Font font = new Font(20);

    public GridPane getGridPane() {
        return gridPane;
    }

    public HBox[][] getQstHBoxes() {
        return qstHBoxes;
    }

    public Label[][] getQstLabels() {
        return qstLabels;
    }

    public TextField[][] getQstTexts() {
        return qstTexts;
    }

    public QuestionsTable setHBoxesSize(double width, double height) {
        this.hBoxWidth = width;
        this.hBoxHeight = height;
        return this;
    }

    public QuestionsTable setHBoxesSpacing(double spacing) {
        this.hBoxSpacing = spacing;
        return this;
    }

    public QuestionsTable construct(GridPane gridPane, HBox[][] qstHBoxes, Label[][] qstLabels, TextField[][] qstTexts) {
        this.gridPane = gridPane;
        this.qstHBoxes = qstHBoxes;
        this.qstLabels = qstLabels;
        this.qstTexts = qstTexts;

        for(int r = 0; r < qstHBoxes.length; r++) {
            for(int c = 0; c < qstHBoxes[0].length; c++) {
                qstLabels[r][c] = new Label("Q(" + r + ", " + c + ")");
                //"answer(" + r + ", " + c + ")"
                qstTexts[r][c] = new TextField();
                qstTexts[r][c].setMaxWidth(70.0);
                qstHBoxes[r][c].getChildren().add(qstLabels[r][c]);
                qstHBoxes[r][c].getChildren().add(qstTexts[r][c]);
                qstHBoxes[r][c].setSpacing(hBoxSpacing);
                qstHBoxes[r][c].setAlignment(Pos.CENTER_RIGHT);
                qstHBoxes[r][c].setMinSize(hBoxWidth, hBoxHeight);
                qstLabels[r][c].setFont(font);
                qstTexts[r][c].setFont(font);
            }
        }
        return this;
    }

    /**
     * 生成题目
     * @param type  题目类型
     */
    public void generate(Generator.Type type) {
        if(qstHBoxes == null || qstLabels == null || qstTexts == null) {
            try {
                throw new IllegalAccessException("Question table is not build complete.");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        String[] questions = generator.generate(type, qstHBoxes.length * qstHBoxes[0].length);
        if(questions != null)
            flushQuestions(questions);
    }

    public void generate() {
        generate(generator.getCurType());
    }

    /**
     * 刷新题目
     * @param questions
     */
    public void flushQuestions(String[] questions) {
        int row = qstLabels.length;
        int column = qstLabels[0].length;
        if(questions.length != row * column) {
            throw new IllegalArgumentException("Number of questions is not correct");
        }

        for(int q = 0; q < questions.length; q++) {
            int qRow = q / column;
            int qColumn = q % column;
            qstLabels[qRow][qColumn].setText(questions[q]);
            qstTexts[qRow][qColumn].setStyle("-fx-background-color: #FFFFFF;"); //恢复默认状态
        }
        startTime = System.currentTimeMillis() / 1000;  //当前时间（秒）
    }

    /**
     * 检查答题情况
     */
    public void checkAnswer() {
        if(rightAnswers.size() != 0 || wrongAnswers.size() != 0 || noAnswers.size() != 0) {
            rightAnswers.clear();
            wrongAnswers.clear();
            noAnswers.clear();
        }
        long endTime = System.currentTimeMillis() / 1000;   //答题结束时间

        Map<String, Integer> solutions = generator.getResult();
        for(int r = 0; r < qstLabels.length; r++) {
            for(int c = 0; c < qstLabels[0].length; c++) {
                String question = qstLabels[r][c].getText();
                Integer answer = Integer.MIN_VALUE;
                try {
                    String ans = qstTexts[r][c].getText().trim();
                    if(!"".equals(ans))  //检查答案是否为空
                        answer = Integer.parseInt(ans);
                    }catch (NumberFormatException e) {
                        e.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("请检查答案格式");
                        alert.show();
                }
                Integer solution = solutions.get(question);
                if(solution == answer) {    //答案正确
                    rightAnswers.put(question, answer);
                    qstTexts[r][c].setStyle("-fx-background-color: #76EE00;");
                    SQLClient.putAnswer(question, String.valueOf(answer), 1);
                }else if(answer == Integer.MIN_VALUE){ //没写答案
                    noAnswers.put(question, answer);
                    SQLClient.putAnswer(question, String.valueOf(answer), 2);
                }else { //答案错误
                    wrongAnswers.put(question, answer);
                    qstTexts[r][c].setStyle("-fx-background-color: #FF3030;");
                    SQLClient.putAnswer(question, String.valueOf(answer), 0);
                }
            }
        }
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("答题情况");
        String context = "正确: " + rightAnswers.size() + "\n"
                + "错误: " + (wrongAnswers.size() + noAnswers.size()) + "\n";
//        if(rightAnswers.size() == solutions.size())
//            alert.setHeaderText("答的全会，蒙的全对!");
//        else if(wrongAnswers.size() + noAnswers.size() == solutions.size())
//            alert.setHeaderText("拉跨了");
//        else alert.setHeaderText("还有上升空间");
        System.out.println(context);
//        alert.setContentText(context);
//        alert.show();
        SQLClient.putRound(rightAnswers.size(), wrongAnswers.size(), noAnswers.size(),
                endTime - startTime);
    }

    public Map<String, Integer> getRightAnswers() {
        return rightAnswers;
    }

    public Map<String, Integer> getWrongAnswers() {
        return wrongAnswers;
    }

    public Map<String, Integer> getNoAnswers() {
        return noAnswers;
    }
}
