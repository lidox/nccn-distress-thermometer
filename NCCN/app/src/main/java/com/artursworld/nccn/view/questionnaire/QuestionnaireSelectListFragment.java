package com.artursworld.nccn.view.questionnaire;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.artursworld.nccn.R;
import com.artursworld.nccn.model.entity.AbstractQuestionnaire;

import java.util.ArrayList;
import java.util.List;

public class QuestionnaireSelectListFragment extends Fragment {

    private static final String CLASS_NAME = QuestionnaireSelectListFragment.class.getSimpleName();

    // UI
    private ListView questionnaireListView = null;

    public QuestionnaireSelectListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(CLASS_NAME, " onCreateView()");
        View view = inflater.inflate(R.layout.fragment_questionnaire_select_list, container, false);
        questionnaireListView = (ListView) view.findViewById(R.id.medicament_list_view);
        //emptyListTextView = (TextView) view.findViewById(R.id.empty_medicament_list);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(CLASS_NAME, "onResume()");
        fillQuestionnaireListViewByItems();

    }

    private void fillQuestionnaireListViewByItems() {
        //questionnaireListView = new ListView(getActivity().getApplicationContext());
        //TODO: not implemented yet
        new AsyncTask<Void, Void, List<AbstractQuestionnaire>>() {


            @Override
            protected List<AbstractQuestionnaire> doInBackground(Void... params) {
                List<AbstractQuestionnaire> list = new ArrayList<>();
                list.add(new AbstractQuestionnaire("QualityOfLife", 86));
                list.add(new AbstractQuestionnaire("Thermometer", 23));
                list.add(new AbstractQuestionnaire("HADSD", 99));

                return null;
            }

            @Override
            protected void onPostExecute(List<AbstractQuestionnaire> abstractQuestionnairesList) {
                super.onPostExecute(abstractQuestionnairesList);
                AbstractQuestionnaireItemAdapter adapter = new AbstractQuestionnaireItemAdapter(getActivity(), abstractQuestionnairesList);
                questionnaireListView.setAdapter(adapter);

                questionnaireListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.i(CLASS_NAME, "selected medicament=" + abstractQuestionnairesList.get(position) + " at position: " + position);
                    }
                });
            }


        }.execute();
    }

}
