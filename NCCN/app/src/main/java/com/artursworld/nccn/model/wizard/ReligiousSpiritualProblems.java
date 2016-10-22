package com.artursworld.nccn.model.wizard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.util.Strings;
import com.github.fcannizzaro.materialstepper.AbstractStep;

public class ReligiousSpiritualProblems extends AbstractStep {

    private int currentIndex = 1;
    private String simpleClassName = ReligiousSpiritualProblems.class.getSimpleName();
    private final static String CLICK = Strings.getStringByRId(R.string.click);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.step_religious_spiritual_problems, container, false);

        if (savedInstanceState != null)
            currentIndex = savedInstanceState.getInt(CLICK, 0);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putInt(CLICK, currentIndex);
    }

    @Override
    public String name() {
        return "Tab " + getArguments().getInt("position", 0);
    }

    @Override
    public boolean isOptional() {
        return true;
    }

    @Override
    public void onStepVisible() {
        Log.i(simpleClassName, "onStepVisible");
    }

    @Override
    public void onNext() {
        Log.i(simpleClassName, "onNext");
    }

    @Override
    public void onPrevious() {
        Log.i(simpleClassName, "onPrevious");
    }

    @Override
    public String optional() {
        return Strings.getStringByRId(R.string.can_skip);
    }

    @Override
    public String error() {
        return "<b>You must click!</b> <small>this is the condition!</small>";
    }

    @Override
    public boolean nextIf() {
        return currentIndex > 1;
    }
}

