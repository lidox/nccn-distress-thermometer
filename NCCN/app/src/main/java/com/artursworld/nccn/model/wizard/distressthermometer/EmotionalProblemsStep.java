package com.artursworld.nccn.model.wizard.distressthermometer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.util.Global;
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.controller.wizard.WizardNCCN;
import com.artursworld.nccn.controller.wizard.WizardQualityOfLife;
import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.DistressThermometerQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;
import com.github.fcannizzaro.materialstepper.AbstractStep;

import java.util.ArrayList;
import java.util.List;

public class EmotionalProblemsStep extends AbstractStep {

    private String CLASS_NAME = EmotionalProblemsStep.class.getSimpleName();

    private DistressThermometerQuestionnaire questionnaire = null;

    // configuration
    private int currentQuestionNumber = 4;
    private int answerCount = 6;
    private List<CheckBox> checkBoxList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(CLASS_NAME, "onCreateView");
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
        Log.i(CLASS_NAME, "Adding checkbox change Listener");
        for (int i = 0; i < answerCount; i++){
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

    /**
     * Changes the bit by param isChecked. If is checked set '1' at the given index. Otherwise sets '0'
     * @param isChecked if checked use '1'. Otherwise '0'
     * @param indexOfAnswerCheckBox index of the bit to be checked or unchecked
     */
    private void changeBitByIndex(boolean isChecked, int indexOfAnswerCheckBox ) {
        questionnaire = new DistressThermometerQuestionnaireManager().getDistressThermometerQuestionnaireByDate(Global.getSelectedUser(), Global.getSelectedQuestionnaireDate());
        char bit = ((isChecked? 1 : 0) + "").charAt(0);
        StringBuilder binaryString = new StringBuilder(questionnaire.getBitsByQuestionNr(currentQuestionNumber));
        binaryString.setCharAt(indexOfAnswerCheckBox , bit);
        questionnaire.setBitsByQuestionNr(currentQuestionNumber, binaryString.toString());
        Log.i(CLASS_NAME, "try to changed bits to: " + binaryString.toString() + " for questionNr:" + currentQuestionNumber);
        new DistressThermometerQuestionnaireManager().update(questionnaire);
    }

    /**
     * Sets checkbox as checked by database
     */
    private void setUIValuesByDB() {
        String binaryStringByQuestionNr = questionnaire.getBitsByQuestionNr(currentQuestionNumber);
        Log.i(CLASS_NAME, "answer bits loaded: " + binaryStringByQuestionNr + " for questionNr:" + currentQuestionNumber);
        StringBuilder bits = new StringBuilder(binaryStringByQuestionNr).reverse();
        int j = 0;
        Log.i(CLASS_NAME, "CheckBoxes registered = " +checkBoxList.size()+", and bits count found = " + binaryStringByQuestionNr.length());
        for (int i = checkBoxList.size()-1; i >= 0; i--){
            checkBoxByNameAndNr(bits, checkBoxList.get(j), i);
            j++;
        }
        Log.i(CLASS_NAME, "set UI by database values finished");
    }

    /**
     * Set checkbox as checked by binary string, checkbox and checkboxNumber
     * @param bits the binary string for the current question
     * @param chechBox the checkbox to set as checked or unchecked
     * @param checkBoxNumber the number of the answer in the UI
     */
    private void checkBoxByNameAndNr(StringBuilder bits, CheckBox chechBox, int checkBoxNumber) {
        try {
            boolean isChecked = bits.charAt(checkBoxNumber) == '1';
            if(chechBox != null){
                chechBox.setChecked(isChecked);
                Log.i(CLASS_NAME, "Set box with ID=" + checkBoxNumber + " as checked="+ isChecked);
            }
        }catch (Exception e){
            Log.e(CLASS_NAME, e.getLocalizedMessage());
        }
    }

    /**
     * Loads the selected questionnaire and the selected user coming from the wizard
     */
    private void initBundledData() {
        Bundle bundle = getArguments();
        User selectedUser = new UserManager().getUserByName(bundle.getString(WizardQualityOfLife.SELECTED_USER));
        questionnaire = new DistressThermometerQuestionnaireManager().getDistressThermometerQuestionnaireByDate(selectedUser.getName(), Global.getSelectedQuestionnaireDate());
        Log.i(CLASS_NAME,"Questionnaire loaded by user("+selectedUser.getName()+") questionnaire=" +questionnaire);
        Log.i(CLASS_NAME, "Questionbits for questionNr("+ currentQuestionNumber+")=" +questionnaire.getBitsByQuestionNr(currentQuestionNumber));
    }

    private View getView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.step_emotional_problems, container, false);
        CheckBox answerA_btn = (CheckBox) v.findViewById(R.id.carrie);
        CheckBox answerB_btn = (CheckBox) v.findViewById(R.id.scary);
        CheckBox answerC_btn = (CheckBox) v.findViewById(R.id.unhappy);
        CheckBox answerD_btn = (CheckBox) v.findViewById(R.id.depressive);
        CheckBox answerE_btn = (CheckBox) v.findViewById(R.id.nervous);
        CheckBox answerF_btn = (CheckBox) v.findViewById(R.id.all_day_activity);
        checkBoxList = new ArrayList<>();
        checkBoxList.add(answerA_btn);
        checkBoxList.add(answerB_btn);
        checkBoxList.add(answerC_btn);
        checkBoxList.add(answerD_btn);
        checkBoxList.add(answerE_btn);
        checkBoxList.add(answerF_btn);
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
        questionnaire = new DistressThermometerQuestionnaireManager().getDistressThermometerQuestionnaireByDate(Global.getSelectedUser(), Global.getSelectedQuestionnaireDate());
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
