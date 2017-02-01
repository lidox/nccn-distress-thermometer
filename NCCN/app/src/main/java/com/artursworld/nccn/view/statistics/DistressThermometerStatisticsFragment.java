package com.artursworld.nccn.view.statistics;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.util.Global;
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;
import com.artursworld.nccn.model.persistence.manager.DistressThermometerQuestionnaireManager;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DistressThermometerStatisticsFragment extends Fragment {


    private static final String CLASS_NAME = DistressThermometerStatisticsFragment.class.getSimpleName();
    private View rootView = null;

    public DistressThermometerStatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_distress_thermometer_statistics, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView userNameLabel = (TextView) rootView.findViewById(R.id.user_name_label);
        userNameLabel.setText(Global.getSelectedStatisticUser());

        TextView historyLabel = (TextView) rootView.findViewById(R.id.history_label);
        historyLabel.setText(Strings.getStringByRId(R.string.history)+ ": " +Strings.getStringByRId(R.string.nccn_distress_thermometer));

        fillHistoryWithItems();
    }

    private void fillHistoryWithItems() {
        new AsyncTask<Void, Void, List<DistressThermometerQuestionnaire>>() {

            @Override
            protected List<DistressThermometerQuestionnaire> doInBackground(Void... params) {
                return new DistressThermometerQuestionnaireManager().getDistressThermometerQuestionnaireList(Global.getSelectedStatisticUser());
            }

            @Override
            protected void onPostExecute(final List<DistressThermometerQuestionnaire> list) {
                super.onPostExecute(list);
                Log.i(CLASS_NAME, "finished loading data: " + list);
                DistressThermometerItemAdapter adapter = new DistressThermometerItemAdapter(getActivity(), list);
                ListView questionnaireListView = (ListView) rootView.findViewById(R.id.quality_of_life_questionnaire_list);
                questionnaireListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }

}
