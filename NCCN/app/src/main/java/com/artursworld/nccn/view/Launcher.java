package com.artursworld.nccn.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.wizard.WizardHADSD;
import com.artursworld.nccn.controller.wizard.WizardNCCN;
import com.artursworld.nccn.controller.wizard.WizardQualityOfLife;
import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;
import com.artursworld.nccn.model.entity.HADSDQuestionnaire;
import com.artursworld.nccn.model.entity.QolQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.DistressThermometerQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.HADSDQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.QualityOfLifeManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Launcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // TODO: add good user management
        UserManager m = new UserManager();
        String userName = "Ahmet";
        m.insertUser(new User(userName));
        DistressThermometerQuestionnaireManager qm = new DistressThermometerQuestionnaireManager();
        qm.insertQuestionnaire(new DistressThermometerQuestionnaire(userName));
        QualityOfLifeManager qm1 = new QualityOfLifeManager();
        qm1.insertQuestionnaire(new QolQuestionnaire(userName));
        HADSDQuestionnaireManager qm2 = new HADSDQuestionnaireManager();
        qm2.insertQuestionnaire(new HADSDQuestionnaire(userName));
    }

    @OnClick(R.id.btn_distress_thermometer) void startDistressThermometerQuestionnaire() {
        startActivityForResult(new Intent(this, WizardNCCN.class), 1);
    }

    @OnClick(R.id.btn_HADS_D) void startHADS_D_Questionnaire() {
        startActivityForResult(new Intent(this, WizardHADSD.class), 2);
    }

    @OnClick(R.id.btn_quality_of_life) void startQOLQuestionnaire() {
        startActivityForResult(new Intent(this, WizardQualityOfLife.class), 3);
    }
}
