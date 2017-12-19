package com.artursworld.nccn;

import android.content.Context;
import android.support.annotation.NonNull;
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

import java.util.Date;

public class TestUtils {

    private static String CLASS_NAME = TestUtils.class.getSimpleName();

    @NonNull
    public static User createUser(Context context) {
        // get user instance
        User user = new User("Fritzchen Nr." + Generator.getRandomInRange(1, 1000000000));
        user.setGender(Gender.MALE);
        user.setBirthDate(new Date());

        // create user in the database
        UserManager userDb = new UserManager(context);
        userDb.insertUser(user);
        return user;
    }

    public static void createHadsdByUser(User user, Context context) {
        // create new questionnaire for HADS-D questionnaire
        HADSDQuestionnaire q1 = new HADSDQuestionnaire(user.getName());

        for (int i = 0; i < 14; i++) {
            String binaryStringSingleSlot = getBinaryStringSingleSlot(4);

            q1.setAnswerByNr(i, Bits.getByteByString(binaryStringSingleSlot));
            Log.d(CLASS_NAME, "HADS-D:" + q1.getAsJSON().toString());
        }

        // insert questionnaire into database
        HADSDQuestionnaireManager q1Db = new HADSDQuestionnaireManager(context);
        q1Db.insertQuestionnaire(q1);
    }

    public static void createDistressThermometer(User user, Context context) {
        // create new questionnaire for HADS-D questionnaire
        DistressThermometerQuestionnaire q1 = new DistressThermometerQuestionnaire(user.getName());
        q1.setBitsByQuestionNr(1, getBinaryStringSingleSlot(11));
        q1.setProgressInPercent(100);

        // insert questionnaire into database
        DistressThermometerQuestionnaireManager q1Db = new DistressThermometerQuestionnaireManager(context);
        q1Db.insertQuestionnaire(q1);
    }

    public static void createQualityOfLifeQuestionnaires(User user, Context context) {
        // create new questionnaire
        QolQuestionnaire questionnaire = new QolQuestionnaire(user.getName());

        for (int index = 1; index <= 50; index++) {
            if (index == 29 || index == 30)
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
    private static String getBinaryStringSingleSlot(int size) {
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
