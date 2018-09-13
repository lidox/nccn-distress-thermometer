package com.artursworld.nccn.view.questionnaire;

import android.content.Intent;
import android.os.Bundle;
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
import com.artursworld.nccn.controller.wizard.WizardFearOfProgression;
import com.artursworld.nccn.controller.wizard.WizardHADSD;
import com.artursworld.nccn.controller.wizard.WizardNCCN;
import com.artursworld.nccn.controller.wizard.WizardQualityOfLife;
import com.artursworld.nccn.model.entity.AbstractQuestionnaire;
import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;
import com.artursworld.nccn.model.entity.FearOfProgressionQuestionnaire;
import com.artursworld.nccn.model.entity.HADSDQuestionnaire;
import com.artursworld.nccn.model.entity.QolQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.DistressThermometerQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.EntityDbManager;
import com.artursworld.nccn.model.persistence.manager.FearOfProgressionManager;
import com.artursworld.nccn.model.persistence.manager.HADSDQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.QualityOfLifeManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class QuestionnaireSelectListFragment extends Fragment {

    private static String CLASS_NAME = QuestionnaireSelectListFragment.class.getSimpleName();

    // UI
    private ListView questionnaireListView = null;
    private TextView percentageAllTextView = null;

    public QuestionnaireSelectListFragment() {
        // Fragment requires empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(CLASS_NAME, " onCreateView()");
        View view = inflater.inflate(R.layout.fragment_questionnaire_select_list, container, false);
        questionnaireListView = (ListView) view.findViewById(R.id.questionnaire_list_view);
        percentageAllTextView = (TextView) view.findViewById(R.id.percentage_text);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(CLASS_NAME, "onResume()");
        showQuestionnairesInListView();
    }

    /**
     * Displays the questionnaires containing progress in percentage
     */
    private void showQuestionnairesInListView() {

        // check if user is selected
        boolean userExisting = Global.getSelectedUser() != null;
        User user = new UserManager().getUserByName(Global.getSelectedUser());
        Date selectedQuestionnaireDate = Global.getSelectedQuestionnaireDate();

        Log.i(CLASS_NAME, "Loading User = '" + user.getName() + "' and selected QuestionnaireDate = '" + selectedQuestionnaireDate + "'");
        if (userExisting && user != null && selectedQuestionnaireDate != null) {

            fillQuestionnaireListView(user, selectedQuestionnaireDate);

        } else {
            Log.e(CLASS_NAME, "UNEXPECTED ERROR! PLEASE CHECK THE CODE. User should always exist and a date should be selected.");
        }
    }

    /**
     * Fills a questionnaire list which is displayed on start screen
     * If user name equals 'create user flag' the default questionnaires will be displayed
     * and puts the progress values into list by questionnaire type
     *
     * @param creationDate the date to use for the questionnaires
     * @param user         the selected user
     */
    private void fillQuestionnaireListView(User user, Date creationDate) {
        List<AbstractQuestionnaire> questionnairesList = new ArrayList<>();

        int hadsProgress = 0;
        int distressProgress = 0;
        int qualityProgress = 0;
        int fearProgress = 0;


        if (Global.hasToCreateNewUser()) {

            Log.i(CLASS_NAME, "Has to Create New Questionnaire");

            if (Global.hasToUseDefaultQuestionnaire()) {
                questionnairesList = getDefaultQuestionnairesList(hadsProgress, distressProgress, qualityProgress, fearProgress);
            } else if (Global.getSelectedQuestionnairesForStartScreen() != null) {
                questionnairesList = getSelectedQuestionnairesList(hadsProgress, distressProgress, qualityProgress, fearProgress);
            } else {
                Log.e(CLASS_NAME, "Strange. Selected Questionnaires has been null");
                questionnairesList = getDefaultQuestionnairesList(hadsProgress, distressProgress, qualityProgress, fearProgress);
            }

        } else {
            // new user has not to be created

            Set<String> setOfBooleans = Global.getSelectedQuestionnairesForStartScreen();
            Log.i(CLASS_NAME, "got selected Questionnaires For StartScreen = " + setOfBooleans);
            if (setOfBooleans != null) {

                boolean hasToGetProgress = !Global.hasToCreateNewUser() && user != null;
                Log.i(CLASS_NAME, "has to get progress of questionnaires =  " + hasToGetProgress);
                // HADS-D
                if (setOfBooleans.contains(Strings.getStringByRId(R.string.hadsd_questionnaire))) {
                    if (hasToGetProgress) {
                        HADSDQuestionnaire hadsdQuestionnaire = new HADSDQuestionnaireManager().getHADSDQuestionnaireByDate_PK(user.getName(), creationDate);
                        if (hadsdQuestionnaire != null) {
                            hadsProgress = hadsdQuestionnaire.getProgressInPercent();
                            Log.i(CLASS_NAME, "Progress loading HADS-D = " + hadsProgress + "%");
                        }
                    }
                    questionnairesList.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.hadsd_questionnaire), hadsProgress));
                }

                // Distress Thermometer
                if (setOfBooleans.contains(Strings.getStringByRId(R.string.nccn_distress_thermometer))) {
                    if (hasToGetProgress) {
                        DistressThermometerQuestionnaire distressThermometerQuestionnaire = new DistressThermometerQuestionnaireManager().getDistressThermometerQuestionnaireByDate(user.getName(), creationDate);
                        if (distressThermometerQuestionnaire != null) {
                            distressProgress = distressThermometerQuestionnaire.getProgressInPercent();
                            Log.i(CLASS_NAME, "Progress loading DistressThermometer = " + distressProgress + "%");
                        }
                    }
                    questionnairesList.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.nccn_distress_thermometer), distressProgress));
                }

                // Quality Of Life
                if (setOfBooleans.contains(Strings.getStringByRId(R.string.quality_of_life_questionnaire))) {
                    if (hasToGetProgress) {
                        QolQuestionnaire qolQuestionnaire = new QualityOfLifeManager().getQolQuestionnaireByDate(user.getName(), creationDate);
                        if (qolQuestionnaire != null) {
                            qualityProgress = qolQuestionnaire.getProgressInPercent();
                            Log.i(CLASS_NAME, "Progress loading QualityOfLife = " + qualityProgress + "%");
                        }
                    }
                    questionnairesList.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.quality_of_life_questionnaire), qualityProgress));
                }
                // Fear of Progression Questionnaire
                if (setOfBooleans.contains(Strings.getStringByRId(R.string.fear_of_progression_questionnaire))) {
                    if (hasToGetProgress) {
                        FearOfProgressionQuestionnaire fearOfProgressionQuestionnaire = new FearOfProgressionManager().getQuestionnaireByDate(user.getName(), creationDate);
                        if (fearOfProgressionQuestionnaire != null) {
                            fearProgress = fearOfProgressionQuestionnaire.getProgressInPercent();
                            Log.i(CLASS_NAME, "Progress loading FearOfProgression = " + fearProgress + "%");
                        }
                    }
                    questionnairesList.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.fear_of_progression_questionnaire), fearProgress));
                }
            } else {
                Log.w(CLASS_NAME, "something went wrong");
            }
        }

        AbstractQuestionnaireItemAdapter adapter = new AbstractQuestionnaireItemAdapter(getActivity(), questionnairesList);
        Log.i(CLASS_NAME, "Abstract Questionnaire questionnairesList = " + questionnairesList.toString());
        if (adapter != null) {
            questionnaireListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        int percentageForAllQuestionnaires = getPercentageForAllQuestionnairesByList(questionnairesList);
        percentageAllTextView.setText(percentageForAllQuestionnaires + " ");
        addOnItemClickListener(questionnairesList);
    }

    private List<AbstractQuestionnaire> getSelectedQuestionnairesList(int hadsProgress, int distressProgress, int qualityProgress, int fearProgress) {
        List<AbstractQuestionnaire> questionnairesList = new ArrayList<>();
        Set<String> setOfBooleans = Global.getSelectedQuestionnairesForStartScreen();

        if (setOfBooleans.contains(Strings.getStringByRId(R.string.hadsd_questionnaire)))
            questionnairesList.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.hadsd_questionnaire), hadsProgress));

        if (setOfBooleans.contains(Strings.getStringByRId(R.string.nccn_distress_thermometer)))
            questionnairesList.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.nccn_distress_thermometer), distressProgress));

        if (setOfBooleans.contains(Strings.getStringByRId(R.string.quality_of_life_questionnaire)))
            questionnairesList.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.quality_of_life_questionnaire), qualityProgress));

        if (setOfBooleans.contains(Strings.getStringByRId(R.string.fear_of_progression_questionnaire))) {
            Log.d(CLASS_NAME, "Adding Fear-of-Progression questionnaire to Listview on Start Screen");
            questionnairesList.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.fear_of_progression_questionnaire), fearProgress));
        }

        return questionnairesList;
    }

    private List<AbstractQuestionnaire> getDefaultQuestionnairesList(int hadsProgress, int distressProgress, int qualityProgress, int fearProgress) {
        List<AbstractQuestionnaire> list = new ArrayList<>();
        Log.i(CLASS_NAME, "display default questionnaires");
        list.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.hadsd_questionnaire), hadsProgress));
        list.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.nccn_distress_thermometer), distressProgress));
        list.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.quality_of_life_questionnaire), qualityProgress));
        list.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.fear_of_progression_questionnaire), fearProgress));
        return list;
    }

    private void addOnItemClickListener(final List<AbstractQuestionnaire> abstractQuestionnairesList) {
        if (questionnaireListView != null) {
            questionnaireListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String selectedQuestionnaireName = abstractQuestionnairesList.get(position).getName();
                    Log.i(CLASS_NAME, "selected questionnaire = " + selectedQuestionnaireName + " at position: " + position);
                    onStartQuestionnaire(selectedQuestionnaireName, abstractQuestionnairesList);
                }
            });
        }
    }

    /**
     * @param selectedQuestionnaireName
     * @param questionnairesList
     */
    private void onStartQuestionnaire(String selectedQuestionnaireName, List<AbstractQuestionnaire> questionnairesList) {
        String selectedUserName = Global.getSelectedUser();

        Date creationDate = Global.getSelectedQuestionnaireDate();

        Log.i(CLASS_NAME, "onStartQuestionnaire with selected user name = " + selectedUserName + ", selected date=" + EntityDbManager.dateFormat.format(creationDate));

        insertQuestionnaireList(questionnairesList, selectedUserName, creationDate);

        Class selectedClass = getWizardClassByQuestionnaireName(selectedQuestionnaireName);

        startActivityForResult(new Intent(getContext(), selectedClass), 0);
    }

    /**
     * Get the selected questionnaire class by questionnaire name
     *
     * @param selectedQuestionnaireName the selected questionnaire name
     * @return the class of the selected questionnaire
     */
    private Class getWizardClassByQuestionnaireName(String selectedQuestionnaireName) {
        Class ret = null;
        boolean isHadsdQuestionnaire = selectedQuestionnaireName.equalsIgnoreCase(Strings.getStringByRId(R.string.hadsd_questionnaire));
        if (isHadsdQuestionnaire) {
            ret = WizardHADSD.class;
        }
        boolean isDistressThermometerQuestionnaire = selectedQuestionnaireName.equalsIgnoreCase(Strings.getStringByRId(R.string.nccn_distress_thermometer));
        if (isDistressThermometerQuestionnaire) {
            ret = WizardNCCN.class;
        }
        boolean isQualityOfLifeQuestionnaire = selectedQuestionnaireName.equalsIgnoreCase(Strings.getStringByRId(R.string.quality_of_life_questionnaire));
        if (isQualityOfLifeQuestionnaire) {
            ret = WizardQualityOfLife.class;
        }
        boolean isFearOfProgressionQuestionnaire = selectedQuestionnaireName.equalsIgnoreCase(Strings.getStringByRId(R.string.fear_of_progression_questionnaire));
        if (isFearOfProgressionQuestionnaire) {
            ret = WizardFearOfProgression.class;
        }
        return ret;
    }

    /**
     * Inserts all questionnaires in the list into database if necessary
     *
     * @param questionnairesList list containing the questionnaires
     * @param selectedUserName   the users name
     * @param creationDate       the creation date of the questionnaires
     */
    private void insertQuestionnaireList(List<AbstractQuestionnaire> questionnairesList, String selectedUserName, Date creationDate) {

        Log.i(CLASS_NAME, "start creating new questionnaires");
        for (AbstractQuestionnaire item : questionnairesList) {

            boolean isHadsdQuestionnaire = item.getName().equalsIgnoreCase(Strings.getStringByRId(R.string.hadsd_questionnaire));
            if (isHadsdQuestionnaire) {
                HADSDQuestionnaire questionnaire = new HADSDQuestionnaireManager().getHADSDQuestionnaireByDate_PK(selectedUserName, creationDate);
                if (questionnaire == null) {
                    questionnaire = new HADSDQuestionnaire(selectedUserName);
                    questionnaire.setCreationDate_PK(creationDate);
                    new HADSDQuestionnaireManager().insertQuestionnaire(questionnaire);
                }
            }

            boolean isDistressThermometerQuestionnaire = item.getName().equalsIgnoreCase(Strings.getStringByRId(R.string.nccn_distress_thermometer));
            if (isDistressThermometerQuestionnaire) {
                DistressThermometerQuestionnaire questionnaire = new DistressThermometerQuestionnaireManager().getDistressThermometerQuestionnaireByDate(selectedUserName, creationDate);
                if (questionnaire == null) {
                    questionnaire = new DistressThermometerQuestionnaire(selectedUserName);
                    questionnaire.setCreationDate_PK(creationDate);
                    new DistressThermometerQuestionnaireManager().insertQuestionnaire(questionnaire);
                }
            }

            boolean isQualityOfLifeQuestionnaire = item.getName().equalsIgnoreCase(Strings.getStringByRId(R.string.quality_of_life_questionnaire));
            if (isQualityOfLifeQuestionnaire) {
                QolQuestionnaire questionnaire = new QualityOfLifeManager().getQolQuestionnaireByDate(selectedUserName, creationDate);
                if (questionnaire == null) {
                    questionnaire = new QolQuestionnaire(selectedUserName);
                    questionnaire.setCreationDate_PK(creationDate);
                    new QualityOfLifeManager().insertQuestionnaire(questionnaire);
                }

            }

            boolean isFearOfProgressionQuestionnaire = item.getName().equalsIgnoreCase(Strings.getStringByRId(R.string.fear_of_progression_questionnaire));
            if (isFearOfProgressionQuestionnaire) {
                FearOfProgressionQuestionnaire questionnaire = new FearOfProgressionManager().getQuestionnaireByDate(selectedUserName, creationDate);
                if (questionnaire == null) {
                    questionnaire = new FearOfProgressionQuestionnaire(selectedUserName);
                    questionnaire.setCreationDate_PK(creationDate);
                    new FearOfProgressionManager().insertQuestionnaire(questionnaire);
                }
            }
        }
        Global.setHasToCreateNewQuestionnaire(false);
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
            double sum = 0;

            double allQuestionsCount = 0;
            for (AbstractQuestionnaire item : list) {
                if (item.getName().equals(Strings.getStringByRId(R.string.hadsd_questionnaire))) {
                    allQuestionsCount += 14.;
                } else if (item.getName().equals(Strings.getStringByRId(R.string.nccn_distress_thermometer))) {
                    allQuestionsCount += 6.;
                } else if (item.getName().equals(Strings.getStringByRId(R.string.quality_of_life_questionnaire))) {
                    allQuestionsCount += 50.;
                } else if (item.getName().equals(Strings.getStringByRId(R.string.fear_of_progression_questionnaire))) {
                    allQuestionsCount += FearOfProgressionQuestionnaire.getQuestionCount();
                }
            }

            for (AbstractQuestionnaire item : list) {
                double factor = 1;
                if (item.getName().equals(Strings.getStringByRId(R.string.hadsd_questionnaire))) {
                    factor = 14. / allQuestionsCount;
                } else if (item.getName().equals(Strings.getStringByRId(R.string.nccn_distress_thermometer))) {
                    factor = 6. / allQuestionsCount;
                } else if (item.getName().equals(Strings.getStringByRId(R.string.quality_of_life_questionnaire))) {
                    factor = 50. / allQuestionsCount;
                } else if (item.getName().equals(Strings.getStringByRId(R.string.fear_of_progression_questionnaire))) {
                    factor = FearOfProgressionQuestionnaire.getQuestionCount() / allQuestionsCount;
                }

                sum += factor * item.getProgressInPercent();
            }

            if (count != 0) {
                return (int) Math.floor(sum);
            }
        }
        return 0;
    }

}
