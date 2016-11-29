package com.artursworld.nccn.model.persistence.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.artursworld.nccn.controller.config.App;
import com.artursworld.nccn.model.entity.QolQuestionnaire;
import com.artursworld.nccn.model.persistence.contracts.DBContracts;

import java.util.ArrayList;
import java.util.List;

public class QualityOfLifeManager extends EntityDbManager {

    public QualityOfLifeManager() {
        super(App.getAppContext());
    }

    /**
     * This constructor is used for unit tests
     * @param context the database context to use
     */
    QualityOfLifeManager(Context context) {
        super(context);
    }

    /**
     * Insert questionnaire into database
     *
     * @param questionnaire the questionnaire to insert
     */
    public void insertQuestionnaire(QolQuestionnaire questionnaire) {
        if (questionnaire == null) {
            Log.e(QualityOfLifeManager.class.getSimpleName(),"the questionnaire to insert equals null!");
            return;
        }

        ContentValues values = getQuestionnaireContentValues(questionnaire);

        try {
            database.insertOrThrow(DBContracts.QualityOfLifeTable.TABLE_NAME, null, values);
            Log.i(QualityOfLifeManager.class.getSimpleName(),"New questionnaire added successfully:" + questionnaire.toString());
        } catch (Exception e) {
            Log.e(QualityOfLifeManager.class.getSimpleName(),"Could not insert new questionnaire into db: " + questionnaire.toString() + "! " + e.getLocalizedMessage());
        }
    }

    /**
     * Get content values by questionnaire
     * @param questionnaire the questionnaire to get the values out
     * @return the ContentValues of the questionnaire
     */
    private ContentValues getQuestionnaireContentValues(QolQuestionnaire questionnaire) {
        ContentValues values = new ContentValues();

        if(questionnaire.getUserNameId_FK() != null)
            values.put(DBContracts.QualityOfLifeTable.NAME_ID_FK, questionnaire.getUserNameId_FK());

        if(questionnaire.getCreationDate_PK() != null)
            values.put(DBContracts.QualityOfLifeTable.CREATION_DATE_PK, EntityDbManager.dateFormat.format(questionnaire.getCreationDate_PK()));

        if(questionnaire.getUpdateDate() != null)
            values.put(DBContracts.QualityOfLifeTable.UPDATE_DATE, EntityDbManager.dateFormat.format(questionnaire.getUpdateDate()));

        if(questionnaire.getAnswersToQuestionsBytes() != null)
            values.put(DBContracts.QualityOfLifeTable.ANSWERS_TO_QUESTIONS, questionnaire.getAnswersToQuestionsBytes());

        return values;
    }

    /**
     * Returns the questionnaire by users name from database
     *
     * @param name the users name
     * @return returns the Questionnaire by users name
     */
    public QolQuestionnaire getQolQuestionnaireByUserName(String name) {
        List<QolQuestionnaire> medicalUserList = new ArrayList<>();
        Cursor cursor = database.query(DBContracts.QualityOfLifeTable.TABLE_NAME,
                getColumns(),
                DBContracts.QualityOfLifeTable.NAME_ID_FK + " LIKE '" + name + "'",
                null, null, null, null);

        while (cursor.moveToNext()) {
            QolQuestionnaire questionnaire = new QolQuestionnaire(cursor.getString(1));

            try {
                questionnaire.setCreationDate_PK(EntityDbManager.dateFormat.parse(cursor.getString(0)));
                questionnaire.setUpdateDate(EntityDbManager.dateFormat.parse(cursor.getString(2)));
            } catch (Exception e) {
                Log.i(QualityOfLifeManager.class.getSimpleName(),"Failed to parse date at getQolQuestionnaireByUserName(" + name + ")!" + e.getLocalizedMessage());
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
            Log.e(QualityOfLifeManager.class.getSimpleName(),"Exception! Could not find QolQuestionnaire by name(" + name + ") in database");
            return null;
        }
    }

    @NonNull
    private String[] getColumns() {
        return new String[]{DBContracts.QualityOfLifeTable.CREATION_DATE_PK,
                DBContracts.QualityOfLifeTable.NAME_ID_FK,
                DBContracts.QualityOfLifeTable.UPDATE_DATE,
                DBContracts.QualityOfLifeTable.ANSWERS_TO_QUESTIONS
        };
    }

    public void update(QolQuestionnaire questionnaire) {
        String WHERE_CLAUSE = DBContracts.QualityOfLifeTable.CREATION_DATE_PK + " =?";
        String[] WHERE_ARGS = new String[] {EntityDbManager.dateFormat.format(questionnaire.getCreationDate_PK())};

        if (questionnaire.getCreationDate_PK() == null) {
            Log.e(QualityOfLifeManager.class.getSimpleName(),"Cannot update questionnaire: " + questionnaire);
            return;
        }

        try {
            ContentValues contentValues = getQuestionnaireContentValues(questionnaire);
            database.update(DBContracts.QualityOfLifeTable.TABLE_NAME, contentValues, WHERE_CLAUSE, WHERE_ARGS);
            Log.i(QualityOfLifeManager.class.getSimpleName(),questionnaire + " has been updated");
        } catch (Exception e) {
            Log.e(QualityOfLifeManager.class.getSimpleName(),"Exception! Could not update the questionnaire(" + questionnaire + ") " + " " + e.getLocalizedMessage());
        }
    }

}
