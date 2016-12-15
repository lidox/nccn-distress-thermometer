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
import com.artursworld.nccn.controller.util.Global;
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
import java.util.Date;
import java.util.List;
import java.util.Set;

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

        // set new Date for questionnaires, for the case that a new user need to be created
        Date selectedQuestionnaireDate = null;
        String selectedUserName = Global.getSelectedUser();

        // check if flag is set e.g. in UserStartConfiguration.class
        boolean hasToCreateNewUser = Global.hasToCreateNewUser();

        if (hasToCreateNewUser) {
            User user = new User(Strings.getStringByRId(R.string.user_name));
            new UserManager().insertUser(user);
            fillQuestionnaireListByUserNameAndCreationDate(list, new Date(), user, hasToCreateNewUser);
        }
        // a user has been selected
        else {
            User user = new UserManager().getUserByName(selectedUserName);
            selectedQuestionnaireDate = Global.getSelectedQuestionnaireDate();
            if (selectedQuestionnaireDate != null) {
                fillQuestionnaireListByUserNameAndCreationDate(list, selectedQuestionnaireDate, user, hasToCreateNewUser);
            }
        }

        return list;
    }

    /**
     * Fills a questionnaire list which is displayed on start screen
     * If user name equals 'create user flag' the default questionnaires will be displayed
     * and puts the progress values into list by questionnaire type
     *
     * @param list                      the list to fill
     * @param selectedQuestionnaireDate the date to use for the questionnaires
     * @param user                      the selected user
     */
    private void fillQuestionnaireListByUserNameAndCreationDate(List<AbstractQuestionnaire> list, Date selectedQuestionnaireDate, User user, boolean hasToCreateNewUser) {
        int hadsProgress = 0;
        int distressProgress = 0;
        int qualityProgress = 0;

        Set<String> setOfBooleans = Global.getSelectedQuestionnairesForStartScreen();
        if (setOfBooleans != null) {
            boolean hasToGetProgress = !Global.hasToCreateNewUser() && user!= null;
            if (setOfBooleans.contains(Strings.getStringByRId(R.string.hadsd_questionnaire))) {
                if(hasToGetProgress) {
                    HADSDQuestionnaire hadsdQuestionnaire = new HADSDQuestionnaireManager().getHADSDQuestionnaireByDate_PK(user.getName(), selectedQuestionnaireDate);
                    if(hadsdQuestionnaire != null)
                        hadsProgress = hadsdQuestionnaire.getProgressInPercent();
                }
                list.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.hadsd_questionnaire), hadsProgress));
            }
            if (setOfBooleans.contains(Strings.getStringByRId(R.string.nccn_distress_thermometer))) {
                if(hasToGetProgress) {
                    DistressThermometerQuestionnaire distressThermometerQuestionnaire = new DistressThermometerQuestionnaireManager().getDistressThermometerQuestionnaireByDate(user.getName(), selectedQuestionnaireDate);
                    if(distressThermometerQuestionnaire!=null)
                        distressProgress = distressThermometerQuestionnaire.getProgressInPercent();
                }
                list.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.nccn_distress_thermometer), distressProgress));
            }
            if (setOfBooleans.contains(Strings.getStringByRId(R.string.quality_of_life_questionnaire))) {
                if(hasToGetProgress) {
                    QolQuestionnaire qolQuestionnaire = new QualityOfLifeManager().getQolQuestionnaireByDate(user.getName(), selectedQuestionnaireDate);
                    if(qolQuestionnaire!=null)
                        qualityProgress = qolQuestionnaire.getProgressInPercent();
                }
                list.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.quality_of_life_questionnaire), qualityProgress));
            }
        } else {
            Log.i(CLASS_NAME, "No configuration to display questionnaires has been found, so display the defaults");
            list.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.hadsd_questionnaire), hadsProgress));
            list.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.nccn_distress_thermometer), distressProgress));
            list.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.quality_of_life_questionnaire), qualityProgress));
        }
    }

    private void addOnItemClickListener(final List<AbstractQuestionnaire> abstractQuestionnairesList) {
        if (questionnaireListView != null) {
            questionnaireListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i(CLASS_NAME, "selected medicament=" + abstractQuestionnairesList.get(position) + " at position: " + position);
                    onStartQuestionnaire(position);
                }
            });
        }
    }

    private void onStartQuestionnaire(int position) {
        String selectedUser = Global.getSelectedUser();
        Date selectedDate = Global.getSelectedQuestionnaireDate();

        //TODO: stuff to delete and manage
        //DistressThermometerQuestionnaireManager qm = new DistressThermometerQuestionnaireManager();
        //qm.insertQuestionnaire(new DistressThermometerQuestionnaire(userName));
        // QualityOfLifeManager qm1 = new QualityOfLifeManager();
        //qm1.insertQuestionnaire(new QolQuestionnaire(userName));
        //HADSDQuestionnaireManager qm2 = new HADSDQuestionnaireManager();
        // qm2.insertQuestionnaire(new HADSDQuestionnaire(userName));


        if (position == 0) {
            startActivityForResult(new Intent(getContext(), WizardHADSD.class), 2);
        } else if (position == 1) {
            startActivityForResult(new Intent(getContext(), WizardNCCN.class), 1);
        } else if (position == 2) {
            startActivityForResult(new Intent(getContext(), WizardQualityOfLife.class), 3);
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
                percentageAll.setText(getPercentageForAllQuestionnairesByList(abstractQuestionnairesList) + "");
            }
        }.execute();
    }

    /**
     * Get the progress in percentage of all questionnairies by the questionnaire list
     *
     * @param list the list where to count the percentage
     * @return the mean of all progress percentages
     */
    private int getPercentageForAllQuestionnairesByList(List<AbstractQuestionnaire> list) {
        if (list != null) {
            int count = list.size();
            int sum = 0;
            for (AbstractQuestionnaire item : list) {
                sum += item.getProgressInPercent();
            }

            if (count != 0) {
                return sum / count;
            }
        }
        return 0;
    }

}
