package com.artursworld.nccn.model.entity;


import com.artursworld.nccn.controller.util.Bits;

import junit.framework.TestCase;

import org.junit.Test;

public class HADSDQuestionnaireTest extends TestCase {

    @Test
    public void testGetDepressionScore1(){
        HADSDQuestionnaire question = new HADSDQuestionnaire("test user");
        question.setAnswerByNr(7, Bits.getByteByString("1000"));
        question.setAnswerByNr(8, Bits.getByteByString("0100"));
        question.setAnswerByNr(9, Bits.getByteByString("0100"));
        question.setAnswerByNr(10, Bits.getByteByString("1000"));
        question.setAnswerByNr(11, Bits.getByteByString("1000"));
        question.setAnswerByNr(12, Bits.getByteByString("0010"));
        question.setAnswerByNr(13, Bits.getByteByString("0100"));

        assertEquals("Check depression Score", 9, question.getDepressionScore());

        assertFalse("Check if patient has depression",question.hasDepression());
    }

    @Test
    public void testGetDepressionScore2(){
        HADSDQuestionnaire question = new HADSDQuestionnaire("test user");
        question.setAnswerByNr(7, Bits.getByteByString("1000"));
        question.setAnswerByNr(8, Bits.getByteByString("1000"));
        question.setAnswerByNr(9, Bits.getByteByString("0001"));
        question.setAnswerByNr(10, Bits.getByteByString("00001"));
        question.setAnswerByNr(11, Bits.getByteByString("0001"));
        question.setAnswerByNr(12, Bits.getByteByString("1000"));
        question.setAnswerByNr(13, Bits.getByteByString("1000"));

        assertEquals("Check depression Score", 21, question.getDepressionScore());

        assertTrue("Check if patient has depression",question.hasDepression());
    }

    @Test
    public void testGetAnxietyScore1(){
        HADSDQuestionnaire question = new HADSDQuestionnaire("test user");
        question.setAnswerByNr(0, Bits.getByteByString("0001"));
        question.setAnswerByNr(1, Bits.getByteByString("0010"));
        question.setAnswerByNr(2, Bits.getByteByString("0100"));
        question.setAnswerByNr(3, Bits.getByteByString("0001"));
        question.setAnswerByNr(4, Bits.getByteByString("0001"));
        question.setAnswerByNr(5, Bits.getByteByString("0100"));
        question.setAnswerByNr(6, Bits.getByteByString("0010"));

        assertEquals("Check anxiety Score", 9, question.getAnxietyScore());

        assertFalse("Check if patient has anxiety",question.hasAnxiety());
    }

    @Test
    public void testGetAnxietyScore2(){
        HADSDQuestionnaire question = new HADSDQuestionnaire("test user");
        question.setAnswerByNr(0, Bits.getByteByString("0001"));
        question.setAnswerByNr(1, Bits.getByteByString("0001"));
        question.setAnswerByNr(2, Bits.getByteByString("0001"));
        question.setAnswerByNr(3, Bits.getByteByString("1000"));
        question.setAnswerByNr(4, Bits.getByteByString("1000"));
        question.setAnswerByNr(5, Bits.getByteByString("0001"));
        question.setAnswerByNr(6, Bits.getByteByString("0001"));

        assertEquals("Check anxiety Score", 21, question.getAnxietyScore());

        assertTrue("Check if patient has anxiety",question.hasAnxiety());
    }


}
