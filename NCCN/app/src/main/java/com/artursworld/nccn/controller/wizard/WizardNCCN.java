package com.artursworld.nccn.controller.wizard;

import android.os.Bundle;

import com.artursworld.nccn.model.wizard.ThermometerStep;
import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.TextStepper;


public class WizardNCCN extends TextStepper {

    private int currentWizardPosition = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setErrorTimeout(1000);
        setTitle("Text Stepper");

        addStep(createFragment(new ThermometerStep()));
        //addStep(createFragment(new StepSample()));
        //addStep(createFragment(new StepSample()));
        //addStep(createFragment(new StepSample()));
        //addStep(createFragment(new StepSample()));

        super.onCreate(savedInstanceState);

    }

    private AbstractStep createFragment(AbstractStep fragment) {
        Bundle b = new Bundle();
        b.putInt("position", currentWizardPosition++);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onComplete() {
        super.onComplete();
        System.out.println("completed Wizzard NCCN");
    }

}
