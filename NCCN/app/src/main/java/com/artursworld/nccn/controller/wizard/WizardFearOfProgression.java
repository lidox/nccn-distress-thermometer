package com.artursworld.nccn.controller.wizard;

import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.config.App;
import com.artursworld.nccn.controller.util.Global;
import com.artursworld.nccn.controller.util.Questionnairy;
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.model.entity.FearOfProgressionQuestionnaire;
import com.artursworld.nccn.model.entity.QolQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.QualityOfLifeManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;
import com.artursworld.nccn.model.wizard.fearofprogression.WizardFearOfProgressionStep;
import com.artursworld.nccn.model.wizard.qualityoflife.QualityOfLifeStep;
import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.TextStepper;

import java.util.List;

public class WizardFearOfProgression extends TextStepper {

    private static final String CLASS_NAME = WizardFearOfProgression.class.getSimpleName();
    public static final String QUESTION_DATA = CLASS_NAME + "-question-data";
    public static final String QUESTION_NUMBER = CLASS_NAME + "-QUESTION_NUMBER";
    public static final String SELECTED_USER = CLASS_NAME + "-SELECTED_USER";

    private int currentWizardPosition = 1;
    private User selectedUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        selectedUser = new UserManager().getUserByName(Global.getSelectedUser());

        // loading questionnaire into fragments
        putAllQuestionAndAnswersToNewFragments();

        // configuration
        setErrorTimeout(1000);
        setTitle(Strings.getStringByRId(R.string.app_name));
        super.onCreate(savedInstanceState);
    }


    /**
     * Get all questions and answers from resource file and put them into single fragments
     */
    @SuppressWarnings("ResourceType")
    private void putAllQuestionAndAnswersToNewFragments() {

        // get questionnaire list
        List<TypedArray> questionnaireList = Questionnairy.getQuestionnairyListById(R.array.q1_fear_of_progression_questionnaire, App.getAppContext());

        // for each question add a fragment
        for (TypedArray questionItem : questionnaireList) {

            // get question string
            String question = questionItem.getString(0);

            // get answers strings
            String answerA = questionItem.getString(1);
            String answerB = questionItem.getString(2);
            String answerC = questionItem.getString(3);
            String answerD = questionItem.getString(4);
            String answerE = questionItem.getString(5);

            // create fragment
            addFragmentStepByTextIds(new WizardFearOfProgressionStep(), question, answerA, answerB, answerC, answerD, answerE);
        }
    }

    /**
     * Adds a single question and its answers with some configuration information to the questionnaire
     *
     * @param questionAndAnswers first the question followed by all answers
     */
    private void addFragmentStepByTextIds(WizardFearOfProgressionStep stepFragment, String... questionAndAnswers) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(QUESTION_DATA, questionAndAnswers);
        bundle.putInt(QUESTION_NUMBER, currentWizardPosition++);
        bundle.putString(SELECTED_USER, selectedUser.getName());
        stepFragment.setArguments(bundle);
        addStep(stepFragment);
    }

    @Override
    public void onComplete() {
        super.onComplete();
        Log.i(CLASS_NAME, "completed " + CLASS_NAME);
        finish();
    }

    // TODO: update progress
    public static void updateProgress(final FearOfProgressionQuestionnaire questionnaire, int questionNr) {
        /*
        int progressValue = (int) Math.floor(questionNr / 50. * 100);
        if (questionnaire.getProgressInPercent() < progressValue) {
            Log.i(CLASS_NAME, "new progress value = " + progressValue);
            questionnaire.setProgressInPercent(progressValue);

            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... voids) {
                    new QualityOfLifeManager().update(questionnaire);
                    return null;
                }
            }.execute();
        }
        */
    }
}
