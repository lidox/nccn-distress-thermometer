package com.artursworld.nccn.controller.wizard;

import android.os.Bundle;
import android.util.Log;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.model.entity.HADSDQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.HADSDQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;
import com.artursworld.nccn.model.wizard.hadsd.AbstractHadsdStep;
import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.TextStepper;



public class WizardHADSD extends TextStepper {

    public static final String QUESTION_DATA = "WizardHADSD-question-data";
    public static final String QUESTION_NUMBER = "WizardHADSD-QUESTION_NUMBER";
    public static final String SELECTED_USER = "WizardHADSD-SELECTED_USER";

    private int currentWizardPosition = 1;
    private User selectedUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        selectedUser = new UserManager().getAllUsers().get(0); // TODO: changes this to real selected user later

        // configuration
        setErrorTimeout(1000);
        setTitle(Strings.getStringByRId(R.string.app_name));

        // question 1
        addStepByTextIds(R.string.question1_tense,
                R.string.q1_1_mostly,
                R.string.q1_2_often,
                R.string.q1_3_occasionally,
                R.string.q1_4_not_at_all);

        // question 2
        addStepByTextIds(R.string.question2_foreboding,
                R.string.q2_1_very_intensive,
                R.string.q2_2_yes_intensive,
                R.string.q2_3_a_bit,
                R.string.q1_4_not_at_all);

        // question 3
        addStepByTextIds(R.string.question3_thougths,
                R.string.big_part,
                R.string.relativly_often,
                R.string.q2_3_a_bit,
                R.string.sometime_never);

        // question 4
        addStepByTextIds(R.string.question4_relax,
                R.string.yes_of_course,
                R.string.usually_yes,
                R.string.not_often,
                R.string.q1_4_not_at_all);

        // question 5
        addStepByTextIds(R.string.question5_anxious_feeling,
                R.string.q1_4_not_at_all,
                R.string.occasionally,
                R.string.quite_often,
                R.string.very_often);

        // question 6
        addStepByTextIds(R.string.question6_restless,
                R.string.yes_indeed,
                R.string.quite,
                R.string.not_much,
                R.string.not_at_all);

        // question 7
        addStepByTextIds(R.string.question7_panic,
                R.string.yes_indeed,
                R.string.quite_often,
                R.string.not_very_often,
                R.string.not_at_all);

        // question 8
        addStepByTextIds(R.string.question8_pleasure,
                R.string.exactly_like_that,
                R.string.not_so_much,
                R.string.only_very_little,
                R.string.hardly_or_not_at_all);

        // question 9
        addStepByTextIds(R.string.question9_can_laugh,
                R.string.yes_as_always,
                R.string.not_quite_that_much,
                R.string.now_much_less,
                R.string.not_at_all);

        // question 10
        addStepByTextIds(R.string.question10_happy,
                R.string.not_at_all,
                R.string.rarely,
                R.string.sometimes,
                R.string.q1_1_mostly);

        // question 11
        addStepByTextIds(R.string.question11_braked,
                R.string.almost_always,
                R.string.very_often,
                R.string.sometimes,
                R.string.not_at_all);

        // question 12
        addStepByTextIds(R.string.question12_lost_interest,
                R.string.yes_exactly,
                R.string.i_dont_care_as_much_as_i_should,
                R.string.maybe_i_care_too_little_about_it,
                R.string.i_care_as_much_about_it_as_always);

        // question 13
        addStepByTextIds(R.string.question13_joy_in_future,
                R.string.yes_very_much,
                R.string.rather_less_than_before,
                R.string.much_less_than_before,
                R.string.hardly_to_not_at_all);

        // question 14
        addStepByTextIds(R.string.question14_i_can_enjoy_a_good_book_radio_tv,
                R.string.often,
                R.string.sometimes,
                R.string.rather_rare,
                R.string.very_rare);


        super.onCreate(savedInstanceState);
    }

    private void addStepByTextIds(int questionId, int answerAId, int answerBId ,int answerCId, int answerDId) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(QUESTION_DATA, new String[]{
                Strings.getStringByRId(questionId),
                Strings.getStringByRId(answerAId),
                Strings.getStringByRId(answerBId),
                Strings.getStringByRId(answerCId),
                Strings.getStringByRId(answerDId)});
        //bundle.putByteArray(ANSWER_DATA, answerBytes);
        addStep(createFragment(new AbstractHadsdStep(), bundle));
    }

    private AbstractStep createFragment(AbstractStep fragment, Bundle b) {
        b.putInt(QUESTION_NUMBER, currentWizardPosition ++);
        b.putString(SELECTED_USER, selectedUser.getName());
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onComplete() {
        super.onComplete();
        Log.i(WizardHADSD.class.getSimpleName(), "completed WizardHADSD");
    }

}
