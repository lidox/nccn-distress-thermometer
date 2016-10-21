package com.artursworld.nccn.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.wizard.WizardNCCN;

public class Launcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, WizardNCCN.class);
        startActivityForResult(intent, 1);
    }
}
