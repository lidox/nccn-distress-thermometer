package com.artursworld.nccn.model.entity;


import com.artursworld.nccn.controller.util.Bits;

import junit.framework.TestCase;

import org.junit.Test;

public class HADSDQuestionnaireTest extends TestCase {

    @Test
    public void testGetDepressionScore1(){
        HADSDQuestionnaire question = new HADSDQuestionnaire("test user");
        question.setAnswerByNr(7, Bits.getByteByString("1000")); // 0 points
        question.setAnswerByNr(8, Bits.getByteByString("0100")); // 1 point
        question.setAnswerByNr(9, Bits.getByteByString("0100")); // 2
        question.setAnswerByNr(10, Bits.getByteByString("1000")); // 3
        question.setAnswerByNr(11, Bits.getByteByString("1000")); // 3
        question.setAnswerByNr(12, Bits.getByteByString("0010")); // 2
        question.setAnswerByNr(13, Bits.getByteByString("0100")); // 1

        assertEquals("Check depression Score", 12, question.getDepressionScore());

        assertTrue("Check if patient has depression",question.hasDepression());
    }

    @Test
    public void testGetDepressionScore2(){
        HADSDQuestionnaire question = new HADSDQuestionnaire("test user");
        question.setAnswerByNr(7, Bits.getByteByString("1000")); // 0 points
        question.setAnswerByNr(8, Bits.getByteByString("1000")); // 0 point
        question.setAnswerByNr(9, Bits.getByteByString("0001")); // 0
        question.setAnswerByNr(10, Bits.getByteByString("00001")); // 0
        question.setAnswerByNr(11, Bits.getByteByString("0001")); // 0
        question.setAnswerByNr(12, Bits.getByteByString("1000")); // 0
        question.setAnswerByNr(13, Bits.getByteByString("1000")); // 0

        assertEquals("Check depression Score", 0, question.getDepressionScore());

        assertFalse("Check if patient has depression",question.hasDepression());
    }

    @Test
    public void testGetAnxietyScore1(){
        HADSDQuestionnaire question = new HADSDQuestionnaire("test user");
        question.setAnswerByNr(0, Bits.getByteByString("0001")); // 0 points
        question.setAnswerByNr(1, Bits.getByteByString("0010")); // 1 point
        question.setAnswerByNr(2, Bits.getByteByString("0100")); // 2
        question.setAnswerByNr(3, Bits.getByteByString("0001")); // 3
        question.setAnswerByNr(4, Bits.getByteByString("0001")); // 3
        question.setAnswerByNr(5, Bits.getByteByString("0100")); // 2
        question.setAnswerByNr(6, Bits.getByteByString("0010")); // 1

        assertEquals("Check anxiety Score", 12, question.getAnxietyScore());

        assertTrue("Check if patient has anxiety",question.hasAnxiety());
    }

    @Test
    public void testGetAnxietyScore2(){
        HADSDQuestionnaire question = new HADSDQuestionnaire("test user");
        question.setAnswerByNr(0, Bits.getByteByString("0001")); // 0 points
        question.setAnswerByNr(1, Bits.getByteByString("0001")); // 0 point
        question.setAnswerByNr(2, Bits.getByteByString("0001")); // 0
        question.setAnswerByNr(3, Bits.getByteByString("1000")); // 0
        question.setAnswerByNr(4, Bits.getByteByString("1000")); // 0
        question.setAnswerByNr(5, Bits.getByteByString("0001")); // 0
        question.setAnswerByNr(6, Bits.getByteByString("0001")); // 0

        assertEquals("Check anxiety Score", 0, question.getAnxietyScore());

        assertFalse("Check if patient has anxiety",question.hasAnxiety());
    }


}
