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
import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;
import com.artursworld.nccn.model.entity.QolQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.DistressThermometerQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.QualityOfLifeManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;
import com.artursworld.nccn.model.wizard.qualityoflife.QualityOfLifeSpecialStep;
import com.artursworld.nccn.model.wizard.qualityoflife.QualityOfLifeStep;
import com.github.fcannizzaro.materialstepper.style.TextStepper;

import java.util.List;

public class WizardQualityOfLife extends TextStepper {

    private static final String CLASS_NAME = WizardQualityOfLife.class.getSimpleName();
    public static final String QUESTION_DATA = "WizardQualityOfLife-question-data";
    public static final String QUESTION_NUMBER = "WizardQualityOfLife-QUESTION_NUMBER";
    public static final String SELECTED_USER = "WizardQualityOfLife-SELECTED_USER";

    private int currentWizardPosition = 1;
    private User selectedUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String userName = Global.getSelectedUser();
        selectedUser = new UserManager().getUserByName(userName);

        // laod questionnaire into fragments
        putAllQuestionAndAnswersToNewFragments();

        // configuration
        setErrorTimeout(1000);
        setTitle(Strings.getStringByRId(R.string.app_name));
        super.onCreate(savedInstanceState);
    }


    /**
     * Get all questions and answers from resource file and put them question by question into sinle fragments
     */
    @SuppressWarnings("ResourceType")
    private void putAllQuestionAndAnswersToNewFragments() {
        List<TypedArray> questionnairyList = Questionnairy.getQuestionnairyListById(R.array.quality_of_life_questionnaire, App.getAppContext());

        for(TypedArray questionItem: questionnairyList){
            String question = questionItem.getString(0);
            boolean isSpecialQuestion = question.equals(Strings.getStringByRId(R.string.c_seven_possible));
            if(isSpecialQuestion){
                Log.i(WizardQualityOfLife.class.getSimpleName(), "Special questions");
                question = questionItem.getString(1);
                addFragmentStepBySpecialQuestion(question);
            }
            else{
                Log.i("", "loading question= " + question);
                String answerA = questionItem.getString(1);
                String answerB = questionItem.getString(2);
                String answerC = questionItem.getString(3);
                String answerD = questionItem.getString(4);
                addFragmentStepByTextIds(question, answerA, answerB, answerC, answerD);
            }
        }
    }

    private void addFragmentStepBySpecialQuestion(String question) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(QUESTION_DATA, new String[]{question});
        bundle.putInt(QUESTION_NUMBER, currentWizardPosition ++);
        bundle.putString(SELECTED_USER, selectedUser.getName());
        QualityOfLifeSpecialStep fragment = new QualityOfLifeSpecialStep();
        fragment.setArguments(bundle);
        addStep(fragment);
    }

    /**
     * Adds a single question and its answers with some configuration information to the questionnairy
     * @param question the question
     * @param answerA a possible answer
     * @param answerB a possible answer
     * @param answerC a possible answer
     * @param answerD a possible answer
     */
    private void addFragmentStepByTextIds(String question, String answerA, String answerB ,String answerC, String answerD) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(QUESTION_DATA, new String[]{question, answerA, answerB, answerC, answerD});
        bundle.putInt(QUESTION_NUMBER, currentWizardPosition ++);
        bundle.putString(SELECTED_USER, selectedUser.getName());
        QualityOfLifeStep fragment = new QualityOfLifeStep();
        fragment.setArguments(bundle);
        addStep(fragment);
    }

    @Override
    public void onComplete() {
        super.onComplete();
        Log.i(WizardQualityOfLife.class.getSimpleName(), "completed WizardQualityOfLife");
        finish();
    }

    public static void updateProgress(final QolQuestionnaire questionnaire, int questionNr){
        int progressValue = (int) Math.floor(questionNr / 50. * 100);
        if(questionnaire.getProgressInPercent() < progressValue){
            Log.i(CLASS_NAME, "new progress value = " + progressValue);
            questionnaire.setProgressInPercent(progressValue);

            new AsyncTask<Void, Void, Void>(){

                @Override
                protected Void doInBackground(Void... voids) {
                    new QualityOfLifeManager().update(questionnaire);
                    return null;
                }
            }.execute();
        }
    }
}
