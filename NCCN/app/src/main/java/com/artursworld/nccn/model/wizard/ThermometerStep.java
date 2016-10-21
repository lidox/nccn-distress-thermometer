package com.artursworld.nccn.model.wizard;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.artursworld.nccn.R;
import com.github.fcannizzaro.materialstepper.AbstractStep;

public class ThermometerStep extends AbstractStep {

    private int i = 1;
    //private Button button;
    private final static String CLICK = "click";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.thermometer_step, container, false);
        //button = (Button) v.findViewById(R.id.button);

        if (savedInstanceState != null)
            i = savedInstanceState.getInt(CLICK, 0);

       // button.setText(Html.fromHtml("Tap <b>" + i + "</b>"));

       /* button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Button) view).setText(Html.fromHtml("Tap <b>" + (++i) + "</b>"));
                mStepper.getExtras().putInt(CLICK, i);
            }
        });
        */

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putInt(CLICK, i);
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
        return "You can skip";
    }

    @Override
    public boolean nextIf() {
        return i > 1;
    }

    @Override
    public String error() {
        return "<b>You must click!</b> <small>this is the condition!</small>";
    }
}
