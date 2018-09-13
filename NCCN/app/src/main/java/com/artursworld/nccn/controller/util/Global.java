package com.artursworld.nccn.controller.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.config.App;
import com.artursworld.nccn.model.persistence.manager.EntityDbManager;

import java.util.Date;
import java.util.HashSet;
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
            if(Share.getStringByKey(R.string.c_selected_questionnaire_creation_date) == null){
                Log.i(CLASS_NAME, "first time app has been started. so new creation date");
                try {
                    String stringId = Strings.getStringByRId(R.string.c_selected_questionnaire_creation_date);
                    String dateAsString = EntityDbManager.dateFormat.format(new Date());
                    Share.putString(stringId, dateAsString);
                    Global.setHasToUseDefaultQuestionnaire(true);
                    Set<String> selectedQuestionnairesSet = new HashSet<>();
                    selectedQuestionnairesSet.add(Strings.getStringByRId(R.string.hadsd_questionnaire));
                    selectedQuestionnairesSet.add(Strings.getStringByRId(R.string.nccn_distress_thermometer));
                    selectedQuestionnairesSet.add(Strings.getStringByRId(R.string.quality_of_life_questionnaire));
                    Log.i(CLASS_NAME, "Following questionnaires are selected to be displayed on start screen=" + selectedQuestionnairesSet);
                    Share.putStringSet(Strings.getStringByRId(R.string.c_selected_questionnaires), selectedQuestionnairesSet);
                }
                catch (Exception ex){
                    Log.e(CLASS_NAME, ex.getLocalizedMessage());
                }
            }
            Log.w(CLASS_NAME, "date could not be parsed so use default date");
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
        Log.i(CLASS_NAME, "setSelectedUserName(" + selectedUserName +")");
    }

    public static void setSelectedQuestionnaireDate(Date newCreationDate) {
        try {
            String stringId = Strings.getStringByRId(R.string.c_selected_questionnaire_creation_date);
            String dateAsString = EntityDbManager.dateFormat.format(newCreationDate);
            Log.i(CLASS_NAME, "CREATION Q-DATE changed to: " + dateAsString + " (old value="+ EntityDbManager.dateFormat.format(getSelectedQuestionnaireDate()) + ")");
            Share.putString(stringId, dateAsString);
                    }
        catch (Exception e){
            Log.e(CLASS_NAME, e.getLocalizedMessage());
        }
    }

    public static Set<String> getSelectedQuestionnairesForStartScreen() {
        return Share.getStringSetByKey(R.string.c_selected_questionnaires);
    }

    public static void setHasToCreateNewUser(boolean hasToCreateNewUser) {
        if(hasToCreateNewUser){
            Log.e(CLASS_NAME, "Create New User ON");
        }
        else {
            Log.e(CLASS_NAME, "Create New User OFF");
        }
        Share.putBoolean(Strings.getStringByRId(R.string.c_create_new_user), hasToCreateNewUser);
    }

    public static boolean hasToCreateNewUser() {
        return Share.getBooleanByKey(R.string.c_create_new_user);
    }

    public static void setHasToUseDefaultQuestionnaire(boolean hasToUseDefaultQuestionnaire) {
        if(hasToUseDefaultQuestionnaire){
            Log.e(CLASS_NAME, "Default Questions ON");
        }
        else {
            Log.e(CLASS_NAME, "Default Questions OFF");
        }

        Share.putBoolean(Strings.getStringByRId(R.string.c_has_to_create_default_questionnaire), hasToUseDefaultQuestionnaire);
    }

    public static boolean hasToUseDefaultQuestionnaire() {
        return Share.getBooleanByKey(R.string.c_has_to_create_default_questionnaire);
    }

    public static boolean hasToCreateNewQuestionnaire() {
        return Share.getBooleanByKey(R.string.c_has_to_create_new_questionnaire);
    }

    public static void setHasToCreateNewQuestionnaire(boolean setHasToCreateNewQuestionnaire) {
        Log.i(CLASS_NAME, "setHasToCreateNewQuestionnaire(" + setHasToCreateNewQuestionnaire +")");
        Share.putBoolean(Strings.getStringByRId(R.string.c_has_to_create_new_questionnaire), setHasToCreateNewQuestionnaire);
    }


    /**
     * Sets the selected user name in order to show statistics
     * @param userName the users name to show statistics
     * @return True on success to set user name. Otherwise false.
     */
    public static boolean setSelectedStatisticsUser(String userName) {
        return  Share.putString(Strings.getStringByRId(R.string.c_selected_statisc_user_name), userName);
    }

    /**
     * Get the selected user name in order to show statistics
     * @return the user name. Otherwise null.
     */
    public static String getSelectedStatisticUser() {
        return Share.getStringByKey(R.string.c_selected_statisc_user_name);
    }

    /**
     * Sets the pin code to access some UIs
     * @param pinCode the new pin to set
     * @return True on success to set pin code. Otherwise false.
     */
    public static boolean setPinCode(String pinCode) {
        return Share.putString(Strings.getStringByRId(R.string.c_pin_code), pinCode);
    }

    /**
     * Get the the pin code
     * @return the pin code. Otherwise 000000.
     */
    public static String getPinCode() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.getAppContext());
        String key = App.getAppContext().getResources().getString(R.string.c_pin_code);
        String restoredText = prefs.getString(key, Strings.getStringByRId(R.string.c_pin_code_default_value));
        return restoredText;
    }

    /**
     * Check if encryption is enabled
     * @return True if encryption is enabled. Otherwise false.
     */
    public static boolean isEncryptionEnabled() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.getAppContext());
        String key = App.getAppContext().getResources().getString(R.string.c_enable_encryption);
        boolean isEncryptionEnabled = prefs.getBoolean(key, true);
        return isEncryptionEnabled;
    }

    /**
     * Enables or disables encryption
     * @param isEncryptionEnabled
     */
    public static void setEncryptionEnabled(boolean isEncryptionEnabled) {
        Share.putBoolean(Strings.getStringByRId(R.string.c_enable_encryption), isEncryptionEnabled);
    }

}
