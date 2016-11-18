package com.artursworld.nccn.controller.wizard;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.model.entity.HADSDQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.HADSDQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;
import com.artursworld.nccn.model.wizard.hadsd.AbstractHadsdStep;
import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.TextStepper;



public class WizardHADSD extends TextStepper {

    public static final String QUESTION_DATA = "WizardHADSD-question-data";
    public static final String QUESTION_NUMBER = "WizardHADSD-QUESTION_NUMBER";
    public static final String SELECTED_USER = "WizardHADSD-SELECTED_USER";

    private int currentWizardPosition = 1;
    private User selectedUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        selectedUser = new UserManager().getAllUsers().get(0); // TODO: changes this to real selected user later

        TypedArray menuResources = getResources().obtainTypedArray(R.array.hadsd_questionnaire);
        TypedArray itemDef;
        for (int i = 0; i < menuResources.length(); i++) {
            int resId = menuResources.getResourceId(i, -1);
            if (resId < 0) {
                continue;
            }

            itemDef = getResources().obtainTypedArray(resId);
            String question = itemDef.getString(0);
            String answerA = itemDef.getString(1);
            String answerB = itemDef.getString(2);
            String answerC = itemDef.getString(3);
            String answerD = itemDef.getString(4);

            // add question and answers
            addStepByTextIds(question, answerA, answerB, answerC, answerD);
        }

        // configuration
        setErrorTimeout(1000);
        setTitle(Strings.getStringByRId(R.string.app_name));
        super.onCreate(savedInstanceState);
    }

    private void addStepByTextIds(String question, String answerA, String answerB ,String answerC, String answerD) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(QUESTION_DATA, new String[]{question, answerA, answerB, answerC, answerD});
        addStep(createFragment(new AbstractHadsdStep(), bundle));
    }

    private AbstractStep createFragment(AbstractStep fragment, Bundle b) {
        b.putInt(QUESTION_NUMBER, currentWizardPosition ++);
        b.putString(SELECTED_USER, selectedUser.getName());
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onComplete() {
        super.onComplete();
        Log.i(WizardHADSD.class.getSimpleName(), "completed WizardHADSD");
    }

}
