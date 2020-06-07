package com.baith.generator;

import java.util.*;

/**
 * 加法生成器
 */
public class AddGenerator {

    private Set<Integer> addends = new HashSet<>(); //加数
    private Set<Integer> summands = new HashSet<>();    //被加数
    private Map<String, Integer> result = new HashMap<>();  //结果

    private final static int ROUND = 101;   //算数范围：0~ROUND
    private static int size = 0;    //算式个数

    /**
     * 生成加数
     * @return 加数
     */
    public int generateAddend() {
        Random random = new Random();
        int addend = random.nextInt(ROUND);
        return addend;
    }

    /**
     * 生成被加数
     * @return 被加数
     */
    public int generateSummand() {
        Random random = new Random();
        return random.nextInt(ROUND);
    }

    /**
     * 生成加法题目
     * @param size 题目个数
     * @return 加法题目数组
     */
    public String[] generate(int size) {
        this.size = size;
        while(result.size() != size) {
            int n1 = generateAddend();
            int n2 = generateSummand();
            if(n1 + n2 < ROUND && (!addends.contains(n1) || !summands.contains(n2)) ) {    //严格不重复，即前后顺序不同但加数和被加数相同的也算重复
                addends.add(n1);
                summands.add(n2);
                result.put(n1 + "+" + n2, n1 + n2);
            }
        }
        String[] strings = new String[this.size];
        return result.keySet().toArray(strings);
    }

    public Set<Integer> getAddends() {
        return addends;
    }

    public void setAddends(Set<Integer> addends) {
        this.addends = addends;
    }

    public Set<Integer> getSummands() {
        return summands;
    }

    public void setSummands(Set<Integer> summands) {
        this.summands = summands;
    }

    public Map<String, Integer> getResult() {
        return result;
    }

    public void setResult(Map<String, Integer> result) {
        this.result = result;
    }

    public static int getROUND() {
        return ROUND;
    }

    public static int getSize() {
        return size;
    }

    public static void setSize(int size) {
        AddGenerator.size = size;
    }
}
