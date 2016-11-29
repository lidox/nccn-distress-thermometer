package com.artursworld.nccn.model.persistence.manager;

import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;

import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;
import com.artursworld.nccn.model.entity.QolQuestionnaire;

import org.junit.Test;

public class DistressThermometerQuestionnaireManagerTest extends InstrumentationTestCase {

    private UserManager userDB;
    private DistressThermometerQuestionnaireManager db;
    private RenamingDelegatingContext context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getInstrumentation().getTargetContext(), "test_");
        db = new DistressThermometerQuestionnaireManager(context);
        userDB = new UserManager(context);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testConstructor(){
        DistressThermometerQuestionnaire questionnaire = new DistressThermometerQuestionnaire("Artur");
        StringBuilder expect = new StringBuilder();
        expect.append("0000000001"); // 10 bits first question
        expect.append("00000"); // 5 bits second question
        expect.append("00"); // 2 bits third question
        expect.append("000000"); // 6 bits fourth question
        expect.append("00"); // 2 bits fifth question
        expect.append("000000000000000000000"); // 21 bits sixt question
        expect.append("00"); // 2 bits missing to full fill 6 full bytes

        assertEquals(expect.toString(), questionnaire.getAnswersToQuestionsAsString());
        assertEquals("8 bits per byte arrays * 6 arrays",8 * 6, questionnaire.getAnswersToQuestionsAsString().length());
    }
}
