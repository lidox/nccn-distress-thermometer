package com.artursworld.nccn.model.entity;

import junit.framework.TestCase;

import org.junit.Test;

public class DistressThermometerQuestionnaireTest extends TestCase {

    @Test
    public void testGetDistressScore1(){
        DistressThermometerQuestionnaire question = new DistressThermometerQuestionnaire("test user");
        question.setBitsByQuestionNr(1, "00000010000");
        assertEquals("Check depression Score", 4, question.getDistressScore());
        assertFalse("Check if patient has depression",question.hasDistress());
    }

    @Test
    public void testGetDistressScore2(){
        DistressThermometerQuestionnaire question = new DistressThermometerQuestionnaire("test user");
        question.setBitsByQuestionNr(1, "00001000000");
        assertEquals("Check depression Score", 6, question.getDistressScore());
        assertTrue("Check if patient has depression",question.hasDistress());
    }

    @Test
    public void testGetDistressScore3(){
        DistressThermometerQuestionnaire question = new DistressThermometerQuestionnaire("test user");
        question.setBitsByQuestionNr(1, "10000000000");
        assertEquals("Check depression Score", 10, question.getDistressScore());
        assertTrue("Check if patient has depression",question.hasDistress());
    }

    @Test
    public void testGetDistressScore4(){
        DistressThermometerQuestionnaire question = new DistressThermometerQuestionnaire("test user");
        question.setBitsByQuestionNr(1, "00000000001");
        assertEquals("Check depression Score", 0, question.getDistressScore());
        assertFalse("Check if patient has depression",question.hasDistress());
    }
}
