package com.artursworld.nccn.controller.wizard;

import android.os.Bundle;
import android.util.Log;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.util.Global;
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.model.persistence.manager.UserManager;
import com.artursworld.nccn.model.wizard.distressthermometer.BodyProblemsStep;
import com.artursworld.nccn.model.wizard.distressthermometer.EmotionalProblemsStep;
import com.artursworld.nccn.model.wizard.distressthermometer.FamilyProblemsStep;
import com.artursworld.nccn.model.wizard.distressthermometer.PracticalProblemsStep;
import com.artursworld.nccn.model.wizard.distressthermometer.ReligiousSpiritualProblems;
import com.artursworld.nccn.model.wizard.distressthermometer.ThermometerStep;
import com.artursworld.nccn.model.wizard.qualityoflife.QualityOfLifeSpecialStep;
import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.TextStepper;


/**
 * This Wizard stands for the Distress Thermometer
 */
public class WizardNCCN extends TextStepper {

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
        super.onCreate(savedInstanceState);
    }

    private AbstractStep createFragment(AbstractStep fragment) {
        Bundle bundle = new Bundle();
        bundle.putInt(WizardQualityOfLife.QUESTION_NUMBER, currentWizardPosition ++);
        String selectedUserName = Global.getSelectedUser();
        bundle.putString(WizardQualityOfLife.SELECTED_USER, selectedUserName);
        //TODO: check selected questionnaire date
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onComplete() {
        super.onComplete();
        Log.i(WizardNCCN.class.getSimpleName(), "completed Wizard NCCN");
    }

}
