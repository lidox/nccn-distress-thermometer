package com.artursworld.nccn.model.entity;


import com.artursworld.nccn.controller.util.Bits;

import junit.framework.TestCase;

import org.junit.Test;

public class HADSDQuestionnaireTest extends TestCase {

    @Test
    public void testGetDepressionScore(){
        HADSDQuestionnaire question = new HADSDQuestionnaire("test user");

        for(int i = 7; i< 14; i++){
            String questionN = Bits.getStringByByte(question.getAnswerByNr(i));
            int dum = -1;
        }



        assertEquals("1", question.getDepressionScore());
    }
}
