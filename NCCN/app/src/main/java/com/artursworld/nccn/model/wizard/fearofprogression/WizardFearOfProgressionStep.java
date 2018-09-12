package com.artursworld.nccn.model.wizard.fearofprogression;

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
import com.artursworld.nccn.controller.util.Global;
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.controller.wizard.WizardFearOfProgression;
import com.artursworld.nccn.controller.wizard.WizardQualityOfLife;
import com.artursworld.nccn.model.entity.FearOfProgressionQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.FearOfProgressionManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;
import com.github.fcannizzaro.materialstepper.AbstractStep;

public class WizardFearOfProgressionStep extends AbstractStep {

    private static String CLASS_NAME = WizardFearOfProgressionStep.class.getSimpleName();
    private static String BUNDLE_DATA = WizardFearOfProgression.QUESTION_DATA;
    private static String BUNDLE_NUMBER = WizardFearOfProgression.QUESTION_NUMBER;
    private static String BUNDLE_USER = WizardQualityOfLife.SELECTED_USER;
    private static int LAYOUT = R.layout.step_fear_of_progression;

    // View
    private TextView questionLabel;
    private RadioButton answerA_btn;
    private RadioButton answerB_btn;
    private RadioButton answerC_btn;
    private RadioButton answerD_btn;
    private RadioButton answerE_btn;

    private FearOfProgressionQuestionnaire questionnaire = null;
    private User selectedUser = null;
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
        View v = inflater.inflate(LAYOUT, container, false);
        questionLabel = (TextView) v.findViewById(R.id.question_label);
        answerA_btn = (RadioButton) v.findViewById(R.id.answer_a);
        answerB_btn = (RadioButton) v.findViewById(R.id.answer_b);
        answerC_btn = (RadioButton) v.findViewById(R.id.answer_c);
        answerD_btn = (RadioButton) v.findViewById(R.id.answer_d);
        answerE_btn = (RadioButton) v.findViewById(R.id.answer_e);
        answersGroup = (RadioGroup) v.findViewById(R.id.answer_radio_group);
        return v;
    }

    /**
     * Sets the UI texts and loads the database values to set UI
     */
    private void initBundledData() {
        Bundle bundle = getArguments();
        String[] questionData = bundle.getStringArray(BUNDLE_DATA);
        if (questionData != null) {
            String question = questionData[0];
            questionLabel.setText(question);
            answerA_btn.setText(questionData[1]);
            answerB_btn.setText(questionData[2]);
            answerC_btn.setText(questionData[3]);
            answerD_btn.setText(questionData[4]);
            answerE_btn.setText(questionData[5]);
        }

        currentQuestionNumber = bundle.getInt(BUNDLE_NUMBER);
        selectedUser = new UserManager().getUserByName(bundle.getString(BUNDLE_USER));
        questionnaire = new FearOfProgressionManager().getQuestionnaireByDate(Global.getSelectedUser(), Global.getSelectedQuestionnaireDate());

        if (questionnaire != null) {
            checkRadioButtonByBits();
            addAnswerChangeListener();
        }
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
     * On selected radio button change, a new index is set and updates the questionnaire
     *
     * @param answersGroup the radio group containing all radion buttons
     */
    private void onSelectedAnswerChanged(RadioGroup answersGroup) {
        questionnaire = new FearOfProgressionManager().getQuestionnaireByDate(Global.getSelectedUser(), Global.getSelectedQuestionnaireDate());
        if (questionnaire != null) {

            RadioButton checkedRadioButton = (RadioButton) answersGroup.findViewById(answersGroup.getCheckedRadioButtonId());
            int newIndex = answersGroup.indexOfChild(checkedRadioButton) + 1;

            Log.i(CLASS_NAME, "New box selected= '" + checkedRadioButton.getText() + "' with index = " + newIndex + " and current questionNr = " + currentQuestionNumber);
            Log.i(CLASS_NAME, "Changed answer bits from: " + questionnaire.getSelectedAnswerIndexByQuestionNr(currentQuestionNumber) + " to " + newIndex);

            // update new answer selections
            questionnaire.setAnswerIndexByQuestionNr(currentQuestionNumber, newIndex);
            new FearOfProgressionManager().update(questionnaire);
        }
    }

    /**
     * Sets the radio button as 'checked'
     */
    private void checkRadioButtonByBits() {
        double questionNr = currentQuestionNumber;
        int numberOfQuestions = (int) FearOfProgressionQuestionnaire.getQuestionCount();
        int progressValue = (int) Math.floor(questionNr / numberOfQuestions * 100);

        if (questionnaire != null) {

            if (questionnaire.getProgressInPercent() >= progressValue || questionnaire.getProgressInPercent() == 100) {

                int indexToCheck = questionnaire.getSelectedAnswerIndexByQuestionNr(currentQuestionNumber) - 1;
                setRadioBoxCheckedByIndex(indexToCheck, true);

            } else {
                setRadioBoxCheckedByIndex(5, true);
            }
        }
    }

    /**
     * Sets a specified radio button as checked / unchecked
     *
     * @param indexToCheck the index of the radio button
     * @param isChecked    check or uncheck radio button
     */
    private void setRadioBoxCheckedByIndex(int indexToCheck, boolean isChecked) {
        RadioButton buttonToCheck = ((RadioButton) answersGroup.getChildAt(indexToCheck));
        if (buttonToCheck != null)
            buttonToCheck.setChecked(isChecked);
    }

    @Override
    public String name() {
        return "Tab " + getArguments().getInt("position", 0);
    }

    @Override
    public void onNext() {
        questionnaire = new FearOfProgressionManager().getQuestionnaireByDate(Global.getSelectedUser(), Global.getSelectedQuestionnaireDate());
        WizardFearOfProgression.updateProgress(questionnaire, currentQuestionNumber);
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
        if (answersGroup != null) {
            RadioButton buttonToCheck = ((RadioButton) answersGroup.getChildAt(5));
            return !buttonToCheck.isChecked();
        }
        return false;
    }
}
