package com.baith.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class SQLClientTest {
    @Test
    public void general() {
        SQLClient.createRoundTable();
        SQLClient.createTable();
    }

    @Test
    public void lastRoundTest() {
        Assert.assertEquals(1, SQLClient.lastRound());
    }

    @Test
    public void wrongListTest() {
        for(Map.Entry<String, String> q : SQLClient.getWrongList().entrySet())
            System.out.println(q.getKey() + " " + q.getValue());
    }
}
