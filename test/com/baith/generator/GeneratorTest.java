package com.baith.generator;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;

public class GeneratorTest {

    @Test
    public void addTest() {
        Generator generator = new Generator();
        String[] adds = generator.generate(Generator.Type.ADD, 50);
        for(String add : adds) {
            System.out.println(add);
        }
    }

    @Test
    public void sortTest() {
        SubtractGenerator subtractGenerator = new SubtractGenerator();
        String[] questions = subtractGenerator.generate(5);
        Set<Integer> n1s = subtractGenerator.getSubtractions();
        Set<Integer> n2s = subtractGenerator.getSubtracted();
        Iterator<Integer> n1Itor = n1s.iterator();
        Iterator<Integer> n2Itor = n2s.iterator();

        for(String q : questions) {
            System.out.println(q);
            System.out.println(n1Itor.next() + "-" + n2Itor.next());
            System.out.println();
        }

    }

    @Test
    public void test() {
        System.out.println(new BigDecimal(0 * 1.0 / 3 * 1.0)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue());
    }

}
