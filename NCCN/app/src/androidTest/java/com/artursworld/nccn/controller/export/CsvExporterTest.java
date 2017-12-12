package com.artursworld.nccn.controller.export;

import android.content.Context;
import android.support.annotation.NonNull;
import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import com.artursworld.nccn.controller.util.Bits;
import com.artursworld.nccn.controller.util.Generator;
import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;
import com.artursworld.nccn.model.entity.Gender;
import com.artursworld.nccn.model.entity.HADSDQuestionnaire;
import com.artursworld.nccn.model.entity.QolQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.DistressThermometerQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.HADSDQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.QualityOfLifeManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Date;
import java.util.List;


public class CsvExporterTest extends InstrumentationTestCase {

    private RenamingDelegatingContext context;
    private static String CLASS_NAME = CsvExporterTest.class.getSimpleName();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        Context targetContext = getInstrumentation().getTargetContext();
        context = new RenamingDelegatingContext(targetContext, "test_");
    }

    @Test
    public void testContext() {
        if (context != null)
            Log.d(CLASS_NAME, "hahaha this test works like a charm! :)");

        Assert.assertNotNull("test if context works like a charm", context);
    }

    @Test
    public void testCreateUserAndQuestionnairesForExport() {

        // create a user
        User user = createUser();

        // create random amount of HADS-D questionnaires
        double hadsdCount = Generator.getRandomInRange(1, 100);
        for (int i = 0; i < hadsdCount; i++) {
            createHadsdByUser(user);
        }

        // create random amount of Distress Thermometer questionnaires
        double distressThermometerCount = Generator.getRandomInRange(1, 100);
        for (int i = 0; i < distressThermometerCount; i++) {
            createDistressThermometer(user);
        }

        // create random amount of 'quality of life' questionnaires
        double qualityOfLifeCount = Generator.getRandomInRange(1, 100);
        for (int i = 0; i < qualityOfLifeCount; i++) {
            createQualityOfLifeQuestionnaires(user);
        }


        // make check if database contains questionnaires
        HADSDQuestionnaireManager db = new HADSDQuestionnaireManager(context);
        List<HADSDQuestionnaire> list = db.getHadsdQuestionnaireListByUserName(user.getName());
        Assert.assertTrue("check if list contains elements", list.size() > 0);

        List<String[]> testList = CsvExporter.getExportableCsvListByUser(user, context);
        assertTrue(testList != null);
    }


    @NonNull
    private User createUser() {
        // get user instance
        User user = new User("Peter der 1.");
        user.setGender(Gender.MALE);
        user.setBirthDate(new Date());

        // create user in the database
        UserManager userDb = new UserManager(context);
        userDb.insertUser(user);
        return user;
    }

    private void createHadsdByUser(User user) {
        // create new questionnaire for HADS-D questionnaire
        HADSDQuestionnaire q1 = new HADSDQuestionnaire(user.getName());

        for(int i = 0; i < 14; i++){
            String binaryStringSingleSlot = getBinaryStringSingleSlot(4);

            q1.setAnswerByNr(i, Bits.getByteByString(binaryStringSingleSlot));
            Log.d(CLASS_NAME, "HADS-D:" +q1.getAsJSON().toString());
        }

        // insert questionnaire into database
        HADSDQuestionnaireManager q1Db = new HADSDQuestionnaireManager(context);
        q1Db.insertQuestionnaire(q1);
    }

    private void createDistressThermometer(User user) {
        // create new questionnaire for HADS-D questionnaire
        DistressThermometerQuestionnaire q1 = new DistressThermometerQuestionnaire(user.getName());
        q1.setBitsByQuestionNr(1, getBinaryStringSingleSlot(11));

        // insert questionnaire into database
        DistressThermometerQuestionnaireManager q1Db = new DistressThermometerQuestionnaireManager(context);
        q1Db.insertQuestionnaire(q1);
    }

    private void createQualityOfLifeQuestionnaires(User user) {
        // create new questionnaire
        QolQuestionnaire questionnaire = new QolQuestionnaire(user.getName());

        for(int index = 1; index <= 50; index ++){
            if(index == 29 || index == 30)
                continue;
            questionnaire.setBitsByQuestionNr(index, getBinaryStringSingleSlot(4));
        }

        // exceptions
        questionnaire.setBitsByQuestionNr(29, getBinaryStringSingleSlot(8));
        questionnaire.setBitsByQuestionNr(30, getBinaryStringSingleSlot(8));

        // insert questionnaire into database
        QualityOfLifeManager manager = new QualityOfLifeManager(context);
        manager.insertQuestionnaire(questionnaire);
    }


    /**
     * Generates a binary string containing only containing a single '1' bit.
     * The rest is zero
     *
     * @param size the size of the binary string to be generated
     * @return a binary string containing only containing a single '1' bit. The rest is zero
     */
    @NonNull
    private String getBinaryStringSingleSlot(int size) {
        double random = Generator.getRandomInRange(0, size - 1);
        StringBuilder binaryString = new StringBuilder();
        for (int i = 0; i < size; i++) {
            if (i == random)
                binaryString.append("1");
            else
                binaryString.append("0");
        }
        return binaryString.toString();
    }

}
