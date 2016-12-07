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

public class UserStartConfiguration {

    public static String CLASS_NAME = UserStartConfiguration.class.getSimpleName();

    // Attributes
    private MaterialDialog dialog;
    private Context cxt = null;

    public UserStartConfiguration(Context context){
        cxt = context;
    }

    public void showConfigurationDialog(){
        MaterialDialog.Builder b = new MaterialDialog.Builder(cxt)
                .title(R.string.questionnaire_to_display)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .customView(R.layout.dialog_select_questionnairies, true)
                .onPositive(new MaterialDialog.SingleButtonCallback() {

                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Log.i(CLASS_NAME, "ok");
                    }
                });
        dialog = b.show();
        toggleById(R.id.toggle_user_layout, R.id.layout_users, R.string.select_existing_user, R.string.switch_to_new_user,  R.string.create_new_user, R.string.switch_to_existing_user);
        toggleById(R.id.included_questionnaire_title, R.id.layout_below_included_questionnaire_title, R.string.configurable_questionnaire, R.string.switch_to_default_selection,R.string.standard_questionnaire, R.string.switch_to_questionnaire_selection);
        }

    /**
     * Toggle View Content which is below Title and Subtitle
     * @param barToggleLayoutId the id of included title, subtitle and switcher layout
     * @param layoutIdBelowBarToggleLayout  the id of layout below included title, subtitle and switcher layout
     * @param titleTextId the title displayed if toggle button is checked
     * @param subTitleTextId the subtitle displayed if toggle button is checked
     * @param invisibleTitleTextId the title displayed if toggle button is unchecked
     * @param invisibleSubTitleTextId the subtitle displayed if toggle button is unchecked
     */
    private void toggleById(int barToggleLayoutId, final int layoutIdBelowBarToggleLayout, final int titleTextId, final int subTitleTextId, final int invisibleTitleTextId, final int invisibleSubTitleTextId){
        final View toggleTitleLayout =  dialog.getView().findViewById(barToggleLayoutId);
        Switch questionnaireSwitch = (Switch) toggleTitleLayout.findViewById(R.id.switcher);

        // init UI
        final TextView title = (TextView) toggleTitleLayout.findViewById(R.id.title);
        final TextView subtitle = (TextView) toggleTitleLayout.findViewById(R.id.subtitle);
        title.setText(Strings.getStringByRId(invisibleTitleTextId));
        subtitle.setText(Strings.getStringByRId(invisibleSubTitleTextId));

        questionnaireSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RelativeLayout layout = (RelativeLayout) dialog.getView().findViewById(layoutIdBelowBarToggleLayout);

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
