package com.artursworld.nccn.view.user;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.util.Strings;
import com.fenjuly.mylibrary.ToggleExpandLayout;

public class UserStartConfiguration {

    public static String CLASS_NAME = UserStartConfiguration.class.getSimpleName();

    // Attributes
    private MaterialDialog dialog;
    private Context cxt = null;

    public UserStartConfiguration(Context context){
        cxt = context;
    }

    public void showConfigurationDialog(){
        dialog = new MaterialDialog.Builder(cxt)
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
        addToggleBtnListener();
    }

    private void addToggleBtnListener() {
        //toggleById(R.id.toggle_user_layout, R.string.configurable_questionnaire, R.string.switch_to_default_selection,  R.string.standard_questionnaire, R.string.switch_to_questionnaire_selection);
        //toggleById(R.id.questionnaire_layout, R.id.layout_questionnaires, R.string.switch_to_default_selection,   R.string.standard_questionnaire, R.string.switch_to_questionnaire_selection);
        toggleById(R.id.toggle_user_layout, R.id.layout_users, R.string.create_new_user, R.string.switch_to_existing_user,  R.string.select_existing_user, R.string.switch_to_new_user);
        toggleById(R.id.questionnaire_layout, R.id.layout_questionnaires, R.string.standard_questionnaire, R.string.switch_to_questionnaire_selection,  R.string.configurable_questionnaire, R.string.switch_to_default_selection);

    }

    private void toggleById(int toggleUserLayout, final int layoutToToggle, final int titleTextId, final int subTitleTextId, final int invisibleTitleTextId, final int invisibleSubTitleTextId){
        final View toggleTitleLayout =  dialog.getView().findViewById(toggleUserLayout);
        Switch questionnaireSwitch = (Switch) toggleTitleLayout.findViewById(R.id.switcher);

        // init UI
        final TextView title = (TextView) dialog.getView().findViewById(R.id.title);
        final TextView subtitle = (TextView) dialog.getView().findViewById(R.id.subtitle);
        title.setText(Strings.getStringByRId(titleTextId));
        subtitle.setText(Strings.getStringByRId(subTitleTextId));

        questionnaireSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RelativeLayout layout = (RelativeLayout) dialog.getView().findViewById(layoutToToggle);
                // show details UI
                if(isChecked){
                    layout.setVisibility(View.VISIBLE);
                    title.setText(Strings.getStringByRId(titleTextId));
                    subtitle.setText(Strings.getStringByRId(subTitleTextId));
                }
                // hide details
                else{
                    layout.setVisibility(View.GONE);
                    title.setText(Strings.getStringByRId(invisibleTitleTextId));
                    subtitle.setText(Strings.getStringByRId(invisibleSubTitleTextId));
                }
            }
        });
    }
}
