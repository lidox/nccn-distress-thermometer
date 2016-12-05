package com.artursworld.nccn.model.persistence.manager;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.artursworld.nccn.controller.config.App;
import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;
import com.artursworld.nccn.model.persistence.contracts.DBContracts;

import java.util.ArrayList;
import java.util.List;

public class DistressThermometerQuestionnaireManager extends EntityDbManager {

    private String CLASS_NAME = DistressThermometerQuestionnaireManager.class.getSimpleName();

    public DistressThermometerQuestionnaireManager() {
        super(App.getAppContext());
    }

    /**
     * This constructor is used for unit tests
     * @param context the database context to use
     */
    DistressThermometerQuestionnaireManager(Context context) {
        super(context);
    }

    /**
     * Insert questionnaire into database
     *
     * @param questionnaire the questionnaire to insert
     */
    public void insertQuestionnaire(DistressThermometerQuestionnaire questionnaire) {
        if (questionnaire == null) {
            Log.e(CLASS_NAME,"the questionnaire to insert equals null!");
            return;
        }

        ContentValues values = getQuestionnaireContentValues(questionnaire);

        try {
            database.insertOrThrow(DBContracts.DistressThermometerTable.TABLE_NAME, null, values);
            Log.i(CLASS_NAME,"New questionnaire added successfully:" + questionnaire.toString());
        } catch (Exception e) {
            Log.e(CLASS_NAME,"Could not insert new questionnaire into db: " + questionnaire.toString() + "! " + e.getLocalizedMessage());
        }
    }

    /**
     * Get content values by questionnaire
     * @param questionnaire the questionnaire to get the values out
     * @return the ContentValues of the questionnaire
     */
    private ContentValues getQuestionnaireContentValues(DistressThermometerQuestionnaire questionnaire) {
        ContentValues values = new ContentValues();

        if(questionnaire.getUserNameId_FK() != null)
            values.put(DBContracts.DistressThermometerTable.NAME_ID_FK, questionnaire.getUserNameId_FK());

        if(questionnaire.getCreationDate_PK() != null)
            values.put(DBContracts.DistressThermometerTable.CREATION_DATE_PK, EntityDbManager.dateFormat.format(questionnaire.getCreationDate_PK()));

        if(questionnaire.getUpdateDate() != null)
            values.put(DBContracts.DistressThermometerTable.UPDATE_DATE, EntityDbManager.dateFormat.format(questionnaire.getUpdateDate()));

        if(questionnaire.getAnswersToQuestionsBytes() != null)
            values.put(DBContracts.DistressThermometerTable.ANSWERS_TO_QUESTIONS, questionnaire.getAnswersToQuestionsBytes());

        return values;
    }

    /**
     * Returns the questionnaire by users name from database
     *
     * @param name the users name
     * @return returns the Questionnaire by users name
     */
    public DistressThermometerQuestionnaire getDistressThermometerQuestionnaireByUserName(String name) {
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
                Log.i(CLASS_NAME,"Failed to parse date at getDistressThermometerQuestionnaireByUserName(" + name + ")!" + e.getLocalizedMessage());
            }

            questionnaire.setAnswersToQuestionsBytes(cursor.getBlob(3));
            medicalUserList.add(questionnaire);
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        if (medicalUserList.size() > 0) {
            return medicalUserList.get(0);
        } else {
            Log.e(CLASS_NAME,"Exception! Could not find DistressThermometerQuestionnaire by name(" + name + ") in database");
            return null;
        }
    }

    @NonNull
    private String[] getColumns() {
        return new String[]{DBContracts.DistressThermometerTable.CREATION_DATE_PK,
                DBContracts.DistressThermometerTable.NAME_ID_FK,
                DBContracts.DistressThermometerTable.UPDATE_DATE,
                DBContracts.DistressThermometerTable.ANSWERS_TO_QUESTIONS
        };
    }

    public void update(DistressThermometerQuestionnaire questionnaire) {
        String WHERE_CLAUSE = DBContracts.DistressThermometerTable.CREATION_DATE_PK + " =?";
        String[] WHERE_ARGS = new String[] {EntityDbManager.dateFormat.format(questionnaire.getCreationDate_PK())};
        
        if (questionnaire.getCreationDate_PK() == null) {
            Log.e(CLASS_NAME,"Cannot update questionnaire: " + questionnaire);
            return;
        }

        try {
            ContentValues contentValues = getQuestionnaireContentValues(questionnaire);
            database.update(DBContracts.DistressThermometerTable.TABLE_NAME, contentValues, WHERE_CLAUSE, WHERE_ARGS);
            Log.i(CLASS_NAME,questionnaire + " has been updated");
        } catch (Exception e) {
            Log.e(CLASS_NAME,"Exception! Could not update the questionnaire(" + questionnaire + ") " + " " + e.getLocalizedMessage());
        }
    }
}