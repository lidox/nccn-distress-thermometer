package com.artursworld.nccn.model.persistence.manager;


import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;

import com.artursworld.nccn.controller.util.Bits;
import com.artursworld.nccn.model.entity.HADSDQuestionnaire;
import com.artursworld.nccn.model.entity.User;

import org.junit.Test;


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
        byte[] bytes = Bits.getByteByString(byteString);
        String result = Bits.getStringByByte(bytes);
        assertEquals(byteString, result);

        byteString = "0111010100101010"; // 16 characters
        bytes = Bits.getByteByString(byteString);
        result = Bits.getStringByByte(bytes);
        assertEquals(byteString, result);

        byteString = "00000000";
        bytes = Bits.getByteByString(byteString);
        result = Bits.getStringByByte(bytes);
        assertEquals(byteString, result);
    }

    @Test
    public void testCreateAndGetQuestionaire() throws Exception {
        User medUser = new User("Arturo Vidal");
        userDB.insertUser(medUser);

        HADSDQuestionnaire q = new HADSDQuestionnaire(medUser.getName());
        q.getAnswerToQuestionList().set(0, Bits.getByteByString("00000101"));
        q.setAnswerByNr(1, Bits.getByteByString("00000000"));

        hadsdDB.insertQuestionnaire(q);
        HADSDQuestionnaire result = hadsdDB.getHADSDQuestionnaireByUserName(medUser.getName());
        assertEquals(Bits.getStringByByte(Bits.getByteByString("00000101")), Bits.getStringByByte(result.getAnswerToQuestionList().get(0)));
        assertEquals(Bits.getStringByByte(Bits.getByteByString("00000000")), Bits.getStringByByte(result.getAnswerByNr(1)));
    }

    @Test
    public void testUpdateQuestionaireByCheckBox() throws Exception {
        User medUser = new User("Peter Lustig");
        userDB.insertUser(medUser);

        HADSDQuestionnaire q = new HADSDQuestionnaire(medUser.getName());
        hadsdDB.insertQuestionnaire(q);
        int questionNr = 0;
        byte[] newByte = Bits.getNewByteByCheckBox(true, 3, q.getAnswerByNr(questionNr));
        assertEquals("00001000", Bits.getStringByByte(newByte));
        q.setAnswerByNr(questionNr, newByte);
        hadsdDB.update(q);

        HADSDQuestionnaire result = hadsdDB.getHADSDQuestionnaireByUserName(medUser.getName());
        String resultByteString = Bits.getStringByByte(result.getAnswerByNr(questionNr));
        assertEquals("00001000", resultByteString);
    }

    @Test
    public void testUpdateQuestionaireByRadioBtn() throws Exception {
        User medUser = new User("Tony D");
        userDB.insertUser(medUser);
        int questionNr = 0;
        HADSDQuestionnaire q = new HADSDQuestionnaire(medUser.getName());
        String startingBits = "00000001";
        q.setAnswerByNr(questionNr, Bits.getByteByString(startingBits));
        hadsdDB.insertQuestionnaire(q);

        HADSDQuestionnaire test = hadsdDB.getHADSDQuestionnaireByUserName(medUser.getName());
        assertEquals(startingBits, Bits.getStringByByte(test.getAnswerByNr(questionNr)));

        byte[] newByte = Bits.getNewByteByRadioBtn(true, 3, q.getAnswerByNr(questionNr));
        assertEquals("00001000", Bits.getStringByByte(newByte));
        q.setAnswerByNr(questionNr, newByte);
        hadsdDB.update(q);

        HADSDQuestionnaire result = hadsdDB.getHADSDQuestionnaireByUserName(medUser.getName());
        String resultByteString = Bits.getStringByByte(result.getAnswerByNr(questionNr));
        assertEquals("00001000", resultByteString);
    }

    @Test
    public void testCreateAndGetQuestionaire2() throws Exception {
        User medUser = new User("Siggi");
        userDB.insertUser(medUser);

        HADSDQuestionnaire q = new HADSDQuestionnaire(medUser.getName());
        q.setAnswerByNr(0, Bits.getByteByString("00000001"));
        q.setAnswerByNr(1, Bits.getByteByString("00000010"));
        q.setAnswerByNr(2, Bits.getByteByString("00000100"));
        q.setAnswerByNr(3, Bits.getByteByString("00001000"));
        q.setAnswerByNr(4, Bits.getByteByString("00010000"));
        q.setAnswerByNr(5, Bits.getByteByString("00100000"));
        q.setAnswerByNr(6, Bits.getByteByString("01000000"));
        q.setAnswerByNr(7, Bits.getByteByString("00000001"));
        q.setAnswerByNr(8, Bits.getByteByString("00000101"));
        q.setAnswerByNr(9, Bits.getByteByString("00001101"));
        q.setAnswerByNr(10, Bits.getByteByString("00110101"));
        q.setAnswerByNr(11, Bits.getByteByString("01100101"));
        q.setAnswerByNr(12, Bits.getByteByString("11000101"));
        q.setAnswerByNr(13, Bits.getByteByString("11111111"));
        hadsdDB.insertQuestionnaire(q);

        HADSDQuestionnaire result = hadsdDB.getHADSDQuestionnaireByUserName(medUser.getName());
        assertEquals(Bits.getStringByByte(Bits.getByteByString("00000001")), Bits.getStringByByte(result.getAnswerToQuestionList().get(0)));
        assertEquals(Bits.getStringByByte(Bits.getByteByString("00000010")), Bits.getStringByByte(result.getAnswerByNr(1)));
        assertEquals(Bits.getStringByByte(Bits.getByteByString("00000100")), Bits.getStringByByte(result.getAnswerByNr(2)));
        assertEquals(Bits.getStringByByte(Bits.getByteByString("00001000")), Bits.getStringByByte(result.getAnswerByNr(3)));
        assertEquals(Bits.getStringByByte(Bits.getByteByString("00010000")), Bits.getStringByByte(result.getAnswerByNr(4)));
        assertEquals(Bits.getStringByByte(Bits.getByteByString("00100000")), Bits.getStringByByte(result.getAnswerByNr(5)));
        assertEquals(Bits.getStringByByte(Bits.getByteByString("01000000")), Bits.getStringByByte(result.getAnswerByNr(6)));
        assertEquals(Bits.getStringByByte(Bits.getByteByString("00000001")), Bits.getStringByByte(result.getAnswerByNr(7)));
        assertEquals(Bits.getStringByByte(Bits.getByteByString("00000101")), Bits.getStringByByte(result.getAnswerByNr(8)));
        assertEquals(Bits.getStringByByte(Bits.getByteByString("00001101")), Bits.getStringByByte(result.getAnswerByNr(9)));
        assertEquals(Bits.getStringByByte(Bits.getByteByString("00110101")), Bits.getStringByByte(result.getAnswerByNr(10)));
        assertEquals(Bits.getStringByByte(Bits.getByteByString("01100101")), Bits.getStringByByte(result.getAnswerByNr(11)));
        assertEquals(Bits.getStringByByte(Bits.getByteByString("11000101")), Bits.getStringByByte(result.getAnswerByNr(12)));
        assertEquals(Bits.getStringByByte(Bits.getByteByString("11111111")), Bits.getStringByByte(result.getAnswerByNr(13)));
    }


}
