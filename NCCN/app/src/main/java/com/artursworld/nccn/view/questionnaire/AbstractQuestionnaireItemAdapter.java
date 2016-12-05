package com.artursworld.nccn.view.questionnaire;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.artursworld.nccn.R;
import com.artursworld.nccn.model.entity.AbstractQuestionnaire;

import java.util.List;


public class AbstractQuestionnaireItemAdapter extends ArrayAdapter<AbstractQuestionnaire> {

    private List<AbstractQuestionnaire> list = null;
    private Activity activity = null;


    public AbstractQuestionnaireItemAdapter(Activity activity, List<AbstractQuestionnaire> list) {
        super(activity.getApplicationContext(), R.layout.adapter_questionnaire_item, list);
        this.activity = activity;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = activity.getApplicationContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_questionnaire_item, null);
        }

        TextView proccessPercentage = (TextView) convertView.findViewById(R.id.procces_percentage_txt);
        TextView questionnaireName = (TextView) convertView.findViewById(R.id.questionnaire_name_txt);

        if(proccessPercentage != null)
            proccessPercentage.setText(list.get(position).getProgressInPercent() +"%");

        if(questionnaireName != null)
            questionnaireName.setText(list.get(position).getName());


        return convertView;
    }

}
