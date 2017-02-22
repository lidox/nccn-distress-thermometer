package com.artursworld.nccn.view.statistics;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.util.Dates;
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.model.entity.QolQuestionnaire;

import java.util.Date;
import java.util.List;

public class BrainCancerModuleAdapter extends ArrayAdapter<QolQuestionnaire> {

    private static String CLASS_NAME = BrainCancerModuleAdapter.class.getName();
    private List<QolQuestionnaire> list = null;
    private Activity activity = null;

    public BrainCancerModuleAdapter(Activity activity, List<QolQuestionnaire> list) {
        super(activity, R.layout.adapter_brain_cancer_module_item, list);
        Log.i(CLASS_NAME, "Adapter loaded by list: " + list.toString());
        this.activity = activity;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = activity.getApplicationContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_brain_cancer_module_item, null);
        }

        TextView date = (TextView) convertView.findViewById(R.id.date_txt);
        TextView score_future_uncertaintyText = (TextView) convertView.findViewById(R.id.score_future_uncertainty);
        TextView score_visual_disorderText = (TextView) convertView.findViewById(R.id.score_visual_disorder);
        TextView score_motor_dysfunctionText = (TextView) convertView.findViewById(R.id.score_motor_dysfunction);
        TextView score_communication_deficitText = (TextView) convertView.findViewById(R.id.score_communication_deficit);
        TextView score_headachesText = (TextView) convertView.findViewById(R.id.score_headaches);
        TextView score_seizuresText = (TextView) convertView.findViewById(R.id.score_seizures);
        TextView score_drowsinessText = (TextView) convertView.findViewById(R.id.score_drowsiness);
        TextView score_hair_lossText = (TextView) convertView.findViewById(R.id.score_hair_loss);
        TextView score_itchy_skinText = (TextView) convertView.findViewById(R.id.score_itchy_skin);
        TextView score_weakness_of_legsText = (TextView) convertView.findViewById(R.id.score_weakness_of_legs);
        TextView score_bladder_controlText = (TextView) convertView.findViewById(R.id.score_bladder_control);

        QolQuestionnaire questionnaire = list.get(position);


        if (date != null) {
            Date cDate = questionnaire.getCreationDate_PK();
            String creationDate = Dates.getGermanDateByDate(cDate);
            date.setText(Strings.getStringByRId(R.string.date) + ": " + creationDate + " " + Strings.getStringByRId(R.string.oclock));
        }

        if (score_future_uncertaintyText != null) {
            score_future_uncertaintyText.setText(questionnaire.getFutureUncertaintyScore() + " ");
            score_visual_disorderText.setText(questionnaire.getVisualDisorderScore() + "");
            score_motor_dysfunctionText.setText(questionnaire.getMotorDysfunctionScore() + "");
            score_communication_deficitText.setText(questionnaire.getCommunicationDeficitScore() + "");
            score_headachesText.setText(questionnaire.getHeadachesScore() + "");
            score_seizuresText.setText(questionnaire.getSeizuresScore() + "");
            score_drowsinessText.setText(questionnaire.getDrowsinessScore() + "");
            score_hair_lossText.setText(questionnaire.getHairLossScore() + "");
            score_itchy_skinText.setText(questionnaire.getItchySkinScore() + "");
            score_weakness_of_legsText.setText(questionnaire.getWeaknessOfLegsScore() + "");
            score_bladder_controlText.setText(questionnaire.getBladderControlScore() + "");
        }

        return convertView;
    }

}
