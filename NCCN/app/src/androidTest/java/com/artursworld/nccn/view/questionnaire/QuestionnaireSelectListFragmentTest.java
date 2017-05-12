package com.artursworld.nccn.view.questionnaire;

import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;

import com.artursworld.nccn.controller.util.Security;
import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;
import com.artursworld.nccn.model.entity.HADSDQuestionnaire;
import com.artursworld.nccn.model.entity.QolQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.DistressThermometerQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.HADSDQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.QualityOfLifeManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertNotEquals;

public class QuestionnaireSelectListFragmentTest extends InstrumentationTestCase {

    private UserManager userDB;
    private DistressThermometerQuestionnaireManager distressDB;
    public RenamingDelegatingContext context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getInstrumentation().getTargetContext(), "test_");
        userDB = new UserManager(context);
        distressDB = new DistressThermometerQuestionnaireManager(context);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testGetProgressOfQuestionnaireById(){
        // Date as primkey for an context and user name
        Date dateKey = new Date();
        String userName = "Regina for president";


        User user = new User(userName);
        userDB.insertUser(user);

        //add questionnaires
        HADSDQuestionnaire q1 = new HADSDQuestionnaire(userName);
        q1.setCreationDate_PK(dateKey);
        q1.setLastQuestionEditedNr(4);
        q1.setProgressInPercent(99);
        new HADSDQuestionnaireManager(context).insertQuestionnaire(q1);

        DistressThermometerQuestionnaire q2 = new DistressThermometerQuestionnaire(userName);
        q2.setCreationDate_PK(dateKey);
        q2.setLastQuestionEditedNr(2);
        q2.setProgressInPercent(77);
        new DistressThermometerQuestionnaireManager(context).insertQuestionnaire(q2);

        QolQuestionnaire q3 = new QolQuestionnaire(userName);
        q3.setCreationDate_PK(dateKey);
        q3.setLastQuestionEditedNr(0);
        q3.setProgressInPercent(33);
        new QualityOfLifeManager(context).insertQuestionnaire(q3);

        // get selected questionnaire
        user = userDB.getUserByName(userName);

        int hadsProgress = new HADSDQuestionnaireManager(context).getHADSDQuestionnaireByDate_PK(user.getName(), dateKey).getProgressInPercent();
        int distressProgress = new DistressThermometerQuestionnaireManager(context).getDistressThermometerQuestionnaireByDate(user.getName(), dateKey).getProgressInPercent();
        int qualityProgress = new QualityOfLifeManager(context).getQolQuestionnaireByDate(user.getName(), dateKey).getProgressInPercent();

        assertEquals(99, hadsProgress);
        assertEquals(77, distressProgress);
        assertEquals(33, qualityProgress);
    }

    @Test
    public void testEncryption1() {
        String text = "Hallo-ABL!~-!§%!";
        String encrypted = Security.encrypt(text);
        assertNotEquals(encrypted, text);
        String decrypted = Security.decrypt(encrypted);
        assertEquals(text, decrypted);
    }


}
