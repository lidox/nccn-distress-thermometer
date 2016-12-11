package com.artursworld.nccn.controller.util;

import android.util.Log;

import com.artursworld.nccn.R;
import com.artursworld.nccn.model.persistence.manager.EntityDbManager;

import java.util.Date;
import java.util.Set;


/**
 * Class to access global values from everywhere
 */
public class Global {

    private static String CLASS_NAME = Global.class.getSimpleName();

    /**
     * Get the selected questionnaire date which is PK for all
     * questionnaires in a context (e.g. pre-operation context)
     * @return the date of the selected questionnaire context
     */
    public static Date getSelectedQuestionnaireDate() {

        Date selectedQuestionnaireDate = null;
        try {
            selectedQuestionnaireDate = EntityDbManager.dateFormat.parse(Share.getStringByKey(R.string.c_selected_questionnaire_creation_date));
        } catch (Exception e) {
            Log.i(CLASS_NAME, "date could not be parsed so use default date");
        }
        return selectedQuestionnaireDate;
    }

    /**
     * Get the selected user
     * @return the selected user
     */
    public static String getSelectedUser() {
        return Share.getStringByKey(R.string.c_selected_user_name);
    }

    /*
     * Sets the selected user name
     * @param selectedUserName the users name
     */
    public static void setSelectedUserName(String selectedUserName) {
        Share.putString(Strings.getStringByRId(R.string.c_selected_user_name), selectedUserName);
    }

    public static void setSelectedQuestionnaireDate(Date newCreationDate) {
        Share.putString(Strings.getStringByRId(R.string.c_selected_questionnaire_creation_date), EntityDbManager.dateFormat.format(newCreationDate));
    }

    public static Set<String> getSelectedQuestionnairesForStartScreen() {
        return Share.getStringSetByKey(R.string.c_selected_questionnaires);
    }
}
