package com.artursworld.nccn.model.wizard.distressthermometer;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.config.App;
import com.artursworld.nccn.controller.util.Bits;
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.model.entity.QolQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.QualityOfLifeManager;
import com.artursworld.nccn.model.wizard.qualityoflife.QualityOfLifeStep;
import com.github.fcannizzaro.materialstepper.AbstractStep;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

public class ThermometerStep extends AbstractStep {

    private String CLASS_NAME = ThermometerStep.class.getSimpleName();
    private DiscreteSeekBar thermometer = null;

    private User selectedUser = null;
    private QolQuestionnaire questionnaire = null;
    private int currentQuestionNumber = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.thermometer_step, container, false);
        thermometer = (DiscreteSeekBar) v.findViewById(R.id.seekbar);
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
        return v;
    }
    /**
     * On selected seek bar change, a new byte array is calculated and
     * update the questionnaire to its new byte array
     * @param newValueIndex the new selected index of the seek bar
     */
    private void onSelectedAnswerChanged(int newValueIndex) {
        Log.i("","New selected answer at index= '" + newValueIndex + " and current questionNr = " +currentQuestionNumber);

        // load current answers again because maybe there are already updates
        QualityOfLifeManager m = new QualityOfLifeManager();
        questionnaire = new QualityOfLifeManager().getQolQuestionnaireByUserName(selectedUser.getName());

        // display oldBits and new byte
        String oldBits = questionnaire.getBitsByQuestionNr(currentQuestionNumber);
        String newOne = Bits.getNewBinaryStringByIndex(newValueIndex, oldBits);
        Log.i(QualityOfLifeStep.class.getSimpleName(), "Changed answer bits from: "+ oldBits + " to " +newOne);

        // update new answer selections
        questionnaire.setBitsByQuestionNr(currentQuestionNumber, newOne);
        m.update(questionnaire);
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
