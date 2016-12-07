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
        /*
        int switch_id = R.id.switch_questionnairies;
        final int layout_id = R.id.layout_questionnaires;
        final int titleID = R.id.title2;
        final int subtitleId = R.id.subtitle2;
        final int titleTextId = R.string.configurable_questionnaire;
        final int subTitleTextId = R.string.switch_to_default_selection;
        final int invisibleTitleTextId = R.string.standard_questionnaire;
        final int invisibleSubTitleTextId = R.string.switch_to_questionnaire_selection;

        //toggleByIds(switch_id, layout_id, titleID, subtitleId, titleTextId, subTitleTextId, invisibleTitleTextId, invisibleSubTitleTextId);
        toggleByIds(R.id.user_layout, R.id.switch_users, R.id.layout_users, R.id.title1, R.id.subtitle1, R.string.select_existing_user, R.string.switch_to_new_user, R.string.create_new_user, R.string.switch_to_existing_user);
        */

        toggleById(R.id.toggle_user_layout, R.string.configurable_questionnaire, R.string.switch_to_default_selection,  R.string.standard_questionnaire, R.string.switch_to_questionnaire_selection);
    }

    private void toggleById(int toggle_user_layout, final int titleTextId, final int subTitleTextId, final int invisibleTitleTextId, final int invisibleSubTitleTextId){
        RelativeLayout layout = (RelativeLayout) dialog.getView().findViewById(R.id.relative);
        final View toggleTitleLayout = layout.findViewById(R.id.toggle_user_layout);
        Switch questionnaireSwitch = (Switch) toggleTitleLayout.findViewById(R.id.switcher);
        questionnaireSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // init UI
                RelativeLayout layout = (RelativeLayout) toggleTitleLayout.findViewById(R.id.relative);
                TextView title = (TextView) toggleTitleLayout.findViewById(R.id.title);
                TextView subtitle = (TextView) toggleTitleLayout.findViewById(R.id.subtitle);

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

    private void toggleByIds(final int root_layout_id,int switch_id, final int layout_id, final int titleID, final int subtitleId, final int titleTextId, final int subTitleTextId, final int invisibleTitleTextId, final int invisibleSubTitleTextId) {
        final View yourLayout = dialog.getView().findViewById(root_layout_id);
        Switch questionnaireSwitch = (Switch) yourLayout.findViewById(switch_id);
        questionnaireSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // init UI
                RelativeLayout layout = (RelativeLayout) yourLayout.findViewById(layout_id);
                TextView title = (TextView) yourLayout.findViewById(titleID);
                TextView subtitle = (TextView) yourLayout.findViewById(subtitleId);

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
