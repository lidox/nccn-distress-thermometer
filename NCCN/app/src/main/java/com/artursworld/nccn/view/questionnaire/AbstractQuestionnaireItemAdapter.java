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

/**
 * Created by Russland on 05.12.2016.
 */

public class AbstractQuestionnaireItemAdapter extends ArrayAdapter<AbstractQuestionnaire> {

    private List<AbstractQuestionnaire> list = null;
    private Activity activity = null;


    public AbstractQuestionnaireItemAdapter(Activity activity, List<AbstractQuestionnaire> medicamentList) {
        super(activity.getApplicationContext(), R.layout.adapter_medicament_list, medicamentList);
        this.activity = activity;
        this.list = medicamentList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = activity.getApplicationContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_medicament_list, null);
        }

        TextView timeLabel = (TextView) convertView.findViewById(R.id.ad_time_label);
        TextView dateLabel = (TextView) convertView.findViewById(R.id.ad_date_label);
        TextView medicamentNameLabel = (TextView) convertView.findViewById(R.id.ad_name_label);
        TextView dosisLabel = (TextView) convertView.findViewById(R.id.ad_dosis_label);
        TextView unitLabel = (TextView) convertView.findViewById(R.id.ad_unit_label);

        if(timeLabel != null)
            timeLabel.setText(list.get(position).getTime());

        if(dateLabel != null)
            dateLabel.setText(list.get(position).getDate());

        if(medicamentNameLabel != null)
            medicamentNameLabel.setText(list.get(position).getName());

        if(dosisLabel != null)
            dosisLabel.setText(list.get(position).getDosage()+"");

        if(unitLabel != null)
            unitLabel.setText(list.get(position).getUnit());

        return convertView;
    }

}
