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
import com.artursworld.nccn.model.entity.HADSDQuestionnaire;
import com.artursworld.nccn.model.persistence.manager.HADSDQuestionnaireManager;

import java.util.ArrayList;
import java.util.List;

public class HadsdStatisticsFragment extends Fragment {

    private static final String CLASS_NAME = HadsdStatisticsFragment.class.getSimpleName();
    private View rootView = null;
    HadsdItemAdapter adapter = null;
    List<HADSDQuestionnaire> questionnairesList = new ArrayList<>();

    public HadsdStatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_hadsd_statistics, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView userNameLabel = (TextView) rootView.findViewById(R.id.user_name_label);
        userNameLabel.setText(Global.getSelectedUser());

        TextView historyLabel = (TextView) rootView.findViewById(R.id.history_label);
        historyLabel.setText(Strings.getStringByRId(R.string.history)+ ": " +Strings.getStringByRId(R.string.hadsd_questionnaire) );

        fillHistoryWithItems();
    }

    private void fillHistoryWithItems() {
        new AsyncTask<Void, Void, List<HADSDQuestionnaire>>() {

            @Override
            protected List<HADSDQuestionnaire> doInBackground(Void... params) {
                return new HADSDQuestionnaireManager().getHadsdQuestionnaireListByUserName(Global.getSelectedUser());
            }

            @Override
            protected void onPostExecute(final List<HADSDQuestionnaire> list) {
                super.onPostExecute(list);
                Log.i(CLASS_NAME, "finished loading data: " + list);
                questionnairesList = list;
                adapter = new HadsdItemAdapter(getActivity(), questionnairesList);
                ListView questionnaireListView = (ListView) rootView.findViewById(R.id.hadsd_questionnaire_list);
                questionnaireListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }

}
