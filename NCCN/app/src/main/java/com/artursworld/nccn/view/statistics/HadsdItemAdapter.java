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
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.model.entity.HADSDQuestionnaire;

import java.util.List;


public class HadsdItemAdapter extends ArrayAdapter<HADSDQuestionnaire> {

    private static String CLASS_NAME = HadsdStatisticsFragment.class.getName();
    private List<HADSDQuestionnaire> list = null;
    private Activity activity = null;


    public HadsdItemAdapter(Activity activity, List<HADSDQuestionnaire> list) {
        super(activity, R.layout.adapter_hadsd_questionnaire_item, list);
        Log.i(CLASS_NAME, "Adapter loaded by list: " +list.toString());
        this.activity = activity;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = activity.getApplicationContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_hadsd_questionnaire_item, null);
        }

        TextView date = (TextView) convertView.findViewById(R.id.date_txt);
        TextView anxietyPoints = (TextView) convertView.findViewById(R.id.anxiety_points_txt);
        TextView depressionPoints = (TextView) convertView.findViewById(R.id.points_depression_txt);

        if(date != null)
            date.setText(Strings.getStringByRId(R.string.date) + ": " + list.get(position).getCreationTimeStamp());

        if( anxietyPoints != null)
            anxietyPoints.setText(Strings.getStringByRId(R.string.anxiety) + ": " + list.get(position).getAnxietyScore() + " " + Strings.getStringByRId(R.string.points));

        if( depressionPoints != null)
             depressionPoints.setText(Strings.getStringByRId(R.string.depression) + ": " + list.get(position).getDepressionScore()+ " " + Strings.getStringByRId(R.string.points));

        return convertView;
    }

}
