package com.artursworld.nccn.view.questionnaire;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.util.Global;
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.controller.wizard.WizardHADSD;
import com.artursworld.nccn.controller.wizard.WizardNCCN;
import com.artursworld.nccn.controller.wizard.WizardQualityOfLife;
import com.artursworld.nccn.model.entity.AbstractQuestionnaire;
import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;
import com.artursworld.nccn.model.entity.HADSDQuestionnaire;
import com.artursworld.nccn.model.entity.MetaQuestionnaire;
import com.artursworld.nccn.model.entity.PsychoSocialSupportState;
import com.artursworld.nccn.model.entity.QolQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.DistressThermometerQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.EntityDbManager;
import com.artursworld.nccn.model.persistence.manager.HADSDQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.MetaQuestionnaireManager;
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(CLASS_NAME, "onResume()");
        showQuestionnairesInListView();
    }

    /**
     * Displays the questionnaires containing progress in percentage
     */
    private void showQuestionnairesInListView() {
        boolean userExisting = Global.getSelectedUser() != null;
        if (userExisting) {

            Date newCreationDate = Global.getSelectedQuestionnaireDate();
            if (Global.hasToCreateNewQuestionnaire()) {
                Log.i(CLASS_NAME, "New questionnaires has to be created.");
                // create new user
                User user = new User(Strings.getStringByRId(R.string.user_name));

                fillQuestionnaireListView(user, newCreationDate);
            } else if(Global.getSelectedQuestionnaireDate() != null){
                //TODO: async
                Log.i(CLASS_NAME, "Questionnaires already exists. So show questionnaires in ListView");
                User user = new UserManager().getUserByName(Global.getSelectedUser());
                if (user != null) {
                    fillQuestionnaireListView(user, Global.getSelectedQuestionnaireDate());
                }
            }
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
        List<AbstractQuestionnaire> list = new ArrayList<>();

        int hadsProgress = 0;
        int distressProgress = 0;
        int qualityProgress = 0;


        if (Global.hasToCreateNewQuestionnaire()) {
            if (Global.hasToUseDefaultQuestionnaire()) {
                displayDefaultQuestionnaires(list, hadsProgress, distressProgress, qualityProgress);
            } else {
                Set<String> setOfBooleans = Global.getSelectedQuestionnairesForStartScreen();
                if (setOfBooleans != null) {
                    if (setOfBooleans.contains(Strings.getStringByRId(R.string.hadsd_questionnaire)))
                        list.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.hadsd_questionnaire), hadsProgress));

                    if (setOfBooleans.contains(Strings.getStringByRId(R.string.nccn_distress_thermometer)))
                        list.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.nccn_distress_thermometer), distressProgress));

                    if (setOfBooleans.contains(Strings.getStringByRId(R.string.quality_of_life_questionnaire)))
                        list.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.quality_of_life_questionnaire), qualityProgress));
                }
                else{
                    displayDefaultQuestionnaires(list, hadsProgress, distressProgress, qualityProgress);
                }
            }
        } else {
            Set<String> setOfBooleans = Global.getSelectedQuestionnairesForStartScreen();
            if (setOfBooleans != null) {
                boolean hasToGetProgress = !Global.hasToCreateNewUser() && user != null;

                // HADS-D
                if (setOfBooleans.contains(Strings.getStringByRId(R.string.hadsd_questionnaire))) {
                    if (hasToGetProgress) {
                        HADSDQuestionnaire hadsdQuestionnaire = new HADSDQuestionnaireManager().getHADSDQuestionnaireByDate_PK(user.getName(), creationDate);
                        if (hadsdQuestionnaire != null) {
                            hadsProgress = hadsdQuestionnaire.getProgressInPercent();
                            Log.i(CLASS_NAME, "Progress loading HADS-D = " + hadsProgress);
                        }
                    }
                    list.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.hadsd_questionnaire), hadsProgress));
                }

                // Distress Thermometer
                if (setOfBooleans.contains(Strings.getStringByRId(R.string.nccn_distress_thermometer))) {
                    if (hasToGetProgress) {
                        DistressThermometerQuestionnaire distressThermometerQuestionnaire = new DistressThermometerQuestionnaireManager().getDistressThermometerQuestionnaireByDate(user.getName(), creationDate);
                        if (distressThermometerQuestionnaire != null)
                            distressProgress = distressThermometerQuestionnaire.getProgressInPercent();
                    }
                    list.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.nccn_distress_thermometer), distressProgress));
                }

                if (setOfBooleans.contains(Strings.getStringByRId(R.string.quality_of_life_questionnaire))) {
                    if (hasToGetProgress) {
                        QolQuestionnaire qolQuestionnaire = new QualityOfLifeManager().getQolQuestionnaireByDate(user.getName(), creationDate);
                        if (qolQuestionnaire != null) {
                            qualityProgress = qolQuestionnaire.getProgressInPercent();
                            Log.i(CLASS_NAME, "Progress loading QualityOfLife = " + qualityProgress);
                        }
                    }
                    list.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.quality_of_life_questionnaire), qualityProgress));
                }
            }
            else{
                Log.w(CLASS_NAME, "something went wrong");
            }
        }
        AbstractQuestionnaireItemAdapter adapter = new AbstractQuestionnaireItemAdapter(getActivity(), list);
        questionnaireListView.setAdapter(adapter);
        int percentageForAllQuestionnaires = getPercentageForAllQuestionnairesByList(list);
        askForProfessionalPsychosocialSupport(percentageForAllQuestionnaires);
        percentageAllTextView.setText(percentageForAllQuestionnaires + " ");
        addOnItemClickListener(list);
    }

    private void askForProfessionalPsychosocialSupport(int percentageForAllQuestionnaires) {
        //TODO:
        Log.i(CLASS_NAME, "ask for Professional Psychosocial Support");
        boolean hasNotAskedYet = true;
        if(percentageForAllQuestionnaires == 100  && hasNotAskedYet){
            MaterialDialog b = new MaterialDialog.Builder(this.getActivity())
                    .title(R.string.wish_professional_psychosocial_support)
                    .positiveText(R.string.yes)
                    .negativeText(R.string.no)
                    .customView(R.layout.dialog_select_questionnairies, true)
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            setPsychoSocialSupportState(PsychoSocialSupportState.REJECTED);
                        }
                    })
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            setPsychoSocialSupportState(PsychoSocialSupportState.ACCEPTED);

                        }
                    }).build();
        }
    }

    private void setPsychoSocialSupportState(final PsychoSocialSupportState supportState) {
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                MetaQuestionnaireManager db = new MetaQuestionnaireManager();
                MetaQuestionnaire meta = db.getMetaDataByCreationDate(Global.getSelectedQuestionnaireDate());
                meta.setPsychoSocialSupportState(supportState);
                if(meta!=null)
                    db.update(meta);
                return null;
            }
        }.execute();
    }

    private void displayDefaultQuestionnaires(List<AbstractQuestionnaire> list, int hadsProgress, int distressProgress, int qualityProgress) {
        Log.i(CLASS_NAME, "display default questionnaires");
        list.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.hadsd_questionnaire), hadsProgress));
        list.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.nccn_distress_thermometer), distressProgress));
        list.add(new AbstractQuestionnaire(Strings.getStringByRId(R.string.quality_of_life_questionnaire), qualityProgress));
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
        boolean hasToCreateNewQuestionnaire = Global.hasToCreateNewQuestionnaire();
        Date creationDate = Global.getSelectedQuestionnaireDate();

        Log.i(CLASS_NAME, "onStartQuestionnaire with selected user name = " + selectedUserName + ", selected date=" + EntityDbManager.dateFormat.format(creationDate));

        insertQuestionnaireList(questionnairesList, hasToCreateNewQuestionnaire, selectedUserName, creationDate);

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
        return ret;
    }

    /**
     * Inserts all questionnaires in the list into database if necessary
     *
     * @param questionnairesList          list containing the questionnaires
     * @param hasToCreateNewQuestionnaire only inserts if necessary
     * @param selectedUserName            the users name
     * @param creationDate                the creation date of the questionnaires
     */
    private void insertQuestionnaireList(List<AbstractQuestionnaire> questionnairesList, boolean hasToCreateNewQuestionnaire, String selectedUserName, Date creationDate) {
        //TODO: async
        if (hasToCreateNewQuestionnaire) {
            Log.i(CLASS_NAME, "start creating new questionnaires");
            for (AbstractQuestionnaire item : questionnairesList) {
                boolean isHadsdQuestionnaire = item.getName().equalsIgnoreCase(Strings.getStringByRId(R.string.hadsd_questionnaire));
                if (isHadsdQuestionnaire) {
                    HADSDQuestionnaire questionnaire = new HADSDQuestionnaire(selectedUserName);
                    questionnaire.setCreationDate_PK(creationDate);
                    new HADSDQuestionnaireManager().insertQuestionnaire(questionnaire);
                }
                boolean isDistressThermometerQuestionnaire = item.getName().equalsIgnoreCase(Strings.getStringByRId(R.string.nccn_distress_thermometer));
                if (isDistressThermometerQuestionnaire) {
                    DistressThermometerQuestionnaire questionnaire = new DistressThermometerQuestionnaire(selectedUserName);
                    questionnaire.setCreationDate_PK(creationDate);
                    new DistressThermometerQuestionnaireManager().insertQuestionnaire(questionnaire);
                }
                boolean isQualityOfLifeQuestionnaire = item.getName().equalsIgnoreCase(Strings.getStringByRId(R.string.quality_of_life_questionnaire));
                if (isQualityOfLifeQuestionnaire) {
                    QolQuestionnaire questionnaire = new QolQuestionnaire(selectedUserName);
                    questionnaire.setCreationDate_PK(creationDate);
                    new QualityOfLifeManager().insertQuestionnaire(questionnaire);
                }
            }
            Global.setHasToCreateNewQuestionnaire(false);
        }
        else{
            Log.i(CLASS_NAME, "does NOT create new questionnaires, because does not have to do it.");
        }
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
            for (AbstractQuestionnaire item : list) {
                double factor = 1;
                if (item.getName().equals(Strings.getStringByRId(R.string.hadsd_questionnaire))) {
                    factor = 14. / 70;
                } else if (item.getName().equals(Strings.getStringByRId(R.string.nccn_distress_thermometer))) {
                    factor = 6. / 70;
                } else if (item.getName().equals(Strings.getStringByRId(R.string.quality_of_life_questionnaire))) {
                    factor = 50. / 70;
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
