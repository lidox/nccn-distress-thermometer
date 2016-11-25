package com.artursworld.nccn.model.persistence.manager;


import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;

import com.artursworld.nccn.controller.util.Bits;
import com.artursworld.nccn.model.entity.QolQuestionnaire;

import org.junit.Test;

public class QualityOfLifeManagerTest extends InstrumentationTestCase {

    private UserManager userDB;
    private QualityOfLifeManager db;
    private RenamingDelegatingContext context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getInstrumentation().getTargetContext(), "test_");
        db = new QualityOfLifeManager(context);
        userDB = new UserManager(context);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }


    @Test
    public void testConstructor(){
        QolQuestionnaire questionnaire = new QolQuestionnaire("Artur");
        String answers = questionnaire.getAnswersToQuestionsAsString();
        StringBuilder expected = new StringBuilder();
        for(int i = 1; i < 29 ; i++){
            expected.append(QolQuestionnaire.defaultByte);
        }
        expected.append(QolQuestionnaire.exceptionalByte);
        expected.append(QolQuestionnaire.exceptionalByte);
        for(int i = 1; i < 31 ; i++){
            expected.append(QolQuestionnaire.defaultByte);
        }
        assertEquals(expected.toString(), answers);
    }
}