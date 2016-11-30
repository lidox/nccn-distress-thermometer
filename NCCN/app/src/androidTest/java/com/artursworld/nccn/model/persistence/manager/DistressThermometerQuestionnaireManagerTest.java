package com.artursworld.nccn.model.persistence.manager;

import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;

import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;
import com.artursworld.nccn.model.entity.QolQuestionnaire;
import com.artursworld.nccn.model.entity.User;

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
        assertEquals("8 bits per byte arrays * 6 arrays", 8 * 6, questionnaire.getAnswersToQuestionsAsString().length());
    }

    @Test
    public void testSetAndGetBitsByQuestionNr(){
        DistressThermometerQuestionnaire questionnaire = new DistressThermometerQuestionnaire("Franziska");

        String questionBits = "1111111101";
        int questionNr = 1;
        questionnaire.setBitsByQuestionNr(questionNr, questionBits);
        String questionResult = questionnaire.getBitsByQuestionNr(questionNr);
        assertEquals(questionBits, questionResult);

        questionBits = "11111";
        questionNr = 2;
        questionnaire.setBitsByQuestionNr(questionNr, questionBits);
        questionResult = questionnaire.getBitsByQuestionNr(questionNr);
        assertEquals(questionBits, questionResult);
    }

    @Test
    public void testSetAndGetBitsByQuestionNr2(){
        DistressThermometerQuestionnaire questionnaire = new DistressThermometerQuestionnaire("Patrizia");
        String questionBits = "0000000000";
        int questionNr = 1;
        questionnaire.setBitsByQuestionNr(questionNr, questionBits);
        String questionResult = questionnaire.getBitsByQuestionNr(questionNr);
        assertEquals(questionBits, questionResult);

        questionBits = "11111";
        questionNr = 2;
        questionnaire.setBitsByQuestionNr(questionNr, questionBits);
        questionResult = questionnaire.getBitsByQuestionNr(questionNr);
        assertEquals(questionBits, questionResult);
    }

    @Test
    public void testSetAndGetBitsByQuestionNr3(){
        DistressThermometerQuestionnaire questionnaire = new DistressThermometerQuestionnaire("Patrizia");
        String questionBits = "00000";
        int questionNr = 2;
        questionnaire.setBitsByQuestionNr(questionNr, questionBits);
        String questionResult = questionnaire.getBitsByQuestionNr(questionNr);
        assertEquals(questionBits, questionResult);

        questionBits = "11";
        questionNr = 3;
        questionnaire.setBitsByQuestionNr(questionNr, questionBits);
        questionResult = questionnaire.getBitsByQuestionNr(questionNr);
        assertEquals(questionBits, questionResult);

        questionBits = "010";
        questionNr = 3;
        questionnaire.setBitsByQuestionNr(questionNr, questionBits);
        questionResult = questionnaire.getBitsByQuestionNr(questionNr);
        assertEquals("Wrong input because of invalid input length, so do not change bits", "11", questionResult);
    }

    @Test
    public void testSetAndGetBitsByQuestionNr4(){
        DistressThermometerQuestionnaire questionnaire = new DistressThermometerQuestionnaire("Patrizia");
        String questionBits = "11";
        int questionNr = 3;
        questionnaire.setBitsByQuestionNr(questionNr, questionBits);
        String questionResult = questionnaire.getBitsByQuestionNr(questionNr);
        assertEquals(questionBits, questionResult);

        questionBits = "110010";
        questionNr = 4;
        questionnaire.setBitsByQuestionNr(questionNr, questionBits);
        questionResult = questionnaire.getBitsByQuestionNr(questionNr);
        assertEquals(questionBits, questionResult);

        questionBits = "010";
        questionnaire.setBitsByQuestionNr(questionNr, questionBits);
        questionResult = questionnaire.getBitsByQuestionNr(questionNr);
        assertEquals("Wrong input because of invalid input length, so do not change bits", "110010", questionResult);
    }

    @Test
    public void testSetAndGetBitsByQuestionNr5(){
        DistressThermometerQuestionnaire questionnaire = new DistressThermometerQuestionnaire("Patrizia");
        String questionBits = "000000";
        int questionNr = 4;
        questionnaire.setBitsByQuestionNr(questionNr, questionBits);
        String questionResult = questionnaire.getBitsByQuestionNr(questionNr);
        assertEquals(questionBits, questionResult);

        questionBits = "11";
        questionNr = 5;
        questionnaire.setBitsByQuestionNr(questionNr, questionBits);
        questionResult = questionnaire.getBitsByQuestionNr(questionNr);
        assertEquals(questionBits, questionResult);

        questionBits = "010";
        questionnaire.setBitsByQuestionNr(questionNr, questionBits);
        questionResult = questionnaire.getBitsByQuestionNr(questionNr);
        assertEquals("Wrong input because of invalid input length, so do not change bits", "11", questionResult);
    }

    @Test
    public void testSetAndGetBitsByQuestionNr6(){
        DistressThermometerQuestionnaire questionnaire = new DistressThermometerQuestionnaire("Patrizia");
        String questionBits = "00";
        int questionNr = 5;
        questionnaire.setBitsByQuestionNr(questionNr, questionBits);
        String questionResult = questionnaire.getBitsByQuestionNr(questionNr);
        assertEquals(questionBits, questionResult);

        questionBits = "111111111111111111111";
        questionNr = 6;
        questionnaire.setBitsByQuestionNr(questionNr, questionBits);
        questionResult = questionnaire.getBitsByQuestionNr(questionNr);
        assertEquals(questionBits, questionResult);

        questionBits = "010";
        questionnaire.setBitsByQuestionNr(questionNr, questionBits);
        questionResult = questionnaire.getBitsByQuestionNr(questionNr);
        assertEquals("Wrong input because of invalid input length, so do not change bits", "111111111111111111111", questionResult);
    }

    @Test
    public void testUpdateQuestionnaire() throws Exception {
        // create user
        User user = new User("Captian Bluebear");
        userDB.insertUser(user);

        // create questionnaire with question by binary strings
        DistressThermometerQuestionnaire q = new DistressThermometerQuestionnaire(user.getName());
        q.setBitsByQuestionNr(1, "1010101010");
        q.setBitsByQuestionNr(2,"10001");
        q.setBitsByQuestionNr(3,"00");
        q.setBitsByQuestionNr(4,"101100");
        q.setBitsByQuestionNr(5,"11");
        q.setBitsByQuestionNr(6,"111100001111000011110");
        db.insertQuestionnaire(q);

        // check if created
        DistressThermometerQuestionnaire result = db.getDistressThermometerQuestionnaireByUserName(user.getName());
        assertEquals("1010101010", result.getBitsByQuestionNr(1));
        assertEquals("10001", result.getBitsByQuestionNr(2));
        assertEquals("00", result.getBitsByQuestionNr(3));
        assertEquals("101100", result.getBitsByQuestionNr(4));
        assertEquals("11", result.getBitsByQuestionNr(5));
        assertEquals("111100001111000011110", result.getBitsByQuestionNr(6));

        // update question nr. 1
        result.setBitsByQuestionNr(1, "0101010101");
        db.update(result);

        // check update
        DistressThermometerQuestionnaire updated = db.getDistressThermometerQuestionnaireByUserName(user.getName());
        assertEquals("0101010101", updated.getBitsByQuestionNr(1));

        // update question nr. 30 because it has 8 bits instead of 4 like the others
        updated.setBitsByQuestionNr(6, "000000100000000110000");
        db.update(updated);

        // check the update
        DistressThermometerQuestionnaire updated30 = db.getDistressThermometerQuestionnaireByUserName(user.getName());
        assertEquals("000000100000000110000", updated30.getBitsByQuestionNr(6));
    }
}
