package com.artursworld.nccn.model.persistence.manager;

import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;

import com.artursworld.nccn.model.entity.FearOfProgressionQuestionnaire;
import com.artursworld.nccn.model.entity.User;

import junit.framework.Assert;

import org.junit.Test;

public class FearOfProgressionTest extends InstrumentationTestCase {

    private UserManager userDB;
    private FearOfProgressionManager db;
    private RenamingDelegatingContext context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getInstrumentation().getTargetContext(), "test_");
        db = new FearOfProgressionManager(context);
        userDB = new UserManager(context);
    }

    @Test
    public void testFull() {
        // create user
        User medUser = new User("Rush Hour");
        userDB.insertUser(medUser);

        // create questionnaire with question 1 = 0111
        int questionNr = 1;
        FearOfProgressionQuestionnaire questionnaire = new FearOfProgressionQuestionnaire(medUser.getName());
        questionnaire.setProgressInPercent(100);
        questionnaire.setSelectedAnswerIndexQ1(1);
        questionnaire.setSelectedAnswerIndexQ2(1);
        questionnaire.setSelectedAnswerIndexQ3(1);
        questionnaire.setSelectedAnswerIndexQ4(1);
        questionnaire.setSelectedAnswerIndexQ5(1);
        questionnaire.setSelectedAnswerIndexQ6(1);
        questionnaire.setSelectedAnswerIndexQ7(1);
        questionnaire.setSelectedAnswerIndexQ8(1);
        questionnaire.setSelectedAnswerIndexQ9(1);
        questionnaire.setSelectedAnswerIndexQ10(1);
        questionnaire.setSelectedAnswerIndexQ11(1);
        questionnaire.setSelectedAnswerIndexQ12(1);

        double score = questionnaire.getScore();
        Assert.assertEquals(12., score);
        db.insertQuestionnaire(questionnaire);

        // check if created
        FearOfProgressionQuestionnaire result = db.getQuestionnaireByDate(medUser.getName(), questionnaire.getCreationDate_PK());
        assertEquals(12., result.getScore());

        // update question nr. 1
        result.setSelectedAnswerIndexQ1(5);
        db.update(result);

        // check update
        FearOfProgressionQuestionnaire updated = db.getQuestionnaireByDate(medUser.getName(), questionnaire.getCreationDate_PK());
        assertEquals(16., updated.getScore());
    }


}
