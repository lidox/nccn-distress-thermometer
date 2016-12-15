package com.artursworld.nccn.model.persistence.manager;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.artursworld.nccn.controller.config.App;
import com.artursworld.nccn.model.entity.HADSDQuestionnaire;
import com.artursworld.nccn.model.persistence.contracts.DBContracts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        ContentValues values = new ContentValues();

        if(questionnaire.getUserNameId_FK() != null)
            values.put(DBContracts.HADSDTable.NAME_ID_FK, questionnaire.getUserNameId_FK());

        if(questionnaire.getCreationDate_PK() != null)
            values.put(DBContracts.HADSDTable.CREATION_DATE_PK, EntityDbManager.dateFormat.format(questionnaire.getCreationDate_PK()));

        if(questionnaire.getUpdateDate() != null)
            values.put(DBContracts.HADSDTable.UPDATE_DATE, EntityDbManager.dateFormat.format(questionnaire.getUpdateDate()));


        if(questionnaire.getLastQuestionEditedNr() >= 0)
            values.put(DBContracts.HADSDTable.LAST_QEUSTION_EDITED_NR, questionnaire.getLastQuestionEditedNr());

        if(questionnaire.getProgressInPercent() >= 0)
            values.put(DBContracts.HADSDTable.PROGRESS, questionnaire.getProgressInPercent());


        values = addAnswerToQuestion(questionnaire, values, 0, DBContracts.HADSDTable.ANSWER_TO_QUESTION1);
        values = addAnswerToQuestion(questionnaire, values, 1, DBContracts.HADSDTable.ANSWER_TO_QUESTION2);
        values = addAnswerToQuestion(questionnaire, values, 2, DBContracts.HADSDTable.ANSWER_TO_QUESTION3);
        values = addAnswerToQuestion(questionnaire, values, 3, DBContracts.HADSDTable.ANSWER_TO_QUESTION4);
        values = addAnswerToQuestion(questionnaire, values, 4, DBContracts.HADSDTable.ANSWER_TO_QUESTION5);
        values = addAnswerToQuestion(questionnaire, values, 5, DBContracts.HADSDTable.ANSWER_TO_QUESTION6);
        values = addAnswerToQuestion(questionnaire, values, 6, DBContracts.HADSDTable.ANSWER_TO_QUESTION7);
        values = addAnswerToQuestion(questionnaire, values, 7, DBContracts.HADSDTable.ANSWER_TO_QUESTION8);
        values = addAnswerToQuestion(questionnaire, values, 8, DBContracts.HADSDTable.ANSWER_TO_QUESTION9);
        values = addAnswerToQuestion(questionnaire, values, 9, DBContracts.HADSDTable.ANSWER_TO_QUESTION10);
        values = addAnswerToQuestion(questionnaire, values, 10, DBContracts.HADSDTable.ANSWER_TO_QUESTION11);
        values = addAnswerToQuestion(questionnaire, values, 11, DBContracts.HADSDTable.ANSWER_TO_QUESTION12);
        values = addAnswerToQuestion(questionnaire, values, 12, DBContracts.HADSDTable.ANSWER_TO_QUESTION13);
        values = addAnswerToQuestion(questionnaire, values, 13, DBContracts.HADSDTable.ANSWER_TO_QUESTION14);
        return values;
    }

    private ContentValues addAnswerToQuestion(HADSDQuestionnaire questionnaire, ContentValues values, int questionIndex, String attribute){
        if(questionnaire.getAnswerToQuestionList().get(questionIndex) == null)
            values.put(attribute, questionnaire.getDefaultByte());
        else
            values.put(attribute, questionnaire.getAnswerByNr(questionIndex));

        return values;
    }

    /**
     * Returns the questionnaire by users name from database
     *
     * @param name the users name
     * @return returns the Questionnaire by users name
     */
    public HADSDQuestionnaire getHADSDQuestionnaireByUserName(String name) {
        List<HADSDQuestionnaire> medicalUserList = getHadsdQuestionnaireListByUserName(name);

        if (medicalUserList.size() > 0) {
            return medicalUserList.get(0);
        } else {
            Log.e(HADSDQuestionnaireManager.class.getSimpleName(),"Exception! Could not find HADSDQuestionnaire by name(" + name + ") in database");
            return null;
        }
    }

    /**
     * Get questionnaire by username and creation date
     * @param userName the users name
     * @param date the questionnaires creation date
     * @return the HADSD questionnaire by username and creation date
     */
    public HADSDQuestionnaire getHADSDQuestionnaireByDate_PK(String userName, Date date){
        List<HADSDQuestionnaire> questionnaireList = getHadsdQuestionnaireListByUserName(userName);
        SimpleDateFormat format = EntityDbManager.dateFormat;
        for (HADSDQuestionnaire item: questionnaireList){
            if(format.format(item.getCreationDate_PK()).equals(format.format(date))) {
                return item;
            }
        }
        return null;
    }

    @NonNull
    public List<HADSDQuestionnaire> getHadsdQuestionnaireListByUserName(String name) {
        List<HADSDQuestionnaire> medicalUserList = new ArrayList<>();
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
            questionnaire.setAnswerByNr(2, cursor.getBlob(5));
            questionnaire.setAnswerByNr(3, cursor.getBlob(6));
            questionnaire.setAnswerByNr(4, cursor.getBlob(7));
            questionnaire.setAnswerByNr(5, cursor.getBlob(8));
            questionnaire.setAnswerByNr(6, cursor.getBlob(9));
            questionnaire.setAnswerByNr(7, cursor.getBlob(10));
            questionnaire.setAnswerByNr(8, cursor.getBlob(11));
            questionnaire.setAnswerByNr(9, cursor.getBlob(12));
            questionnaire.setAnswerByNr(10, cursor.getBlob(13));
            questionnaire.setAnswerByNr(11, cursor.getBlob(14));
            questionnaire.setAnswerByNr(12, cursor.getBlob(15));
            questionnaire.setAnswerByNr(13, cursor.getBlob(16));

            questionnaire.setLastQuestionEditedNr(cursor.getInt(17));
            questionnaire.setProgressInPercent(cursor.getInt(18));
            medicalUserList.add(questionnaire);
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return medicalUserList;
    }

    @NonNull
    private String[] getColumns() {
        return new String[]{DBContracts.HADSDTable.CREATION_DATE_PK,
                DBContracts.HADSDTable.NAME_ID_FK,
                DBContracts.HADSDTable.UPDATE_DATE,
                DBContracts.HADSDTable.ANSWER_TO_QUESTION1,
                DBContracts.HADSDTable.ANSWER_TO_QUESTION2,
                DBContracts.HADSDTable.ANSWER_TO_QUESTION3,
                DBContracts.HADSDTable.ANSWER_TO_QUESTION4,
                DBContracts.HADSDTable.ANSWER_TO_QUESTION5,
                DBContracts.HADSDTable.ANSWER_TO_QUESTION6,
                DBContracts.HADSDTable.ANSWER_TO_QUESTION7,
                DBContracts.HADSDTable.ANSWER_TO_QUESTION8,
                DBContracts.HADSDTable.ANSWER_TO_QUESTION9,
                DBContracts.HADSDTable.ANSWER_TO_QUESTION10,
                DBContracts.HADSDTable.ANSWER_TO_QUESTION11,
                DBContracts.HADSDTable.ANSWER_TO_QUESTION12,
                DBContracts.HADSDTable.ANSWER_TO_QUESTION13,
                DBContracts.HADSDTable.ANSWER_TO_QUESTION14,
                DBContracts.HADSDTable.LAST_QEUSTION_EDITED_NR,
                DBContracts.HADSDTable.PROGRESS
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
