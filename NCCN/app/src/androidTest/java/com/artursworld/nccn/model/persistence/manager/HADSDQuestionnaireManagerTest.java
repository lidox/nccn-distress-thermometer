package com.artursworld.nccn.model.persistence.manager;


import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;

import com.artursworld.nccn.model.entity.HADSDQuestionnaire;
import com.artursworld.nccn.model.entity.User;

import org.junit.Test;

import java.math.BigInteger;


public class HADSDQuestionnaireManagerTest extends InstrumentationTestCase {

    private UserManager userDB;
    private HADSDQuestionnaireManager hadsdDB;
    private RenamingDelegatingContext context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getInstrumentation().getTargetContext(), "test_");
        hadsdDB = new HADSDQuestionnaireManager(context);
        userDB = new UserManager(context);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }


    @Test
    public void testBytesAndStrings(){
        String byteString = "00000101";
        byte[] bytes = HADSDQuestionnaire.getByteByString(byteString);
        String result = HADSDQuestionnaire.getStringByByte(bytes);
        assertEquals(byteString, result);

        byteString = "10101010"; // 16 characters
        bytes = HADSDQuestionnaire.getByteByString(byteString);
        result = HADSDQuestionnaire.getStringByByte(bytes);
        assertEquals(byteString, result);

        byteString = "00000000";
        bytes = HADSDQuestionnaire.getByteByString(byteString);
        result = HADSDQuestionnaire.getStringByByte(bytes);
        assertEquals(byteString, result);

    }

    @Test
    public void testCreateAndGetQuestionaire() throws Exception {
        // create user to be inserted into database
        User medUser = new User("Arturo Vidal");
        userDB.insertUser(medUser);

        HADSDQuestionnaire q = new HADSDQuestionnaire(medUser.getName());
        q.setANSWER_TO_QUESTION1(HADSDQuestionnaire.getByteByString("00000101")); // 0101
        q.setANSWER_TO_QUESTION2(HADSDQuestionnaire.getByteByString("00000000")); // 0000

        hadsdDB.insertQuestionnaire(q);
        HADSDQuestionnaire result = hadsdDB.getHADSDQuestionnaireByUserName(medUser.getName());
        assertEquals(HADSDQuestionnaire.getStringByByte(HADSDQuestionnaire.getByteByString("00000101")), HADSDQuestionnaire.getStringByByte(result.getANSWER_TO_QUESTION1()));
        assertEquals(HADSDQuestionnaire.getStringByByte(HADSDQuestionnaire.getByteByString("00000000")), HADSDQuestionnaire.getStringByByte(result.getANSWER_TO_QUESTION2()));
    }

}
