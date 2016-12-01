package com.artursworld.nccn.model.wizard.distressthermometer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.util.Bits;
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.controller.wizard.WizardQualityOfLife;
import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.DistressThermometerQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;
import com.github.fcannizzaro.materialstepper.AbstractStep;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

public class ThermometerStep extends AbstractStep {

    private String CLASS_NAME = ThermometerStep.class.getSimpleName();
    private DiscreteSeekBar thermometer = null;

    private User selectedUser = null;
    private DistressThermometerQuestionnaire questionnaire = null;
    private int currentQuestionNumber = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = getView(inflater, container);
        initBundledData();
        setUIValuesByDB();
        return v;
    }

    private void setUIValuesByDB() {
        String binaryStringByQuestionNr = questionnaire.getBitsByQuestionNr(currentQuestionNumber);
        byte[] answerByte = Bits.getByteByString(binaryStringByQuestionNr);
        Log.i(CLASS_NAME, "answer bits loaded: "+ Bits.getStringByByte(answerByte) + " for questionNr:" +(currentQuestionNumber));
        StringBuilder bits = new StringBuilder(binaryStringByQuestionNr).reverse();
        int indexToCheck = bits.indexOf("1");

        if(thermometer!= null){
            Log.i(CLASS_NAME, "New thermometer index =" + indexToCheck);
            thermometer.setProgress(indexToCheck);
        }
    }

    private void initBundledData() {
        Bundle bundle = getArguments();
        selectedUser = new UserManager().getUserByName(bundle.getString(WizardQualityOfLife.SELECTED_USER));
        questionnaire = new DistressThermometerQuestionnaireManager().getDistressThermometerQuestionnaireByUserName(selectedUser.getName());
    }

    @NonNull
    private View getView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.thermometer_step, container, false);
        thermometer = (DiscreteSeekBar) v.findViewById(R.id.seekbar);
        addThermometerChangeListener();
        return v;
    }

    private void addThermometerChangeListener() {
        thermometer.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                Log.i(CLASS_NAME, "onProgressChanged");
                if(fromUser)
                    onSelectedAnswerChanged(value);
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
                Log.i(CLASS_NAME, "onStartTrackingTouch");
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                Log.i(CLASS_NAME, "onStopTrackingTouch");
            }
        });
    }

    /**
     * On selected seek bar change, a new byte array is calculated and
     * update the questionnaire to its new byte array
     * @param newValueIndex the new selected index of the seek bar
     */
    private void onSelectedAnswerChanged(int newValueIndex) {
        Log.i(CLASS_NAME,"New selected answer at index= '" + newValueIndex + " and current questionNr = " +currentQuestionNumber);

        // load current answers again because maybe there are already updates
        final DistressThermometerQuestionnaireManager m = new DistressThermometerQuestionnaireManager();
        questionnaire = m.getDistressThermometerQuestionnaireByUserName(selectedUser.getName());

        // display oldBits and new byte
        String oldBits = questionnaire.getBitsByQuestionNr(currentQuestionNumber);
        String newOne = Bits.getNewBinaryStringByIndex(newValueIndex, oldBits);
        Log.i(CLASS_NAME, "Changed answer bits from: "+ oldBits + " to " +newOne);

        // update new answer selections
        questionnaire.setBitsByQuestionNr(currentQuestionNumber, newOne);
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                m.update(questionnaire);
                return null;
            }
        }.execute();

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
