package com.artursworld.nccn.controller.wizard;


import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.config.App;
import com.artursworld.nccn.controller.util.Questionnairy;
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.UserManager;
import com.artursworld.nccn.model.wizard.hadsd.AbstractHadsdStep;
import com.github.fcannizzaro.materialstepper.style.TextStepper;

import java.util.List;

public class WizardQualityOfLife extends TextStepper {

    public static final String QUESTION_DATA = "WizardQualityOfLife-question-data";
    public static final String QUESTION_NUMBER = "WizardQualityOfLife-QUESTION_NUMBER";
    public static final String SELECTED_USER = "WizardQualityOfLife-SELECTED_USER";

    private int currentWizardPosition = 1;
    private User selectedUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // TODO: changes this to real selected user. Maybe this should not be transmitted to fragment. instead get it in fragment
        selectedUser = new UserManager().getAllUsers().get(0);

        // laod questionnairy into fragments
        putAllQuestionAndAnswersToNewFragments();

        // configuration
        setErrorTimeout(1000);
        setTitle(Strings.getStringByRId(R.string.app_name));
        super.onCreate(savedInstanceState);
    }


    /**
     * Get all questions and answers from resource file and put them question by question into sinle fragments
     */
    private void putAllQuestionAndAnswersToNewFragments() {
        List<TypedArray> questionnairyList = Questionnairy.getQuestionnairyListById(R.array.hadsd_questionnaire, App.getAppContext());
        for(TypedArray questionItem: questionnairyList){
            String question = questionItem.getString(0);
            boolean isSpecialQuestion = question.equals(Strings.getStringByRId(R.string.c_seven_possible));
            if(isSpecialQuestion){
                Log.i(WizardQualityOfLife.class.getSimpleName(), "Special questions");
            }
            else{
                String answerA = questionItem.getString(1);
                String answerB = questionItem.getString(2);
                String answerC = questionItem.getString(3);
                String answerD = questionItem.getString(4);
                addFragmentStepByTextIds(question, answerA, answerB, answerC, answerD);
            }
        }
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
        AbstractHadsdStep fragment = new AbstractHadsdStep();
        fragment.setArguments(bundle);
        addStep(fragment);
    }

    @Override
    public void onComplete() {
        super.onComplete();
        Log.i(WizardQualityOfLife.class.getSimpleName(), "completed WizardQualityOfLife");
    }
}