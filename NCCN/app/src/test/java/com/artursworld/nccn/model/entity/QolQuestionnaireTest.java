package com.artursworld.nccn.model.entity;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class QolQuestionnaireTest {

    private QolQuestionnaire q = null;

    @Before
    public void setUp(){
        q =  new QolQuestionnaire("Local Test User");
    }

    @Test
    public void testGetRawScore1() {
        double result = q.getRawScore(1, 7);
        assertEquals(4, result, 0);

        result = q.getRawScore(1, 2, 3, 4, 5, 6, 7);
        assertEquals(4, result, 0);
    }

    @Test
    public void testGetGlobalHealthScore1() {
        // update question nr. 30 because it has 8 bits instead of 4 like the others

        // 7 points = 10000000
        q.setBitsByQuestionNr(29, "10000000");

        // 7 points = 10000000
        q.setBitsByQuestionNr(30, "10000000");
        double result = q.getGlobalHealthScore();
        assertEquals(100.0, result, 0);


        // 1 Point = 00000010
        q.setBitsByQuestionNr(29, "00000010");

        // 1 Point = 00000010
        q.setBitsByQuestionNr(30, "00000010");

        result = q.getGlobalHealthScore();

        assertEquals(0.0, result, 0);

    }

    @Test
    public void testGetPhysicalFunctioningScore1() {

        // 1 = 0001 and 4 = 1000
        q.setBitsByQuestionNr(1, "1000");
        q.setBitsByQuestionNr(2, "1000");
        q.setBitsByQuestionNr(3, "1000");
        q.setBitsByQuestionNr(4, "1000");
        q.setBitsByQuestionNr(5, "1000");
        double result = q.getPhysicalFunctioningScore();
        assertEquals(0.0, result, 0);

        // 1 = 0001 and 4 = 1000
        q.setBitsByQuestionNr(1, "0001");
        q.setBitsByQuestionNr(2, "0001");
        q.setBitsByQuestionNr(3, "0001");
        q.setBitsByQuestionNr(4, "0001");
        q.setBitsByQuestionNr(5, "0001");
        result = q.getPhysicalFunctioningScore();
        assertEquals(100.0, result, 0);

    }

}
