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
import com.artursworld.nccn.model.entity.QolQuestionnaire;
import com.artursworld.nccn.model.persistence.manager.QualityOfLifeManager;

import java.util.List;

public class BrainCancerModuleStatisticsFragment extends Fragment {

    private static final String CLASS_NAME = BrainCancerModuleStatisticsFragment.class.getSimpleName();
    private View rootView = null;

    public BrainCancerModuleStatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //TODO: change fragment:
        rootView =  inflater.inflate(R.layout.fragment_brain_cancer_module_statistics, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView userNameLabel = (TextView) rootView.findViewById(R.id.user_name_label);
        userNameLabel.setText(Global.getSelectedStatisticUser());

        TextView historyLabel = (TextView) rootView.findViewById(R.id.history_label);
        historyLabel.setText(Strings.getStringByRId(R.string.history)+ ": " +Strings.getStringByRId(R.string.brain_cancer_module));

        fillHistoryWithItems();
    }

    private void fillHistoryWithItems() {
        new AsyncTask<Void, Void, List<QolQuestionnaire>>() {

            @Override
            protected List<QolQuestionnaire> doInBackground(Void... params) {
                return new QualityOfLifeManager().getQolQuestionnaireList(Global.getSelectedStatisticUser());
            }

            @Override
            protected void onPostExecute(final List<QolQuestionnaire> list) {
                super.onPostExecute(list);
                Log.i(CLASS_NAME, "finished loading data: " + list);
                BrainCancerModuleAdapter adapter = new BrainCancerModuleAdapter(getActivity(), list);
                ListView questionnaireListView = (ListView) rootView.findViewById(R.id.brain_cancer_module_list);
                questionnaireListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }

}
