package com.artursworld.nccn.model.wizard.qualityoflife;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.util.Bits;
import com.artursworld.nccn.controller.util.Global;
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.controller.wizard.WizardNCCN;
import com.artursworld.nccn.controller.wizard.WizardQualityOfLife;
import com.artursworld.nccn.model.entity.QolQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.QualityOfLifeManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;
import com.github.fcannizzaro.materialstepper.AbstractStep;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

public class QualityOfLifeSpecialStep extends AbstractStep {

    private static String CLASS_NAME = QualityOfLifeSpecialStep.class.getSimpleName();

    // View
    private TextView questionLabel;
    private DiscreteSeekBar seekBar;

    private User selectedUser = null;
    private QolQuestionnaire questionnaire = null;
    private int currentQuestionNumber = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = getView(inflater, container);
        initBundledData();
        setValuesByDB();
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
        View v = inflater.inflate(R.layout.step_special_quality_of_life, container, false);
        questionLabel = (TextView) v.findViewById(R.id.question_label);
        seekBar = (DiscreteSeekBar) v.findViewById(R.id.seekbar);
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
        questionnaire = new QualityOfLifeManager().getQolQuestionnaireByDate(Global.getSelectedUser(), Global.getSelectedQuestionnaireDate());

        // display oldBits and new byte
        String oldBits = questionnaire.getBitsByQuestionNr(currentQuestionNumber);
        String newOne = Bits.getNewBinaryStringByIndex(newValueIndex, oldBits);
        Log.i(QualityOfLifeStep.class.getSimpleName(), "Changed answer bits from: "+ oldBits + " to " +newOne);

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
        if (questionData != null){
            String question = questionData[0];
            questionLabel.setText(question);
        }

        currentQuestionNumber = bundle.getInt(WizardQualityOfLife.QUESTION_NUMBER);
        selectedUser = new UserManager().getUserByName(bundle.getString(WizardQualityOfLife.SELECTED_USER));
        questionnaire = new QualityOfLifeManager().getQolQuestionnaireByDate(selectedUser.getName(), Global.getSelectedQuestionnaireDate());
        addAnswerChangeListener();
    }


    /**
     * Adds a change listener to the radio button group
     */
    private void addAnswerChangeListener() {
        this.seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                Log.i("", "onProgressChanged("+value+")");
                if(fromUser)
                    onSelectedAnswerChanged(value);
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
                Log.i("", "onStartTrackingTouch");
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                Log.i("", "onStopTrackingTouch");
            }
        });
    }

    /**
     * Sets the radio button as 'checked' by byte array coming from database
     */
    private void setValuesByDB() {
        byte[] answerByte = Bits.getByteByString(questionnaire.getBitsByQuestionNr(currentQuestionNumber));
        Log.i(QualityOfLifeStep.class.getSimpleName(), "answer bits loaded: "+ Bits.getStringByByte(answerByte) + " for questionNr:" +(currentQuestionNumber));
        StringBuilder bits = new StringBuilder(Bits.getStringByByte(answerByte)).reverse();
        int indexToCheck = bits.indexOf("1");

        if(seekBar!= null)
            seekBar.setProgress(indexToCheck);
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
        WizardQualityOfLife.updateProgress(questionnaire, currentQuestionNumber);
        Log.i(CLASS_NAME, "onNext with questionNr. " + currentQuestionNumber );
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

