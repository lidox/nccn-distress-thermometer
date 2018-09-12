package com.artursworld.nccn.model.persistence.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.artursworld.nccn.controller.config.App;
import com.artursworld.nccn.model.entity.FearOfProgressionQuestionnaire;
import com.artursworld.nccn.model.persistence.contracts.DBContracts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FearOfProgressionManager extends EntityDbManager {

    private String CLASS_NAME = FearOfProgressionManager.class.getSimpleName();

    public FearOfProgressionManager() {
        super(App.getAppContext());
    }

    FearOfProgressionManager(Context ctx) {
        super(ctx);
    }

    public void insertQuestionnaire(FearOfProgressionQuestionnaire questionnaire) {
        try {
            if (questionnaire == null) {
                Log.e(CLASS_NAME, "the questionnaire to be inserted equals null!");
                return;
            }

            ContentValues contentValues = getQuestionnaireContentValues(questionnaire);
            database.insertOrThrow(DBContracts.FoPTable.TABLE_NAME, null, contentValues);
            Log.i(CLASS_NAME, "New questionnaire added successfully:" + questionnaire.toString());

        } catch (Exception e) {
            Log.e(CLASS_NAME, "Could not insert new questionnaire into db: " + questionnaire + "! " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void update(FearOfProgressionQuestionnaire questionnaire) {
        String WHERE_CLAUSE = DBContracts.FoPTable.CREATION_DATE_PK + " =?";
        String[] WHERE_ARGS = new String[]{EntityDbManager.dateFormat.format(questionnaire.getCreationDate_PK())};

        if (questionnaire.getCreationDate_PK() == null) {
            Log.e(CLASS_NAME, "Cannot update questionnaire: " + questionnaire);
            return;
        }

        try {
            ContentValues contentValues = getQuestionnaireContentValues(questionnaire);
            database.update(DBContracts.FoPTable.TABLE_NAME, contentValues, WHERE_CLAUSE, WHERE_ARGS);
            Log.d(CLASS_NAME,  "questionnaire has been updated");
        } catch (Exception e) {
            Log.e(CLASS_NAME, "Exception! Could not update the questionnaire(" + questionnaire + ") " + " " + e.getLocalizedMessage());
        }
    }

    public FearOfProgressionQuestionnaire getQuestionnaireByDate(String userName, Date data) {
        List<FearOfProgressionQuestionnaire> list = getQuestionnaireListByUserName(userName);
        SimpleDateFormat format = EntityDbManager.dateFormat;
        for (FearOfProgressionQuestionnaire item : list) {
            if (format.format(item.getCreationDate_PK()).equals(format.format(data))) {
                return item;
            }
        }
        return null;
    }

    @NonNull
    private List<FearOfProgressionQuestionnaire> getQuestionnaireListByUserName(String userName) {
        List<FearOfProgressionQuestionnaire> resultList = new ArrayList<>();
        try {
            Cursor cursor = database.query(DBContracts.FoPTable.TABLE_NAME,
                    getColumns(),
                    DBContracts.FoPTable.COLUMN_NAME_ID_FK + " LIKE '" + userName + "'",
                    null, null, null, null);

            while (cursor.moveToNext()) {
                FearOfProgressionQuestionnaire questionnaire = new FearOfProgressionQuestionnaire(null);
                questionnaire.setCreationDate_PK(EntityDbManager.dateFormat.parse(cursor.getString(0)));
                questionnaire.setUserNameId_FK(cursor.getString(1));
                questionnaire.setUpdateDate(EntityDbManager.dateFormat.parse(cursor.getString(2)));
                questionnaire.setLastQuestionEditedNr(cursor.getInt(3));
                questionnaire.setProgressInPercent(cursor.getInt(4));
                questionnaire.setSelectedAnswerIndexQ1(cursor.getInt(5));
                questionnaire.setSelectedAnswerIndexQ2(cursor.getInt(6));
                questionnaire.setSelectedAnswerIndexQ3(cursor.getInt(7));
                questionnaire.setSelectedAnswerIndexQ4(cursor.getInt(8));
                questionnaire.setSelectedAnswerIndexQ5(cursor.getInt(9));
                questionnaire.setSelectedAnswerIndexQ6(cursor.getInt(10));
                questionnaire.setSelectedAnswerIndexQ7(cursor.getInt(11));
                questionnaire.setSelectedAnswerIndexQ8(cursor.getInt(12));
                questionnaire.setSelectedAnswerIndexQ9(cursor.getInt(13));
                questionnaire.setSelectedAnswerIndexQ10(cursor.getInt(14));
                questionnaire.setSelectedAnswerIndexQ11(cursor.getInt(15));
                questionnaire.setSelectedAnswerIndexQ12(cursor.getInt(16));
                resultList.add(questionnaire);
            }

            if (!cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.i(CLASS_NAME, "Failed to parse date at getQuestionnaireByUserName(" + userName + ")!" + e.getLocalizedMessage());
        }
        return resultList;
    }

    /**
     * Get content values by questionnaire
     *
     * @param questionnaire the questionnaire to get the values out
     * @return the ContentValues of the questionnaire
     */
    private ContentValues getQuestionnaireContentValues(FearOfProgressionQuestionnaire questionnaire) {
        ContentValues values = new ContentValues();

        if (questionnaire.getUserNameId_FK() != null)
            values.put(DBContracts.FoPTable.COLUMN_NAME_ID_FK, questionnaire.getUserNameId_FK());

        if (questionnaire.getCreationDate_PK() != null)
            values.put(DBContracts.FoPTable.CREATION_DATE_PK, EntityDbManager.dateFormat.format(questionnaire.getCreationDate_PK()));

        if (questionnaire.getUpdateDate() != null)
            values.put(DBContracts.FoPTable.UPDATE_DATE, EntityDbManager.dateFormat.format(questionnaire.getUpdateDate()));

        if (questionnaire.getLastQuestionEditedNr() >= 0)
            values.put(DBContracts.FoPTable.LAST_QUESTION_EDITED_NR, questionnaire.getLastQuestionEditedNr());

        if (questionnaire.getProgressInPercent() >= 0)
            values.put(DBContracts.FoPTable.PROGRESS, questionnaire.getProgressInPercent());

        if (questionnaire.getSelectedAnswerIndexQ1() > -1)
            values.put(DBContracts.FoPTable.ANSWER_1, questionnaire.getSelectedAnswerIndexQ1());

        if (questionnaire.getSelectedAnswerIndexQ2() > -1)
            values.put(DBContracts.FoPTable.ANSWER_2, questionnaire.getSelectedAnswerIndexQ2());

        if (questionnaire.getSelectedAnswerIndexQ3() > -1)
            values.put(DBContracts.FoPTable.ANSWER_3, questionnaire.getSelectedAnswerIndexQ3());

        if (questionnaire.getSelectedAnswerIndexQ4() > -1)
            values.put(DBContracts.FoPTable.ANSWER_4, questionnaire.getSelectedAnswerIndexQ4());

        if (questionnaire.getSelectedAnswerIndexQ5() > -1)
            values.put(DBContracts.FoPTable.ANSWER_5, questionnaire.getSelectedAnswerIndexQ5());

        if (questionnaire.getSelectedAnswerIndexQ6() > -1)
            values.put(DBContracts.FoPTable.ANSWER_6, questionnaire.getSelectedAnswerIndexQ6());

        if (questionnaire.getSelectedAnswerIndexQ7() > -1)
            values.put(DBContracts.FoPTable.ANSWER_7, questionnaire.getSelectedAnswerIndexQ7());

        if (questionnaire.getSelectedAnswerIndexQ8() > -1)
            values.put(DBContracts.FoPTable.ANSWER_8, questionnaire.getSelectedAnswerIndexQ8());

        if (questionnaire.getSelectedAnswerIndexQ9() > -1)
            values.put(DBContracts.FoPTable.ANSWER_9, questionnaire.getSelectedAnswerIndexQ9());

        if (questionnaire.getSelectedAnswerIndexQ10() > -1)
            values.put(DBContracts.FoPTable.ANSWER_10, questionnaire.getSelectedAnswerIndexQ10());

        if (questionnaire.getSelectedAnswerIndexQ11() > -1)
            values.put(DBContracts.FoPTable.ANSWER_11, questionnaire.getSelectedAnswerIndexQ11());

        if (questionnaire.getSelectedAnswerIndexQ12() > -1)
            values.put(DBContracts.FoPTable.ANSWER_12, questionnaire.getSelectedAnswerIndexQ12());

        return values;
    }

    @NonNull
    private String[] getColumns() {
        return new String[]{
                DBContracts.FoPTable.CREATION_DATE_PK,
                DBContracts.FoPTable.COLUMN_NAME_ID_FK,
                DBContracts.FoPTable.UPDATE_DATE,
                DBContracts.FoPTable.LAST_QUESTION_EDITED_NR,
                DBContracts.FoPTable.PROGRESS,
                DBContracts.FoPTable.ANSWER_1,
                DBContracts.FoPTable.ANSWER_2,
                DBContracts.FoPTable.ANSWER_3,
                DBContracts.FoPTable.ANSWER_4,
                DBContracts.FoPTable.ANSWER_5,
                DBContracts.FoPTable.ANSWER_6,
                DBContracts.FoPTable.ANSWER_7,
                DBContracts.FoPTable.ANSWER_8,
                DBContracts.FoPTable.ANSWER_9,
                DBContracts.FoPTable.ANSWER_10,
                DBContracts.FoPTable.ANSWER_11,
                DBContracts.FoPTable.ANSWER_12,
        };
    }
}
