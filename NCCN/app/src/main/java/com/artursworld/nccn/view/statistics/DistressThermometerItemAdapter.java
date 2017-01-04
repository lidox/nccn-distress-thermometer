package com.artursworld.nccn.view.statistics;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.util.Dates;
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;

import java.util.Date;
import java.util.List;

public class DistressThermometerItemAdapter extends ArrayAdapter<DistressThermometerQuestionnaire> {

    private static String CLASS_NAME = DistressThermometerItemAdapter.class.getName();
    private List<DistressThermometerQuestionnaire> list = null;
    private Activity activity = null;


    public DistressThermometerItemAdapter(Activity activity, List<DistressThermometerQuestionnaire> list) {
        super(activity, R.layout.adapter_distress_thermometer_questionnaire_item, list);
        Log.i(CLASS_NAME, "Adapter loaded by list: " +list.toString());
        this.activity = activity;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = activity.getApplicationContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_distress_thermometer_questionnaire_item, null);
        }

        TextView date = (TextView) convertView.findViewById(R.id.date_txt);
        TextView anxietyPoints = (TextView) convertView.findViewById(R.id.score_txt);
        ImageView statusAnxietyImage = (ImageView) convertView.findViewById(R.id.status_img);

        DistressThermometerQuestionnaire questionnaire = list.get(position);


        if(date != null) {
            Date cDate = questionnaire.getCreationDate_PK();
            String creationDate = Dates.getGermanDateByDate(cDate);
            date.setText(Strings.getStringByRId(R.string.date) + ": " + creationDate + " " +Strings.getStringByRId(R.string.oclock));
        }

        if( anxietyPoints != null)
            anxietyPoints.setText(questionnaire.getDistressScore() + " " + Strings.getStringByRId(R.string.points));

        if(statusAnxietyImage != null) {
            if (questionnaire.hasDistress())
                statusAnxietyImage.setImageResource(R.drawable.ic_error_black_24dp);
            else
                statusAnxietyImage.setImageResource(R.drawable.ic_check_circle_black_24dp);
        }

        return convertView;
    }

}
