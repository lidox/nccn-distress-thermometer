package com.artursworld.nccn.model.wizard.distressthermometer;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.util.Bits;
import com.artursworld.nccn.controller.util.Global;
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.controller.wizard.WizardNCCN;
import com.artursworld.nccn.controller.wizard.WizardQualityOfLife;
import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;
import com.artursworld.nccn.model.entity.QolQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.DistressThermometerQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;
import com.github.fcannizzaro.materialstepper.AbstractStep;

import java.util.ArrayList;
import java.util.List;

public class PracticalProblemsStep extends AbstractStep {

    private String CLASS_NAME = PracticalProblemsStep.class.getSimpleName();

    // UI
    private CheckBox answerA_btn;
    private CheckBox answerB_btn;
    private CheckBox answerC_btn;
    private CheckBox answerD_btn;
    private CheckBox answerE_btn;

    // CONTROLLER
    private User selectedUser = null;
    private DistressThermometerQuestionnaire questionnaire = null;
    private int currentQuestionNumber = 2;
    private List<CheckBox> checkBoxList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = getView(inflater, container);
        initBundledData();
        setUIValuesByDB();
        addCheckboxChangeListener();
        return v;
    }

    /**
     * Adds to all checkBoxes a change listener
     */
    private void addCheckboxChangeListener() {
        for (int i = 0; i < 5; i++){
            CheckBox box = checkBoxList.get(i);
            final int index = i;
            box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    changeBitByIndex(isChecked,index);
                }
            });
        }
    }

    private void changeBitByIndex(boolean isChecked, int indexOfCAnswerCheckBox) {
        char bit = ((isChecked? 1 : 0) + "").charAt(0);
        StringBuilder binaryString = new StringBuilder(questionnaire.getBitsByQuestionNr(currentQuestionNumber));
        binaryString.setCharAt(indexOfCAnswerCheckBox, bit);
        questionnaire.setBitsByQuestionNr(currentQuestionNumber, binaryString.toString());
        Log.i(CLASS_NAME, "try to changed bits to: " + binaryString.toString() + " for questionNr:" + currentQuestionNumber);
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                new DistressThermometerQuestionnaireManager().update(questionnaire);
                return null;
            }
        }.execute();

    }

    /**
     * Sets checkbox as checked by database
     */
    private void setUIValuesByDB() {
        if(questionnaire != null) {
            String binaryStringByQuestionNr = questionnaire.getBitsByQuestionNr(currentQuestionNumber);
            Log.i(CLASS_NAME, "answer bits loaded: " + binaryStringByQuestionNr + " for questionNr:" + currentQuestionNumber);
            StringBuilder bits = new StringBuilder(binaryStringByQuestionNr).reverse();
            checkBoxByNameAndNr(bits, answerA_btn, 4);
            checkBoxByNameAndNr(bits, answerB_btn, 3);
            checkBoxByNameAndNr(bits, answerC_btn, 2);
            checkBoxByNameAndNr(bits, answerD_btn, 1);
            checkBoxByNameAndNr(bits, answerE_btn, 0);
        }
    }

    private void checkBoxByNameAndNr(StringBuilder bits, CheckBox chechBox, int checkBoxNumber) {
        try {
            boolean isChecked = bits.charAt(checkBoxNumber) == '1';
            if(chechBox != null){
                Log.i(CLASS_NAME, "check first checkbox = " + isChecked);
                chechBox.setChecked(isChecked);
            }
        }catch (Exception e){
            Log.e(CLASS_NAME, e.getLocalizedMessage());
        }
    }

    private void initBundledData() {
        Bundle bundle = getArguments();
        selectedUser = new UserManager().getUserByName(bundle.getString(WizardQualityOfLife.SELECTED_USER));
        questionnaire = new DistressThermometerQuestionnaireManager().getDistressThermometerQuestionnaireByDate(selectedUser.getName(), Global.getSelectedQuestionnaireDate());
    }

    @NonNull
    private View getView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.step_practical_problems, container, false);
        answerA_btn = (CheckBox) v.findViewById(R.id.housing_situation);
        answerB_btn = (CheckBox) v.findViewById(R.id.insurance);
        answerC_btn = (CheckBox) v.findViewById(R.id.work_or_school);
        answerD_btn = (CheckBox) v.findViewById(R.id.transport);
        answerE_btn = (CheckBox) v.findViewById(R.id.childcare);
        checkBoxList = new ArrayList<>();
        checkBoxList.add(answerA_btn);
        checkBoxList.add(answerB_btn);
        checkBoxList.add(answerC_btn);
        checkBoxList.add(answerD_btn);
        checkBoxList.add(answerE_btn);
        return v;
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
        Log.i(CLASS_NAME, "onStepVisible");
    }

    @Override
    public void onNext() {
        WizardNCCN.updateProgress(questionnaire, currentQuestionNumber);
        Log.i(CLASS_NAME, "onNext with questionNr. " + currentQuestionNumber );
    }

    @Override
    public void onPrevious() {
        Log.i(CLASS_NAME, "onPrevious");
    }

    @Override
    public String optional() {
        return Strings.getStringByRId(R.string.can_skip);
    }

    @Override
    public String error() {
        return "<b>You must click!</b> <small>this is the condition!</small>";
    }
}
