package com.artursworld.nccn.controller.wizard;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.util.Global;
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;
import com.artursworld.nccn.model.persistence.manager.DistressThermometerQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.HADSDQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;
import com.artursworld.nccn.model.wizard.distressthermometer.BodyProblemsStep;
import com.artursworld.nccn.model.wizard.distressthermometer.EmotionalProblemsStep;
import com.artursworld.nccn.model.wizard.distressthermometer.FamilyProblemsStep;
import com.artursworld.nccn.model.wizard.distressthermometer.PracticalProblemsStep;
import com.artursworld.nccn.model.wizard.distressthermometer.ReligiousSpiritualProblems;
import com.artursworld.nccn.model.wizard.distressthermometer.ThermometerStep;
import com.artursworld.nccn.model.wizard.psychosocial.PastPsychoSocialStep;
import com.artursworld.nccn.model.wizard.psychosocial.PsychoSocialStep;
import com.artursworld.nccn.model.wizard.qualityoflife.QualityOfLifeSpecialStep;
import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.TextStepper;


/**
 * This Wizard stands for the Distress Thermometer
 */
public class WizardNCCN extends TextStepper {

    private static String CLASS_NAME = WizardNCCN.class.getSimpleName();
    private int currentWizardPosition = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setErrorTimeout(1000);
        setTitle(Strings.getStringByRId(R.string.app_name));

        addStep(createFragment(new ThermometerStep()));
        addStep(createFragment(new PracticalProblemsStep()));
        addStep(createFragment(new FamilyProblemsStep()));
        addStep(createFragment(new EmotionalProblemsStep()));
        addStep(createFragment(new ReligiousSpiritualProblems()));
        addStep(createFragment(new BodyProblemsStep()));
        addStep(createFragment(new PastPsychoSocialStep()));
        addStep(createFragment(new PsychoSocialStep()));
        super.onCreate(savedInstanceState);
    }

    private AbstractStep createFragment(AbstractStep fragment) {
        Bundle bundle = new Bundle();
        bundle.putInt(WizardQualityOfLife.QUESTION_NUMBER, currentWizardPosition ++); // TODO: Why "WizardQualityOfLife"?
        String selectedUserName = Global.getSelectedUser();
        bundle.putString(WizardQualityOfLife.SELECTED_USER, selectedUserName);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static void updateProgress(final DistressThermometerQuestionnaire questionnaire, int questionNr){
        Log.i(CLASS_NAME, "updating progress for DistressThermometerQuestionnaire...");
        int progressValue = (int) Math.floor(questionNr / 6. * 100);
        if(questionnaire.getProgressInPercent() < progressValue){
            Log.i(CLASS_NAME, "new progress value = " + progressValue);
            questionnaire.setProgressInPercent(progressValue);
            new AsyncTask<Void, Void, Void>(){

                @Override
                protected Void doInBackground(Void... voids) {
                    new DistressThermometerQuestionnaireManager().update(questionnaire);
                    return null;
                }
            }.execute();
        }
    }


    @Override
    public void onComplete() {
        super.onComplete();
        Log.i(WizardNCCN.class.getSimpleName(), "completed Wizard NCCN");
        finish();
    }

}
