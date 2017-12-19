package com.artursworld.nccn.model.persistence.manager;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.artursworld.nccn.controller.config.App;
import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;
import com.artursworld.nccn.model.entity.HADSDQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.contracts.DBContracts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DistressThermometerQuestionnaireManager extends EntityDbManager {

    private String CLASS_NAME = DistressThermometerQuestionnaireManager.class.getSimpleName();

    public DistressThermometerQuestionnaireManager() {
        super(App.getAppContext());
    }

    /**
     * This constructor is used for unit tests
     *
     * @param context the database context to use
     */
    public DistressThermometerQuestionnaireManager(Context context) {
        super(context);
    }

    /**
     * Insert questionnaire into database
     *
     * @param questionnaire the questionnaire to insert
     */
    public void insertQuestionnaire(DistressThermometerQuestionnaire questionnaire) {
        if (questionnaire == null) {
            Log.e(CLASS_NAME, "the questionnaire to insert equals null!");
            return;
        }

        ContentValues values = getQuestionnaireContentValues(questionnaire);

        try {
            database.insertOrThrow(DBContracts.DistressThermometerTable.TABLE_NAME, null, values);
            Log.i(CLASS_NAME, "New questionnaire added successfully:" + questionnaire.toString());
        } catch (Exception e) {
            Log.e(CLASS_NAME, "Could not insert new questionnaire into db: " + questionnaire.toString() + "! " + e.getLocalizedMessage());
        }
    }

    /**
     * Get content values by questionnaire
     *
     * @param questionnaire the questionnaire to get the values out
     * @return the ContentValues of the questionnaire
     */
    private ContentValues getQuestionnaireContentValues(DistressThermometerQuestionnaire questionnaire) {
        ContentValues values = new ContentValues();

        if (questionnaire.getUserNameId_FK() != null)
            values.put(DBContracts.DistressThermometerTable.NAME_ID_FK, questionnaire.getUserNameId_FK());

        if (questionnaire.getCreationDate_PK() != null)
            values.put(DBContracts.DistressThermometerTable.CREATION_DATE_PK, EntityDbManager.dateFormat.format(questionnaire.getCreationDate_PK()));

        if (questionnaire.getUpdateDate() != null)
            values.put(DBContracts.DistressThermometerTable.UPDATE_DATE, EntityDbManager.dateFormat.format(questionnaire.getUpdateDate()));

        if (questionnaire.getAnswersToQuestionsBytes() != null)
            values.put(DBContracts.DistressThermometerTable.ANSWERS_TO_QUESTIONS, questionnaire.getAnswersToQuestionsBytes());

        if (questionnaire.getLastQuestionEditedNr() >= 0)
            values.put(DBContracts.DistressThermometerTable.LAST_QEUSTION_EDITED_NR, questionnaire.getLastQuestionEditedNr());

        if (questionnaire.getProgressInPercent() >= 0)
            values.put(DBContracts.DistressThermometerTable.PROGRESS, questionnaire.getProgressInPercent());

        return values;
    }

    /**
     * Returns the questionnaire by users name from database
     *
     * @param name the users name
     * @return returns the Questionnaire by users name
     */
    public DistressThermometerQuestionnaire getDistressThermometerQuestionnaireByUserName(String name) {
        List<DistressThermometerQuestionnaire> medicalUserList = getDistressThermometerQuestionnaireList(name);

        if (medicalUserList.size() > 0) {
            return medicalUserList.get(0);
        } else {
            Log.e(CLASS_NAME, "Exception! Could not find DistressThermometerQuestionnaire by name(" + name + ") in database");
            return null;
        }
    }

    @NonNull
    public List<DistressThermometerQuestionnaire> getDistressThermometerQuestionnaireList(String name) {
        List<DistressThermometerQuestionnaire> medicalUserList = new ArrayList<>();
        Cursor cursor = database.query(DBContracts.DistressThermometerTable.TABLE_NAME,
                getColumns(),
                DBContracts.DistressThermometerTable.NAME_ID_FK + " LIKE '" + name + "'",
                null, null, null, null);

        while (cursor.moveToNext()) {
            DistressThermometerQuestionnaire questionnaire = new DistressThermometerQuestionnaire(cursor.getString(1));

            try {
                questionnaire.setCreationDate_PK(EntityDbManager.dateFormat.parse(cursor.getString(0)));
                questionnaire.setUpdateDate(EntityDbManager.dateFormat.parse(cursor.getString(2)));
            } catch (Exception e) {
                Log.i(CLASS_NAME, "Failed to parse date at getDistressThermometerQuestionnaireByUserName(" + name + ")!" + e.getLocalizedMessage());
            }

            questionnaire.setAnswersToQuestionsBytes(cursor.getBlob(3));
            questionnaire.setLastQuestionEditedNr(cursor.getInt(4));
            questionnaire.setProgressInPercent(cursor.getInt(5));
            medicalUserList.add(questionnaire);
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return medicalUserList;
    }

    @NonNull
    private String[] getColumns() {
        return new String[]{DBContracts.DistressThermometerTable.CREATION_DATE_PK,
                DBContracts.DistressThermometerTable.NAME_ID_FK,
                DBContracts.DistressThermometerTable.UPDATE_DATE,
                DBContracts.DistressThermometerTable.ANSWERS_TO_QUESTIONS,
                DBContracts.DistressThermometerTable.LAST_QEUSTION_EDITED_NR,
                DBContracts.DistressThermometerTable.PROGRESS
        };
    }

    public void update(DistressThermometerQuestionnaire questionnaire) {
        String WHERE_CLAUSE = DBContracts.DistressThermometerTable.CREATION_DATE_PK + " =?";
        String[] WHERE_ARGS = new String[]{EntityDbManager.dateFormat.format(questionnaire.getCreationDate_PK())};

        if (questionnaire.getCreationDate_PK() == null) {
            Log.e(CLASS_NAME, "Cannot update questionnaire: " + questionnaire);
            return;
        }

        try {
            ContentValues contentValues = getQuestionnaireContentValues(questionnaire);
            database.update(DBContracts.DistressThermometerTable.TABLE_NAME, contentValues, WHERE_CLAUSE, WHERE_ARGS);
            Log.i(CLASS_NAME, questionnaire + " has been updated");
        } catch (Exception e) {
            Log.e(CLASS_NAME, "Exception! Could not update the questionnaire(" + questionnaire + ") " + " " + e.getLocalizedMessage());
        }
    }

    /**
     * Get questionnaire by username and creation date
     *
     * @param userName the users name
     * @param date     the questionnaires creation date
     * @return the  Distress Thermometer questionnaire by username and creation date
     */
    public DistressThermometerQuestionnaire getDistressThermometerQuestionnaireByDate(String userName, Date date) {
        List<DistressThermometerQuestionnaire> questionnaireList = getDistressThermometerQuestionnaireList(userName);
        SimpleDateFormat format = EntityDbManager.dateFormat;
        for (DistressThermometerQuestionnaire item : questionnaireList) {
            if (format.format(item.getCreationDate_PK()).equals(format.format(date))) {
                return item;
            }
        }
        return null;
    }

    /**
     * Get 'Distress thermometer' values by user
     *
     * @param user the selected user
     * @param ctx  the database context
     * @return information about 'Distress thermometer' questionnaire values
     */
    @NonNull
    public List<String[]> getDistressThermometerValues(User user, Context ctx) {
        List<String[]> retList = new ArrayList<>();

        List<DistressThermometerQuestionnaire> list = getDistressThermometerQuestionnaireList(user.getName());

        // 'Quality Of Life' questionnaire information
        for (int i = 0; i < list.size(); i++) {

            // values to return
            int valueCount = 2;
            String[] csvRecordRow = new String[valueCount];

            csvRecordRow[0] = list.get(i).getUpdateDate().toString();
            csvRecordRow[1] = list.get(i).getDistressScore() + "";

            retList.add(csvRecordRow);
        }

        return retList;
    }
}
