package com.artursworld.nccn.view.questionnaire;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.controller.wizard.WizardHADSD;
import com.artursworld.nccn.controller.wizard.WizardNCCN;
import com.artursworld.nccn.controller.wizard.WizardQualityOfLife;
import com.artursworld.nccn.model.entity.AbstractQuestionnaire;
import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;
import com.artursworld.nccn.model.entity.HADSDQuestionnaire;
import com.artursworld.nccn.model.entity.QolQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.DistressThermometerQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.HADSDQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.QualityOfLifeManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;

import java.util.ArrayList;
import java.util.List;

public class QuestionnaireSelectListFragment extends Fragment {

    private static final String CLASS_NAME = QuestionnaireSelectListFragment.class.getSimpleName();

    // UI
    private ListView questionnaireListView = null;
    private TextView percentageAll = null;

    public QuestionnaireSelectListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(CLASS_NAME, " onCreateView()");
        View view = inflater.inflate(R.layout.fragment_questionnaire_select_list, container, false);
        questionnaireListView = (ListView) view.findViewById(R.id.questionnaire_list_view);
        percentageAll = (TextView) view.findViewById(R.id.percentage_text);
        return view;
    }

    @NonNull
    private List<AbstractQuestionnaire> getQuestionnaireList() {
        List<AbstractQuestionnaire> list = new ArrayList<>();
        list.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.hadsd_questionnaire), 86));
        list.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.nccn_distress_thermometer), 23));
        list.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.quality_of_life_questionnaire), 99));
        return list;
    }

    private void addOnItemClickListener(final List<AbstractQuestionnaire> abstractQuestionnairesList) {
        if(questionnaireListView != null){
            questionnaireListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i(CLASS_NAME, "selected medicament=" + abstractQuestionnairesList.get(position) + " at position: " + position);

                    //TODO: stuff to delete and manage
                    UserManager m = new UserManager();
                    String userName = "Ahmet";
                    m.insertUser(new User(userName));
                    DistressThermometerQuestionnaireManager qm = new DistressThermometerQuestionnaireManager();
                    qm.insertQuestionnaire(new DistressThermometerQuestionnaire(userName));
                    QualityOfLifeManager qm1 = new QualityOfLifeManager();
                    qm1.insertQuestionnaire(new QolQuestionnaire(userName));
                    HADSDQuestionnaireManager qm2 = new HADSDQuestionnaireManager();
                    qm2.insertQuestionnaire(new HADSDQuestionnaire(userName));


                    if(position == 0){
                        startActivityForResult(new Intent(getContext(), WizardHADSD.class), 2);
                    }
                    else if (position == 1){
                        startActivityForResult(new Intent(getContext(), WizardNCCN.class), 1);
                    }
                    else if (position == 2){
                        startActivityForResult(new Intent(getContext(), WizardQualityOfLife.class), 3);
                    }
                }
            });
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(CLASS_NAME, "onResume()");
        fillQuestionnaireListViewByItems();

    }

    private void fillQuestionnaireListViewByItems() {
        new AsyncTask<Void, Void, List<AbstractQuestionnaire>>() {

            @Override
            protected List<AbstractQuestionnaire> doInBackground(Void... params) {
                return getQuestionnaireList();
            }

            @Override
            protected void onPostExecute(final List<AbstractQuestionnaire> abstractQuestionnairesList) {
                super.onPostExecute(abstractQuestionnairesList);
                AbstractQuestionnaireItemAdapter adapter = new AbstractQuestionnaireItemAdapter(getActivity(), abstractQuestionnairesList);
                questionnaireListView.setAdapter(adapter);
                addOnItemClickListener(abstractQuestionnairesList);
                percentageAll.setText(getPercentageForAllQuestionnairesByList(abstractQuestionnairesList)+"");
            }
        }.execute();
    }

    /**
     * Get the progress in percentage of all questionnairies by the questionnaire list
     * @param list the list where to count the percentage
     * @return the mean of all progress percentages
     */
    private int getPercentageForAllQuestionnairesByList(List<AbstractQuestionnaire> list) {
        if(list != null){
            int count = list.size();
            int sum = 0;
            for(AbstractQuestionnaire item: list){
                sum += item.getProgressInPercent();
            }

            if(count != 0){
                return sum/count;
            }
        }
        return 0;
    }

}
