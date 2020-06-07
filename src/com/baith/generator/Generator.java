package com.baith.generator;

import java.util.*;

/**
 * 综合题目生成器
 */
public class Generator {

    private Map<String, Integer> result;

    /**
     * 算数类型
     */
    public enum Type {
        ADD, SUB, MIX, NONE
    }

    public Type curType = Type.NONE;

    public String[] generate(Generator.Type type, int size) {
        curType = type;
        switch (type) {
            case ADD: {
                AddGenerator addGenerator = new AddGenerator();
                String[] res = addGenerator.generate(size);
                result = addGenerator.getResult();
                return res;
            }
            case SUB: {
                SubtractGenerator subGenerator = new SubtractGenerator();
                String[] res = subGenerator.generate(size);
                result = subGenerator.getResult();
                return res;
            }
            case MIX:{
                AddGenerator addGenerator = new AddGenerator();
                SubtractGenerator subGenerator = new SubtractGenerator();
                int half = size / 2;
                String[] adds = addGenerator.generate(half);
                String[] subs = subGenerator.generate(size - half);
                String[] res = new String[size];
                Random random =  new Random();
                Set<Integer> uniqueIndex = new HashSet<>();
                int addIndex = 0, subIndex = 0;

                while(uniqueIndex.size() != size) {
                    int index = random.nextInt(size);
                    if(!uniqueIndex.contains(index)) {
                        uniqueIndex.add(index);
                        if(addIndex < half) {
                            res[index] = adds[addIndex++];
                        }else {
                            res[index] = subs[subIndex++];
                        }
                    }
                }
                result = new HashMap<>();
                result.putAll(addGenerator.getResult());
                result.putAll(subGenerator.getResult());
                return res;
            }
            default:return null;
        }
    }

    public void setCurType(Generator.Type type) {
        this.curType = type;
    }

    public Map<String, Integer> getResult() {
        return result;
    }

    public Type getCurType() {
        return curType;
    }
}
