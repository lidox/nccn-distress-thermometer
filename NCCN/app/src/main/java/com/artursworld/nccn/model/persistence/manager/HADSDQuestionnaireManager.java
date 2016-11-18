package com.artursworld.nccn.model.persistence.manager;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.artursworld.nccn.controller.config.App;
import com.artursworld.nccn.model.entity.HADSDQuestionnaire;
import com.artursworld.nccn.model.persistence.contracts.DBContracts;

import java.util.ArrayList;
import java.util.List;

public class HADSDQuestionnaireManager extends EntityDbManager {

    public HADSDQuestionnaireManager() {
        super(App.getAppContext());
    }

    /**
     * This constructor is used for unit tests
     * @param context the database context to use
     */
    public HADSDQuestionnaireManager(Context context) {
        super(context);
    }

    /**
     * Insert questionnaire into database
     *
     * @param questionnaire the questionnaire to insert
     */
    public void insertQuestionnaire(HADSDQuestionnaire questionnaire) {
        if (questionnaire == null) {
            Log.e(HADSDQuestionnaireManager.class.getSimpleName(),"the questionnaire to insert equals null!");
            return;
        }

        ContentValues values = getQuestionnaireContentValues(questionnaire);

        try {
            database.insertOrThrow(DBContracts.HADSDTable.TABLE_NAME, null, values);
            Log.i(HADSDQuestionnaireManager.class.getSimpleName(),"New questionnaire added successfully:" + questionnaire.toString());
        } catch (Exception e) {
            Log.e(HADSDQuestionnaireManager.class.getSimpleName(),"Could not insert new questionnaire into db: " + questionnaire.toString() + "! " + e.getLocalizedMessage());
        }
    }

    private ContentValues getQuestionnaireContentValues(HADSDQuestionnaire questionnaire) {
        int byteSize = 3;
        ContentValues values = new ContentValues();

        if(questionnaire.getUserNameId_FK() != null)
            values.put(DBContracts.HADSDTable.NAME_ID_FK, questionnaire.getUserNameId_FK());

        if(questionnaire.getCreationDate_PK() != null)
            values.put(DBContracts.HADSDTable.CREATION_DATE_PK, EntityDbManager.dateFormat.format(questionnaire.getCreationDate_PK()));

        if(questionnaire.getUpdateDate() != null)
            values.put(DBContracts.HADSDTable.UPDATE_DATE, EntityDbManager.dateFormat.format(questionnaire.getUpdateDate()));

        if(questionnaire.getAnswerToQuestionList().get(0) == null)
            values.put(DBContracts.HADSDTable.ANSWER_TO_QUESTION1, new byte[byteSize]);
        else
            values.put(DBContracts.HADSDTable.ANSWER_TO_QUESTION1, questionnaire.getAnswerToQuestionList().get(0));

        if(questionnaire.getAnswerToQuestionList().get(1) == null)
            values.put(DBContracts.HADSDTable.ANSWER_TO_QUESTION2, new byte[byteSize]);
        else
            values.put(DBContracts.HADSDTable.ANSWER_TO_QUESTION2, questionnaire.getAnswerToQuestionList().get(1));

        //TODO: so on...
        return values;
    }

    /**
     * Returns the questionnaire by users name from database
     *
     * @param name the users name
     * @return returns the Questionnaire by users name
     */
    public HADSDQuestionnaire getHADSDQuestionnaireByUserName(String name) {
        List<HADSDQuestionnaire> medicalUserList = new ArrayList<HADSDQuestionnaire>();
        Cursor cursor = database.query(DBContracts.HADSDTable.TABLE_NAME,
                getColumns(),
                DBContracts.HADSDTable.NAME_ID_FK + " LIKE '" + name + "'",
                null, null, null, null);

        while (cursor.moveToNext()) {
            HADSDQuestionnaire questionnaire = new HADSDQuestionnaire(cursor.getString(1));
            try {
                questionnaire.setCreationDate_PK(EntityDbManager.dateFormat.parse(cursor.getString(0)));
                questionnaire.setUpdateDate(EntityDbManager.dateFormat.parse(cursor.getString(2)));
            } catch (Exception e) {
                Log.i(HADSDQuestionnaireManager.class.getSimpleName(),"Failed to parse date at getHADSDQuestionnaireByUserName(" + name + ")!" + e.getLocalizedMessage());
            }

            questionnaire.setAnswerByNr(0, cursor.getBlob(3));
            questionnaire.setAnswerByNr(1, cursor.getBlob(4));
            //questionnaire.addAnswerToQuestion(cursor.getBlob(3));
           // questionnaire.addAnswerToQuestion(cursor.getBlob(4));
            // TODO: so on
            medicalUserList.add(questionnaire);
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        if (medicalUserList.size() > 0) {
            return medicalUserList.get(0);
        } else {
            Log.e(HADSDQuestionnaireManager.class.getSimpleName(),"Exception! Could not find HADSDQuestionnaire by name(" + name + ") in database");
            return null;
        }
    }

    @NonNull
    private String[] getColumns() {
        return new String[]{DBContracts.HADSDTable.CREATION_DATE_PK,
                DBContracts.HADSDTable.NAME_ID_FK,
                DBContracts.HADSDTable.UPDATE_DATE,
                DBContracts.HADSDTable.ANSWER_TO_QUESTION1,
                DBContracts.HADSDTable.ANSWER_TO_QUESTION2
        };
    }

    public void update(HADSDQuestionnaire questionnaire) {
        String WHERE_CLAUSE = DBContracts.HADSDTable.CREATION_DATE_PK + " =?";
        String[] WHERE_ARGS = new String[] {EntityDbManager.dateFormat.format(questionnaire.getCreationDate_PK())};

        if (questionnaire.getCreationDate_PK() == null) {
            Log.e(HADSDQuestionnaireManager.class.getSimpleName(),"Cannot update questionnaire: " + questionnaire);
            return;
        }

        try {
            ContentValues contentValues = getQuestionnaireContentValues(questionnaire);
            database.update(DBContracts.HADSDTable.TABLE_NAME, contentValues, WHERE_CLAUSE, WHERE_ARGS);
            Log.i(HADSDQuestionnaireManager.class.getSimpleName(),questionnaire + " has been updated");
        } catch (Exception e) {
            Log.e(HADSDQuestionnaireManager.class.getSimpleName(),"Exception! Could not update the questionnaire(" + questionnaire + ") " + " " + e.getLocalizedMessage());
        }
    }
}
