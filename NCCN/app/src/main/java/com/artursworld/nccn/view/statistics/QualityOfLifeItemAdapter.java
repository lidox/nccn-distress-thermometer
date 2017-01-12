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
import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;
import com.artursworld.nccn.model.entity.QolQuestionnaire;

import java.util.Date;
import java.util.List;


public class QualityOfLifeItemAdapter extends ArrayAdapter<QolQuestionnaire> {

    private static String CLASS_NAME = QualityOfLifeItemAdapter.class.getName();
    private List<QolQuestionnaire> list = null;
    private Activity activity = null;

    public QualityOfLifeItemAdapter(Activity activity, List<QolQuestionnaire> list) {
        super(activity, R.layout.adapter_quality_of_life_questionnaire_item, list);
        Log.i(CLASS_NAME, "Adapter loaded by list: " +list.toString());
        this.activity = activity;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = activity.getApplicationContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_quality_of_life_questionnaire_item, null);
        }


        TextView date = (TextView) convertView.findViewById(R.id.date_txt);
        TextView globalHealthScoreText = (TextView) convertView.findViewById(R.id.global_health_status_score);
        TextView physical_functioning_scoreText = (TextView) convertView.findViewById(R.id.physical_functioning_score);
        TextView role_functioning_scoreText = (TextView) convertView.findViewById(R.id.role_functioning_score);
        TextView emotional_functioning_scoreText = (TextView) convertView.findViewById(R.id.emotional_functioning_score);
        TextView cognitive_functioning_scoreText = (TextView) convertView.findViewById(R.id.cognitive_functioning_score);
        TextView social_functioning_scoreText = (TextView) convertView.findViewById(R.id.social_functioning_score);

        TextView fatigue_scoreText = (TextView) convertView.findViewById(R.id.fatigue_score);
        TextView nausea_and_vomiting_scoreText = (TextView) convertView.findViewById(R.id.nausea_and_vomiting_score);
        TextView pain_scoreText = (TextView) convertView.findViewById(R.id.pain_score);
        TextView dyspnoea_scoreText = (TextView) convertView.findViewById(R.id.dyspnoea_score);
        TextView insomnia_scoreText = (TextView) convertView.findViewById(R.id.insomnia_score);
        TextView appetite_loss_scoreText = (TextView) convertView.findViewById(R.id.appetite_loss_score);
        TextView constipation_scoreText = (TextView) convertView.findViewById(R.id.constipation_score);
        TextView diarrhoea_scoreText = (TextView) convertView.findViewById(R.id.diarrhoea_score);
        TextView financial_difficulties_scoreText = (TextView) convertView.findViewById(R.id.financial_difficulties_score);

        QolQuestionnaire questionnaire = list.get(position);


        if(date != null) {
            Date cDate = questionnaire.getCreationDate_PK();
            String creationDate = Dates.getGermanDateByDate(cDate);
            date.setText(Strings.getStringByRId(R.string.date) + ": " + creationDate + " " +Strings.getStringByRId(R.string.oclock));
        }

        if(globalHealthScoreText != null){
            globalHealthScoreText.setText(questionnaire.getGlobalHealthScore() + " ");
            physical_functioning_scoreText.setText(questionnaire.getPhysicalFunctioningScore() + "");
            role_functioning_scoreText.setText(questionnaire.getRoleFunctioningScore() + "");
            emotional_functioning_scoreText.setText(questionnaire.getEmotionalFunctioningScore() + "");
            cognitive_functioning_scoreText.setText(questionnaire.getCognitiveFunctioningScore() + "");
            social_functioning_scoreText.setText(questionnaire.getSocialFunctioningScore() +"");
        }

        if(fatigue_scoreText !=null){
            fatigue_scoreText.setText(questionnaire.getFatigueScore() +"");
            nausea_and_vomiting_scoreText.setText(questionnaire.getNauseaAndVomitingScore() + "");
            pain_scoreText.setText(questionnaire.getPainScore() + "");
            dyspnoea_scoreText.setText(questionnaire.getDyspnoeaScore() + "");
            insomnia_scoreText.setText(questionnaire.getInsomniaScore()+ "");
            appetite_loss_scoreText.setText(questionnaire.getAppetiteLossScore()+ "");
            constipation_scoreText.setText(questionnaire.getConstipationScore() + "");
            diarrhoea_scoreText.setText(questionnaire.getDiarrhoeaScore()+ "");
            financial_difficulties_scoreText.setText(questionnaire.getFinancialDifficultiesScore()+ "");
        }

        /*
        if( anxietyPoints != null)
            anxietyPoints.setText(questionnaire.getDistressScore() + " " + Strings.getStringByRId(R.string.points));

        if(statusAnxietyImage != null) {
            if (questionnaire.hasDistress())
                statusAnxietyImage.setImageResource(R.drawable.ic_error_black_24dp);
            else
                statusAnxietyImage.setImageResource(R.drawable.ic_check_circle_black_24dp);
        }
        */

        return convertView;
    }
}
