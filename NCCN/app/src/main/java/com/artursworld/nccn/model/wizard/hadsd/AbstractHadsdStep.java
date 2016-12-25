package com.artursworld.nccn.model.wizard.hadsd;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.util.Bits;
import com.artursworld.nccn.controller.util.Global;
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.controller.wizard.WizardHADSD;
import com.artursworld.nccn.model.entity.HADSDQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.HADSDQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;
import com.github.fcannizzaro.materialstepper.AbstractStep;

import java.util.Date;

public class AbstractHadsdStep extends AbstractStep {

    private static final String CLASS_NAME = AbstractHadsdStep.class.getSimpleName();
    // View
    private TextView questionLabel;
    private RadioButton answerA_btn;
    private RadioButton answerB_btn;
    private RadioButton answerC_btn;
    private RadioButton answerD_btn;

    private User selectedUser = null;
    private HADSDQuestionnaire questionnaire = null;
    private int currentQuestionNumber = 0;
    private RadioGroup answersGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = getView(inflater, container);
        /*
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                initBundledData();
                return null;
            }
        }.execute();
        */
        initBundledData();
        return v;
    }


    /**
     * Loads the UI elements and adds radio button change listener
     * @param inflater the inflater
     * @param container the container
     * @return the view loaded by xml file
     */
    @NonNull
    private View getView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.step_hadsd, container, false);
        questionLabel = (TextView) v.findViewById(R.id.question_label);
        answerA_btn = (RadioButton) v.findViewById(R.id.answer_a);
        answerB_btn = (RadioButton) v.findViewById(R.id.answer_b);
        answerC_btn = (RadioButton) v.findViewById(R.id.answer_c);
        answerD_btn = (RadioButton) v.findViewById(R.id.answer_d);
        answersGroup = (RadioGroup) v.findViewById(R.id.answer_radio_group);
        return v;
    }

    /**
     * Adds a change listener to the radio button group
     */
    private void addAnswerChangeListener() {
        answersGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.i("", "onCheckedChanged("+radioGroup.toString()+", "+i+")");
                onSelectedAnswerChanged(answersGroup);
            }
        });
    }

    /**
     * On selected radio button change, a new byte array is calculated and
     * update the questionnaire to its new byte array
     * @param answersGroup the radio group containing all radion buttons
     */
    private void onSelectedAnswerChanged(RadioGroup answersGroup) {
        RadioButton checkedRadioButton = (RadioButton) answersGroup.findViewById(answersGroup.getCheckedRadioButtonId());
        int index = answersGroup.indexOfChild(checkedRadioButton);
        Log.i(AbstractHadsdStep.class.getSimpleName(), "New box selected= '" +checkedRadioButton.getText() +"' with index = " + index + " and current questionNr = " +currentQuestionNumber);

        // load current answers again
        HADSDQuestionnaireManager m = new HADSDQuestionnaireManager();
        questionnaire = m.getHADSDQuestionnaireByDate_PK(selectedUser.getName(), Global.getSelectedQuestionnaireDate());

        // display old and new byte
        byte[] oldByte = questionnaire.getAnswerByNr(currentQuestionNumber);
        byte[] newByte = Bits.getNewByteByRadioBtn(checkedRadioButton.isChecked(), index, oldByte);
        String old = Bits.getStringByByte(oldByte);
        String newOne = Bits.getStringByByte(newByte);
        Log.i(AbstractHadsdStep.class.getSimpleName(), "Changed answer bits from: "+ old + " to " +newOne);

        // update new answer selections
        questionnaire.setAnswerByNr(currentQuestionNumber, newByte);
        m.update(questionnaire);
    }

    /**
     * Sets the UI texts and loads the database values to set UI
     */
    private void initBundledData() {
        Bundle bundle = getArguments();
        String[] questionData = bundle.getStringArray(WizardHADSD.QUESTION_DATA);
        if (questionData != null){
            String question = questionData[0];
            questionLabel.setText(question);
            answerA_btn.setText(questionData[1]);
            answerB_btn.setText(questionData[2]);
            answerC_btn.setText(questionData[3]);
            answerD_btn.setText(questionData[4]);
        }
        currentQuestionNumber = bundle.getInt(WizardHADSD.QUESTION_NUMBER) - 1;
        Log.i(CLASS_NAME, "loading data for question number = " + currentQuestionNumber + " ...");
        selectedUser = new UserManager().getUserByName(bundle.getString(WizardHADSD.SELECTED_USER));
        Date selectedQuestionnaireDate = Global.getSelectedQuestionnaireDate();
        questionnaire = new HADSDQuestionnaireManager().getHADSDQuestionnaireByDate_PK(selectedUser.getName(), selectedQuestionnaireDate);

        if(questionnaire == null)
            getActivity().finish();
        else {
            Log.i(CLASS_NAME, "user = " + selectedUser.getName() + ", questionnaire date = " + selectedQuestionnaireDate + " selected questionnaire = " + questionnaire);
            checkRadioButtonByBits();
            addAnswerChangeListener();
        }
    }

    /**
     * Sets the radio button as 'checked' by byte array coming from database
     */
    private void checkRadioButtonByBits() {
        byte[] answerByte = questionnaire.getAnswerByNr(currentQuestionNumber);
        Log.i(AbstractHadsdStep.class.getSimpleName(), "answer bits loaded: "+ Bits.getStringByByte(answerByte) + " for questionNr: " +(currentQuestionNumber+1) + "(index:"+currentQuestionNumber+")");
        StringBuilder bits = new StringBuilder(Bits.getStringByByte(answerByte)).reverse();
        int indexToCheck = bits.indexOf("1");
        RadioButton buttonToCheck = ((RadioButton)answersGroup.getChildAt(indexToCheck));
        if(buttonToCheck != null)
            buttonToCheck.setChecked(true);
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
    }

    @Override
    public void onNext() {
        System.out.println("onNext");
    }

    @Override
    public void onPrevious() {
        System.out.println("onPrevious");
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
