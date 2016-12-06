package com.artursworld.nccn.view.user;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.config.App;

public class UserStartConfiguration {

    public static String CLASS_NAME = UserStartConfiguration.class.getSimpleName();

    // Attributes
    private Context cxt = null;

    public UserStartConfiguration(Context context){
        cxt = context;
    }

    public void showConfigurationDialog(){
        new MaterialDialog.Builder(cxt)
                .title(R.string.questionnaire_to_display)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .customView(R.layout.dialog_select_questionnairies, true)
                .onPositive(new MaterialDialog.SingleButtonCallback() {

                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Log.i(CLASS_NAME, "ok");
                    }
                })
                .show();
    }
}
