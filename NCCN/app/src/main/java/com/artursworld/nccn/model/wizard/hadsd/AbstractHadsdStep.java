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
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.controller.wizard.WizardHADSD;
import com.github.fcannizzaro.materialstepper.AbstractStep;

public class AbstractHadsdStep extends AbstractStep {

    private int index = 0;
    private String question;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;

    private final static String CLICK = Strings.getStringByRId(R.string.click);

    // View
    private TextView questionLabel;
    private RadioButton answerA_btn;
    private RadioButton answerB_btn;
    private RadioButton answerC_btn;
    private RadioButton answerD_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = getView(inflater, container);
        initBundledData();

        if (savedInstanceState != null)
            index = savedInstanceState.getInt(CLICK, 0);

        setUIElements();

        return v;
    }

    /**
     * Set the question and answer strings
     */
    private void setUIElements() {
        questionLabel.setText(question);
        answerA_btn.setText(answerA);
        answerB_btn.setText(answerB);
        answerC_btn.setText(answerC);
        answerD_btn.setText(answerD);
    }

    @NonNull
    private View getView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.step_hadsd, container, false);
        questionLabel = (TextView) v.findViewById(R.id.question_label);
        answerA_btn = (RadioButton) v.findViewById(R.id.answer_a);
        answerB_btn = (RadioButton) v.findViewById(R.id.answer_b);
        answerC_btn = (RadioButton) v.findViewById(R.id.answer_c);
        answerD_btn = (RadioButton) v.findViewById(R.id.answer_d);
        initAnswerChangeListener(v);
        return v;
    }

    private void initAnswerChangeListener(View v) {
        final RadioGroup answersGroup = (RadioGroup) v.findViewById(R.id.answer_radio_group);
        answersGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                onSelectedAnswerChanged(answersGroup);
            }
        });
    }

    private void onSelectedAnswerChanged(RadioGroup answersGroup) {
        RadioButton checkedRadioButton = (RadioButton) answersGroup.findViewById(answersGroup.getCheckedRadioButtonId());
        int index = answersGroup.indexOfChild(checkedRadioButton);
        Log.i(AbstractHadsdStep.class.getSimpleName(), "selected = " +checkedRadioButton.getText() +" with index:" + index);


    }

    private void initBundledData() {
        Bundle bundle = getArguments();
        String[] questionData = bundle.getStringArray(WizardHADSD.QUESTION_DATA);
        question = questionData[0];
        answerA = questionData[1];
        answerB = questionData[2];
        answerC = questionData[3];
        answerD = questionData[4];
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putInt(CLICK, index);
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
    public boolean nextIf() {
        return index > 1;
    }

    @Override
    public String error() {
        return "<b>You must click!</b> <small>this is the condition!</small>";
    }
}
