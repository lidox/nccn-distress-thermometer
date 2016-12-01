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
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.controller.wizard.WizardQualityOfLife;
import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.DistressThermometerQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;
import com.github.fcannizzaro.materialstepper.AbstractStep;

import java.util.ArrayList;
import java.util.List;

public class BodyProblemsStep extends AbstractStep {

    private String CLASS_NAME = BodyProblemsStep.class.getSimpleName();
    private DistressThermometerQuestionnaire questionnaire = null;

    // configuration
    private int currentQuestionNumber = 6;
    private int answerCount = 21;
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
        char bit = ((isChecked? 1 : 0) + "").charAt(0);
        StringBuilder binaryString = new StringBuilder(questionnaire.getBitsByQuestionNr(currentQuestionNumber));
        binaryString.setCharAt(indexOfAnswerCheckBox , bit);
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
        String binaryStringByQuestionNr = questionnaire.getBitsByQuestionNr(currentQuestionNumber);
        Log.i(CLASS_NAME, "answer bits loaded: " + binaryStringByQuestionNr + " for questionNr:" + currentQuestionNumber);
        StringBuilder bits = new StringBuilder(binaryStringByQuestionNr).reverse();
        int j = 0;
        for (int i = checkBoxList.size()-1; i >= 0; i--){
            checkBoxByNameAndNr(bits, checkBoxList.get(j), i);
            j++;
        }
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
                Log.i(CLASS_NAME, "check first checkbox = " + isChecked);
                chechBox.setChecked(isChecked);
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
        questionnaire = new DistressThermometerQuestionnaireManager().getDistressThermometerQuestionnaireByUserName(selectedUser.getName());
    }

    private View getView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.step_body_problems, container, false);
        CheckBox answerA_btn = (CheckBox) v.findViewById(R.id.hurts);
        CheckBox answerB_btn = (CheckBox) v.findViewById(R.id.ugly_feeling);
        CheckBox answerC_btn = (CheckBox) v.findViewById(R.id.no_power);
        CheckBox answerD_btn = (CheckBox) v.findViewById(R.id.sleep);
        CheckBox answerE_btn = (CheckBox) v.findViewById(R.id.motion);
        CheckBox answerF_btn = (CheckBox) v.findViewById(R.id.wash_dress);
        CheckBox answerG_btn = (CheckBox) v.findViewById(R.id.external_appearance);
        CheckBox answerH_btn = (CheckBox) v.findViewById(R.id.breathing);
        CheckBox answerI_btn = (CheckBox) v.findViewById(R.id.mouth_inflammation);
        CheckBox answerJ_btn = (CheckBox) v.findViewById(R.id.food_nutrition);
        CheckBox answerK_btn = (CheckBox) v.findViewById(R.id.digestive_disorders);
        CheckBox answerL_btn = (CheckBox) v.findViewById(R.id.constipation);
        CheckBox answerM_btn = (CheckBox) v.findViewById(R.id.diarrhea);
        CheckBox answerN_btn = (CheckBox) v.findViewById(R.id.changes_in_pee);
        CheckBox answerO_btn = (CheckBox) v.findViewById(R.id.fever);
        CheckBox answerP_btn = (CheckBox) v.findViewById(R.id.dry_or_itchy_skin);
        CheckBox answerQ_btn = (CheckBox) v.findViewById(R.id.dry_or_stuffy_nose);
        CheckBox answerR_btn = (CheckBox) v.findViewById(R.id.tingling_in_hands_feet);
        CheckBox answerS_btn = (CheckBox) v.findViewById(R.id.swollen_or_bloated_feeling);
        CheckBox answerT_btn = (CheckBox) v.findViewById(R.id.memory_or_concentration);
        CheckBox answerU_btn = (CheckBox) v.findViewById(R.id.sexual_problems);
        checkBoxList = new ArrayList<>();
        checkBoxList.add(answerA_btn);
        checkBoxList.add(answerB_btn);
        checkBoxList.add(answerC_btn);
        checkBoxList.add(answerD_btn);
        checkBoxList.add(answerE_btn);
        checkBoxList.add(answerF_btn);
        checkBoxList.add(answerG_btn);
        checkBoxList.add(answerH_btn);
        checkBoxList.add(answerI_btn);
        checkBoxList.add(answerJ_btn);
        checkBoxList.add(answerK_btn);
        checkBoxList.add(answerL_btn);
        checkBoxList.add(answerM_btn);
        checkBoxList.add(answerN_btn);
        checkBoxList.add(answerO_btn);
        checkBoxList.add(answerP_btn);
        checkBoxList.add(answerQ_btn);
        checkBoxList.add(answerR_btn);
        checkBoxList.add(answerS_btn);
        checkBoxList.add(answerT_btn);
        checkBoxList.add(answerU_btn);
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
        Log.i(CLASS_NAME, "onNext");
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
