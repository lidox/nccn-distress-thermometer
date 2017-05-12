package com.artursworld.nccn.model.wizard.qualityoflife;

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
import com.artursworld.nccn.controller.wizard.WizardQualityOfLife;
import com.artursworld.nccn.model.entity.QolQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.QualityOfLifeManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;
import com.github.fcannizzaro.materialstepper.AbstractStep;

public class QualityOfLifeStep extends AbstractStep {

    private static String CLASS_NAME = QualityOfLifeStep.class.getSimpleName();

    // View
    private TextView questionLabel;
    private RadioButton answerA_btn;
    private RadioButton answerB_btn;
    private RadioButton answerC_btn;
    private RadioButton answerD_btn;

    private User selectedUser = null;
    private QolQuestionnaire questionnaire = null;
    private int currentQuestionNumber = 0;
    private RadioGroup answersGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = getView(inflater, container);
        initBundledData();
        return v;
    }


    /**
     * Loads the UI elements and adds radio button change listener
     *
     * @param inflater  the inflater
     * @param container the container
     * @return the view loaded by xml file
     */
    @NonNull
    private View getView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.step_quality_of_life, container, false);
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
                Log.i("", "onCheckedChanged(" + radioGroup.toString() + ", " + i + ")");
                onSelectedAnswerChanged(answersGroup);
            }
        });
    }

    /**
     * On selected radio button change, a new byte array is calculated and
     * update the questionnaire to its new byte array
     *
     * @param answersGroup the radio group containing all radion buttons
     */
    private void onSelectedAnswerChanged(RadioGroup answersGroup) {
        questionnaire = new QualityOfLifeManager().getQolQuestionnaireByDate(Global.getSelectedUser(), Global.getSelectedQuestionnaireDate());
        RadioButton checkedRadioButton = (RadioButton) answersGroup.findViewById(answersGroup.getCheckedRadioButtonId());
        int index = answersGroup.indexOfChild(checkedRadioButton);
        Log.i(QualityOfLifeStep.class.getSimpleName(), "New box selected= '" + checkedRadioButton.getText() + "' with index = " + index + " and current questionNr = " + currentQuestionNumber);

        // load current answers again because maybe there are already updates
        QualityOfLifeManager m = new QualityOfLifeManager();
        //questionnaire = new QualityOfLifeManager().getQolQuestionnaireByDate(Global.getSelectedStatisticUser(), Global.getSelectedQuestionnaireDate());


        // display old and new byte
        String old = questionnaire.getBitsByQuestionNr(currentQuestionNumber);
        String newOne = Bits.getNewBinaryStringByRadioBtn(checkedRadioButton.isChecked(), index, old, true);
        Log.i(QualityOfLifeStep.class.getSimpleName(), "Changed answer bits from: " + old + " to " + newOne);

        // update new answer selections
        questionnaire.setBitsByQuestionNr(currentQuestionNumber, newOne);
        m.update(questionnaire);
    }

    /**
     * Sets the UI texts and loads the database values to set UI
     */
    private void initBundledData() {
        Bundle bundle = getArguments();
        String[] questionData = bundle.getStringArray(WizardQualityOfLife.QUESTION_DATA);
        if (questionData != null) {
            String question = questionData[0];
            questionLabel.setText(question);
            answerA_btn.setText(questionData[1]);
            answerB_btn.setText(questionData[2]);
            answerC_btn.setText(questionData[3]);
            answerD_btn.setText(questionData[4]);
        }

        currentQuestionNumber = bundle.getInt(WizardQualityOfLife.QUESTION_NUMBER);
        selectedUser = new UserManager().getUserByName(bundle.getString(WizardQualityOfLife.SELECTED_USER));
        questionnaire = new QualityOfLifeManager().getQolQuestionnaireByDate(Global.getSelectedUser(), Global.getSelectedQuestionnaireDate());
        if(questionnaire != null){
            checkRadioButtonByBits();
            addAnswerChangeListener();
        }
    }

    /**
     * Sets the radio button as 'checked' by byte array coming from database
     */
    private void checkRadioButtonByBits() {
        double questionNr = currentQuestionNumber + 1;
        int progressValue = (int) Math.floor(questionNr / 50 * 100);
        if (questionnaire.getProgressInPercent() >= progressValue) {
            byte[] answerByte = Bits.getByteByString(questionnaire.getBitsByQuestionNr(currentQuestionNumber));
            Log.i(QualityOfLifeStep.class.getSimpleName(), "answer bits loaded: " + Bits.getStringByByte(answerByte) + " for questionNr:" + (currentQuestionNumber) + "(index:" + currentQuestionNumber + ")");
            StringBuilder bits = new StringBuilder(Bits.getStringByByte(answerByte)).reverse();
            int indexToCheck = bits.indexOf("1");
            setRadioBoxCheckedByIndex(indexToCheck, true);
        }
        else{
            setRadioBoxCheckedByIndex(4, true);
        }
    }

    /**
     * Sets a specified radio button as checked / unchecked
     * @param indexToCheck the index of the radio button
     * @param isChecked check or uncheck radio button
     */
    private void setRadioBoxCheckedByIndex(int indexToCheck, boolean isChecked) {
        RadioButton buttonToCheck = ((RadioButton)answersGroup.getChildAt(indexToCheck));
        if(buttonToCheck != null)
            buttonToCheck.setChecked(isChecked);
    }

    @Override
    public String name() {
        return "Tab " + getArguments().getInt("position", 0);
    }

    @Override
    public void onNext() {
        questionnaire = new QualityOfLifeManager().getQolQuestionnaireByDate(Global.getSelectedUser(), Global.getSelectedQuestionnaireDate());
        WizardQualityOfLife.updateProgress(questionnaire, currentQuestionNumber);
        Log.i(CLASS_NAME, "onNext with questionNr. " + currentQuestionNumber);
    }

    @Override
    public boolean nextIf() {
        Log.i(CLASS_NAME, "nextIf");
        return isAnAnswerSelected();
    }

    @Override
    public String error() {
        return Strings.getStringByRId(R.string.please_select_answer);
    }

    private boolean isAnAnswerSelected() {
        if(answersGroup != null) {
            RadioButton buttonToCheck = ((RadioButton) answersGroup.getChildAt(4));
            return !buttonToCheck.isChecked();
        }
        return false;
    }
}
