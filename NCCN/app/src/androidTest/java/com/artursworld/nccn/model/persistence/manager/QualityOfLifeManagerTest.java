package com.artursworld.nccn.model.persistence.manager;


import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;

import com.artursworld.nccn.controller.util.Bits;
import com.artursworld.nccn.model.entity.QolQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

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
        StringBuilder expect = new StringBuilder(); // 8
        expect.append("00010001000100010001000100010001000100010001000100010001000100010001000100010001"); // 20
        expect.append("00010001000100010001000100010001"); // 8
        expect.append("0000000100000001");
        expect.append("00010001000100010001000100010001000100010001000100010001000100010001000100010001"); // 20
        assertEquals(expect.toString(), questionnaire.getAnswersToQuestionsAsString());
        assertEquals("8 bits per byte arrays * 26 arrays",8 * 26, questionnaire.getAnswersToQuestionsAsString().length());
    }

    @Test
    public void testGetBitsByQuestionNr(){
        QolQuestionnaire questionnaire = new QolQuestionnaire("Johannes");

        String questionNr7 = questionnaire.getBitsByQuestionNr(7);
        assertEquals("0001", questionNr7);

        String questionNr29 = questionnaire.getBitsByQuestionNr(29);
        assertEquals("00000001", questionNr29);

        String questionNr30 = questionnaire.getBitsByQuestionNr(30);
        assertEquals("00000001", questionNr30);

        String questionNr31 = questionnaire.getBitsByQuestionNr(31);
        assertEquals("0001", questionNr31);

        String questionNr50 = questionnaire.getBitsByQuestionNr(50);
        assertEquals("0001", questionNr50);
    }

    @Test
    public void testSetBitsByQuestionNr(){
        QolQuestionnaire questionnaire = new QolQuestionnaire("Yasmin");

        questionnaire.setBitsByQuestionNr(7, "1111");
        String questionNr7 = questionnaire.getBitsByQuestionNr(7);
        assertEquals("1111", questionNr7);

        questionnaire.setBitsByQuestionNr(29, "11110001");
        String questionNr29 = questionnaire.getBitsByQuestionNr(29);
        assertEquals("11110001", questionNr29);

        questionnaire.setBitsByQuestionNr(30, "01010101");
        String questionNr30 = questionnaire.getBitsByQuestionNr(30);
        assertEquals("01010101", questionNr30);

        questionnaire.setBitsByQuestionNr(31, "1111");
        String questionNr31 = questionnaire.getBitsByQuestionNr(31);
        assertEquals("1111", questionNr31);

        questionnaire.setBitsByQuestionNr(50, "0000");
        String questionNr50 = questionnaire.getBitsByQuestionNr(50);
        assertEquals("0000", questionNr50);

        questionnaire.setBitsByQuestionNr(1, "00");
        String questionNr1 = questionnaire.getBitsByQuestionNr(1);
        assertEquals("In this case it should not change the bis, because wrong input", "0001", questionNr1);

        questionnaire.setBitsByQuestionNr(30, "0000000000000");
        String questionNr30_2 = questionnaire.getBitsByQuestionNr(30);
        assertEquals("In this case it should not change the bis, because wrong input", "01010101", questionNr30_2);
    }

    @Test
    public void testUpdateQuestionnaire() throws Exception {
        // create user
        User medUser = new User("Rush Hour");
        userDB.insertUser(medUser);

        // create questionnaire with question 1 = 0111
        int questionNr = 1;
        QolQuestionnaire q = new QolQuestionnaire(medUser.getName());
        q.setBitsByQuestionNr(questionNr, "0111");
        String s=q.getAnswersToQuestionsAsString();
        db.insertQuestionnaire(q);

        // check if created
        QolQuestionnaire result = db.getQolQuestionnaireByUserName(medUser.getName());
        assertEquals("0111", result.getBitsByQuestionNr(questionNr));

        // update question nr. 1
        result.setBitsByQuestionNr(questionNr, "0011");
        db.update(result);

        // check update
        QolQuestionnaire updated = db.getQolQuestionnaireByUserName(medUser.getName());
        assertEquals("0011", updated.getBitsByQuestionNr(questionNr));

        // update question nr. 30 because it has 8 bits instead of 4 like the others
        updated.setBitsByQuestionNr(30, "00001111");
        db.update(updated);

        // check the update
        QolQuestionnaire updated30 = db.getQolQuestionnaireByUserName(medUser.getName());
        assertEquals("00001111", updated30.getBitsByQuestionNr(30));
    }


    @Test
    public void testExceptionalCase1111(){
        // create user
        User medUser = new User("Revolution");
        userDB.insertUser(medUser);

        // create questionnaire with question 1 = 1111
        int questionNr = 1;
        QolQuestionnaire q = new QolQuestionnaire(medUser.getName());
        q.setBitsByQuestionNr(questionNr, "1111");
        String s=q.getAnswersToQuestionsAsString();
        String s2 = q.getBitsByQuestionNr(questionNr);
        db.insertQuestionnaire(q);

        // check if created
        QolQuestionnaire result = db.getQolQuestionnaireByUserName(medUser.getName());
        assertEquals("1111", result.getBitsByQuestionNr(questionNr));
    }


    @Test
    public void testExceptionalCase1000(){
        // create user
        User medUser = new User("International");
        userDB.insertUser(medUser);

        // create questionnaire with question 1 = 1000
        int questionNr = 1;
        QolQuestionnaire q = new QolQuestionnaire(medUser.getName());
        q.setBitsByQuestionNr(questionNr, "1000");
        //String s=q.getAnswersToQuestionsA sString();
        //String s2 = q.getBitsByQuestionNr(questionNr);
        db.insertQuestionnaire(q);

        // check if created
        QolQuestionnaire result = db.getQolQuestionnaireByUserName(medUser.getName());
        assertEquals("1000", result.getBitsByQuestionNr(questionNr));
    }

    @Test
    public void testRegEx(){
        String byteString = "1000000100010001000100010001000100010001000100010001000100010001000100010001000100010001000100010001000100010001000000010000000100010001000100010001000100010001000100010001000100010001000100010001000100010001";
        Iterable<String> result = Splitter.fixedLength(8).split(byteString);
        String[] parts = Iterables.toArray(result, String.class);
        assertEquals(26,parts.length);
    }


}
