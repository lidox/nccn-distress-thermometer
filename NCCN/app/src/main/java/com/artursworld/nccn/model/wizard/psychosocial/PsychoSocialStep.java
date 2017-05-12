package com.artursworld.nccn.model.wizard.psychosocial;


import android.os.AsyncTask;
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
import com.artursworld.nccn.model.entity.MetaQuestionnaire;
import com.artursworld.nccn.model.entity.PsychoSocialSupportState;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.HADSDQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.MetaQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;
import com.artursworld.nccn.model.wizard.hadsd.AbstractHadsdStep;
import com.github.fcannizzaro.materialstepper.AbstractStep;

import java.util.Date;

public class PsychoSocialStep extends AbstractStep {

    private String CLASS_NAME = PsychoSocialStep.class.getSimpleName();

    // configuration
    private User selectedUser = null;

    // View
    private TextView questionLabel;
    private RadioButton answerA_btn;
    private RadioButton answerB_btn;
    private RadioButton answerC_btn;
    private RadioButton answerD_btn;
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
                Log.i("", "onCheckedChanged(" + radioGroup.toString() + ", " + i + ")");
                onSelectedAnswerChanged(answersGroup);
            }
        });
    }

    /**
     * On selected radio button change, a new psychosocial supprt state will be set
     *
     * @param answersGroup the radio group containing all radion buttons
     */
    private void onSelectedAnswerChanged(RadioGroup answersGroup) {
        int checkedRadioBoxIndex = getCheckedIndex(answersGroup);
        PsychoSocialSupportState supportState = getPsychoSocialSupportStateByCheckedIndex(checkedRadioBoxIndex);
        updatePsychoSocialSupportState(supportState);
    }

    private int getCheckedIndex(RadioGroup answersGroup) {
        RadioButton checkedRadioButton = (RadioButton) answersGroup.findViewById(answersGroup.getCheckedRadioButtonId());
        int index = answersGroup.indexOfChild(checkedRadioButton);
        Log.i(CLASS_NAME, "New box selected= '" + checkedRadioButton.getText() + "' with index = " + index);
        return index;
    }

    private void updatePsychoSocialSupportState(PsychoSocialSupportState supportState) {
        MetaQuestionnaireManager db = new MetaQuestionnaireManager();
        MetaQuestionnaire meta = db.getMetaDataByCreationDate(Global.getSelectedQuestionnaireDate());
        meta.setPsychoSocialSupportState(supportState);
        if (meta != null)
            db.update(meta);
    }

    /**
     * Get the psychosocial support state by radio box index
     *
     * @param index the checked index
     * @return the support state
     */
    @NonNull
    private PsychoSocialSupportState getPsychoSocialSupportStateByCheckedIndex(int index) {
        PsychoSocialSupportState supportState = PsychoSocialSupportState.NOT_ASKED;
        if (index == 0) {
            supportState = PsychoSocialSupportState.ACCEPTED;
        } else if (index == 1) {
            supportState = PsychoSocialSupportState.REJECTED;
        }
        return supportState;
    }

    /**
     * Sets the UI texts and loads the database values to set UI
     */
    private void initBundledData() {
        String question = Strings.getStringByRId(R.string.wish_professional_psychosocial_support);
        questionLabel.setText(question);
        answerA_btn.setText(Strings.getStringByRId(R.string.yes));
        answerB_btn.setText(Strings.getStringByRId(R.string.no));
        answerC_btn.setVisibility(View.INVISIBLE);
        answerD_btn.setVisibility(View.INVISIBLE);

        selectedUser = new UserManager().getUserByName(Global.getSelectedStatisticUser());
        Date selectedQuestionnaireDate = Global.getSelectedQuestionnaireDate();

        MetaQuestionnaireManager db = new MetaQuestionnaireManager();
        MetaQuestionnaire meta = db.getMetaDataByCreationDate(Global.getSelectedQuestionnaireDate());
        if (meta == null)
            getActivity().finish();
        else {
            Log.i(CLASS_NAME, " selected meta = " + meta);
            setRadioButtonByBits(meta);
            addAnswerChangeListener();
        }
    }

    /**
     * Sets the radio button as 'checked' by byte array coming from database
     */
    private void setRadioButtonByBits(MetaQuestionnaire meta) {

        if (meta.getPsychoSocialSupportState() == PsychoSocialSupportState.ACCEPTED) {
            setRadioBoxCheckedByIndex(0, true);
        } else if (meta.getPsychoSocialSupportState() == PsychoSocialSupportState.REJECTED) {
            setRadioBoxCheckedByIndex(1, true);
        } else {
            setRadioBoxCheckedByIndex(4, true);
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
        return "Psycho Social Question";
    }

}
