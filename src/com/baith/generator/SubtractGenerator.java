package com.baith.generator;

import java.util.*;

/**
 * 减法生成器
 */
public class SubtractGenerator {
    private Set<Integer> subtractions = new HashSet<>(); //减数
    private Set<Integer> subtracted = new HashSet<>();    //被减数
    private Map<String, Integer> result = new HashMap<>();  //结果

    private final static int ROUND = 101;   //算数范围
    private static int size = 0;    //算式个数

    public int generateSubtractions() {
        Random random = new Random();
        int addend = random.nextInt(ROUND);
        return addend;
    }

    /**
     * 生成被加数
     * @return 被加数
     */
    public int generateSubtracted() {
        Random random = new Random();
        return random.nextInt(ROUND);
    }

    public String[] generate(int size) {
        this.size = size;
        while(result.size() != size) {
            int n1 = generateSubtractions();
            int n2 = generateSubtracted();
            if(n1 - n2 >= 0 && (!subtractions.contains(n1) || !subtracted.contains(n2)) ) {    //严格不重复，即前后顺序不同但加数和被加数相同的也算重复
                subtractions.add(n1);
                subtracted.add(n2);
                result.put(n1 + "-" + n2, n1 - n2);
            }
        }
        String[] strings = new String[this.size];
        return result.keySet().toArray(strings);
    }

    public Set<Integer> getSubtractions() {
        return subtractions;
    }

    public void setSubtractions(Set<Integer> subtractions) {
        this.subtractions = subtractions;
    }

    public Set<Integer> getSubtracted() {
        return subtracted;
    }

    public void setSubtracted(Set<Integer> subtracted) {
        this.subtracted = subtracted;
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
        SubtractGenerator.size = size;
    }
}
