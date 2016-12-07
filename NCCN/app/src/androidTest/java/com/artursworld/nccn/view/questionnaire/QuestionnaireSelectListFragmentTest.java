package com.artursworld.nccn.view.questionnaire;

import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;

import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;
import com.artursworld.nccn.model.entity.HADSDQuestionnaire;
import com.artursworld.nccn.model.entity.QolQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.DistressThermometerQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.HADSDQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.QualityOfLifeManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;

import org.junit.Test;

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
        //TODO; get questionnaires of user

        // setup
        String userName = "Regina for president";
        User user = new User(userName);
        userDB.insertUser(user);

        //add questionnaires
        HADSDQuestionnaire q1 = new HADSDQuestionnaire(userName);
        q1.setLastQuestionEditedNr(4);
        q1.setProgressInPercent(99);
        new HADSDQuestionnaireManager(context).insertQuestionnaire(q1);

        DistressThermometerQuestionnaire q2 = new DistressThermometerQuestionnaire(userName);
        q2.setLastQuestionEditedNr(2);
        q2.setProgressInPercent(77);
        new DistressThermometerQuestionnaireManager(context).insertQuestionnaire(q2);

        QolQuestionnaire q3 = new QolQuestionnaire(userName);
        q3.setLastQuestionEditedNr(0);
        q3.setProgressInPercent(33);
        new QualityOfLifeManager(context).insertQuestionnaire(q3);

        // get selected questionnaire
        user = userDB.getUserByName(userName);

        //int hadsProgress = ..
        //assertEquals("8 bits per byte arrays * 6 arrays", 8 * 6, expected2.length());
    }

}
