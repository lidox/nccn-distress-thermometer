package com.artursworld.nccn.model.persistence.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.artursworld.nccn.controller.config.App;
import com.artursworld.nccn.controller.util.OperationType;
import com.artursworld.nccn.model.entity.MetaQuestionnaire;
import com.artursworld.nccn.model.entity.QolQuestionnaire;
import com.artursworld.nccn.model.persistence.contracts.DBContracts;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MetaQuestionnaireManager extends EntityDbManager {

    private String CLASS_NAME = MetaQuestionnaireManager.class.getSimpleName();
    private String tableName = DBContracts.MetaQuestionnaireTable.TABLE_NAME;

    public MetaQuestionnaireManager() {
        super(App.getAppContext());
    }


    /**
     * This constructor is used for unit tests
     *
     * @param context the database context to use
     */
    public MetaQuestionnaireManager(Context context) {
        super(context);
    }

    public long update(MetaQuestionnaire meta) {
        long rowsAffected = -1;

        if (meta.getCreationDate() == null) {
            Log.e(CLASS_NAME, "Cannot update meta: " + meta);
            return -1;
        }

        String WHERE_CLAUSE = DBContracts.MetaQuestionnaireTable.CREATION_DATE_QUESTIONNAIRE + " =?";
        String[] WHERE_ARGS = new String[]{EntityDbManager.dateFormat.format(meta.getCreationDate())};

        try {
            ContentValues contentValues = getMetaDataContentValues(meta);
            rowsAffected = database.update(tableName, contentValues, WHERE_CLAUSE, WHERE_ARGS);
            Log.i(tableName, meta + " has been updated. Rows affected: " + rowsAffected);
        } catch (Exception e) {
            Log.e(tableName, "Exception! Could not update the meta data(" + meta + ") " + " " + e.getLocalizedMessage());
        } finally {
            return rowsAffected;
        }
    }


    /**
     * Insert meta data into database
     *
     * @param meta the meta to insert
     */
    public void insert(MetaQuestionnaire meta) {
        if (meta == null) {
            Log.e(CLASS_NAME, "the meta data to insert equals null!");
            return;
        }

        ContentValues values = getMetaDataContentValues(meta);

        try {
            database.insertOrThrow(tableName, null, values);
            Log.i(CLASS_NAME, "New meta added successfully:" + meta.toString());
        } catch (Exception e) {
            Log.e(CLASS_NAME, "Could not insert new meta into db: " + meta.toString() + "! " + e.getLocalizedMessage());
        }
    }

    /**
     * Get content values by meta data
     *
     * @param meta the meta to get the values out
     * @return the ContentValues of the meta
     */
    private ContentValues getMetaDataContentValues(MetaQuestionnaire meta) {
        ContentValues values = new ContentValues();

        if (meta.getCreationDate() != null)
            values.put(DBContracts.MetaQuestionnaireTable.CREATION_DATE_QUESTIONNAIRE, EntityDbManager.dateFormat.format(meta.getCreationDate()));

        if (meta.getUpdateDate() != null)
            values.put(DBContracts.MetaQuestionnaireTable.UPDATE_DATE, EntityDbManager.dateFormat.format(meta.getUpdateDate()));

        if (meta.getOperationType() != null)
            values.put(DBContracts.MetaQuestionnaireTable.OPERATION_TYPE, meta.getOperationType().name());

        return values;
    }

    /**
     * Returns the meta data by creation date from database
     *
     * @param date the creation date
     * @return returns the meta data by creation date from database
     */
    public MetaQuestionnaire getMetaDataByCreationDate(Date date) {
        return getMetaQuestionnaire(date);
    }


    /**
     * Returns the meta data by creation date from database
     *
     * @param date the creation date
     * @return returns the meta data by creation date from database
     */
    /*
    public MetaQuestionnaire getMetaDataByCreationDate(String date){
        try {
            Date creationDate = EntityDbManager.dateFormat.parse(date);
            return getMetaQuestionnaire(creationDate);
        } catch (ParseException e) {
            Log.e(CLASS_NAME, "Could not getMetaDataByCreationDate: " +e.getLocalizedMessage());
        }
        return null;
    }
    */

    /**
     * Helps to get meta data by creation date
     *
     * @param date the creation date
     * @return a single meta data object
     */
    @Nullable
    private MetaQuestionnaire getMetaQuestionnaire(Date date) {
        List<MetaQuestionnaire> metaList = getMetaListByDate(date);

        if (metaList.size() > 0) {
            return metaList.get(0);
        } else {
            Log.w(CLASS_NAME, "WARNING! Could not find meta data by creation date(" + date + ") in database");
            return null;
        }
    }

    /**
     * Get all meta dates by a creation date
     *
     * @param creationDate the creation date of the meta data
     * @return a list of all meta dates by creation date
     */
    @NonNull
    public List<MetaQuestionnaire> getMetaListByDate(Date creationDate) {
        List<MetaQuestionnaire> metaList = new ArrayList<>();

        if (creationDate == null) {
            return metaList;
        }

        Cursor cursor = database.query(tableName,
                getColumns(),
                DBContracts.MetaQuestionnaireTable.CREATION_DATE_QUESTIONNAIRE + " LIKE '" + EntityDbManager.dateFormat.format(creationDate) + "'",
                null, null, null, null);

        while (cursor.moveToNext()) {
            MetaQuestionnaire meta = null;
            try {
                meta = new MetaQuestionnaire(EntityDbManager.dateFormat.parse(cursor.getString(0)));
                meta.setCreationDate(EntityDbManager.dateFormat.parse(cursor.getString(0)));
                meta.setUpdateDate(EntityDbManager.dateFormat.parse(cursor.getString(1)));
                meta.setOperationType(OperationType.findByName(cursor.getString(2)));

            } catch (Exception e) {
                Log.i(CLASS_NAME, "Failed to getMetaListByDate(" + creationDate + ")!" + e.getLocalizedMessage());
            }
            metaList.add(meta);
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return metaList;
    }

    @NonNull
    private String[] getColumns() {
        return new String[]{
                DBContracts.MetaQuestionnaireTable.CREATION_DATE_QUESTIONNAIRE,
                DBContracts.MetaQuestionnaireTable.UPDATE_DATE,
                DBContracts.MetaQuestionnaireTable.OPERATION_TYPE
        };
    }
}
