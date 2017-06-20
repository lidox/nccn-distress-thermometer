package com.artursworld.nccn.model.wizard.psychosocial;

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
        import com.artursworld.nccn.model.entity.MetaQuestionnaire;
        import com.artursworld.nccn.model.entity.PsychoSocialSupportState;
        import com.artursworld.nccn.model.entity.User;
        import com.artursworld.nccn.model.persistence.manager.MetaQuestionnaireManager;
        import com.artursworld.nccn.model.persistence.manager.UserManager;
        import com.github.fcannizzaro.materialstepper.AbstractStep;
        import com.rackspira.kristiawan.rackmonthpicker.RackMonthPicker;
        import com.rackspira.kristiawan.rackmonthpicker.listener.DateMonthDialogListener;

public class PastPsychoSocialStep extends AbstractStep {

    private String CLASS_NAME = PastPsychoSocialStep.class.getSimpleName();

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
        if (checkedRadioBoxIndex == 0) {
            openMonthClicker();
        } else if (checkedRadioBoxIndex == 1) {
            String hadPsychoSocialSupportState = "did not have support";
            updatePastPsychoSocialSupportState(hadPsychoSocialSupportState);
        }
    }

    private int getCheckedIndex(RadioGroup answersGroup) {
        RadioButton checkedRadioButton = (RadioButton) answersGroup.findViewById(answersGroup.getCheckedRadioButtonId());
        int index = answersGroup.indexOfChild(checkedRadioButton);
        Log.i(CLASS_NAME, "New box selected= '" + checkedRadioButton.getText() + "' with index = " + index);
        return index;
    }


    /**
     * Sets the UI texts and loads the database values to set UI
     */
    private void initBundledData() {
        String question = Strings.getStringByRId(R.string.had_already_professional_psychosocial_support);
        questionLabel.setText(question);
        answerA_btn.setText(Strings.getStringByRId(R.string.yes));
        answerB_btn.setText(Strings.getStringByRId(R.string.no));

        // set Visible false to these UI elements, because they are not needed
        answerC_btn.setVisibility(View.INVISIBLE);
        answerD_btn.setVisibility(View.INVISIBLE);

        selectedUser = new UserManager().getUserByName(Global.getSelectedStatisticUser());

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
        return "Past Psycho Social Question";
    }

    //TODO:

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

    private void openMonthClicker() {
        Log.i(CLASS_NAME, "yes he had support");
        RackMonthPicker monthPicker = new RackMonthPicker(getActivity())
                .setPositiveButton(new DateMonthDialogListener() {
                    @Override
                    public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                        String pastPsychoSocialSupportState = "had support on " + monthLabel;
                        Log.i(CLASS_NAME, pastPsychoSocialSupportState);
                        updatePastPsychoSocialSupportState(pastPsychoSocialSupportState);
                    }
                })
                .setNegativeText(Strings.getStringByRId(R.string.cancel));
        monthPicker.show();
    }

    private void updatePastPsychoSocialSupportState(String hadPsychoSocialSupportState) {
        MetaQuestionnaireManager db = new MetaQuestionnaireManager();
        MetaQuestionnaire meta = db.getMetaDataByCreationDate(Global.getSelectedQuestionnaireDate());
        meta.setPastPsychoSocialSupportState(hadPsychoSocialSupportState);
        if (meta != null)
            db.update(meta);
    }


}

