package com.artursworld.nccn.model.persistence.manager;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.artursworld.nccn.controller.config.App;
import com.artursworld.nccn.controller.util.Global;
import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;
import com.artursworld.nccn.model.entity.HADSDQuestionnaire;
import com.artursworld.nccn.model.entity.QolQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.contracts.DBContracts;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserManager extends EntityDbManager {

    private String CLASS_NAME = UserManager.class.getSimpleName();

    public UserManager() {
        super(App.getAppContext());
    }

    /**
     * This constructor is used for unit tests
     *
     * @param ctx the database context to use
     */
    public UserManager(Context ctx) {
        super(ctx);
    }

    /**
     * Insert user into database
     *
     * @param user the user to insert
     */
    public boolean insertUser(User user) {
        if (user == null) {
            Log.e(UserManager.class.getSimpleName(), "the user to insert is null!");
            return false;
        }

        ContentValues values = getUserContentValues(user);

        try {
            database.insertOrThrow(DBContracts.UserTable.TABLE_NAME, null, values);
            Log.i(UserManager.class.getSimpleName(), "New user added successfully: " + user.toString());
            return true;
        } catch (Exception e) {
            Log.e(UserManager.class.getSimpleName(), "Could not insert new user into db: " + user.toString() + "! " + e.getLocalizedMessage());
            return false;
        }
    }

    private ContentValues getUserContentValues(User user) {
        ContentValues values = new ContentValues();

        if (user.getName() != null)
            values.put(DBContracts.UserTable.NAME_ID_PK, user.getName());

        if (user.getCreationDate() != null)
            values.put(DBContracts.UserTable.CREATION_DATE, EntityDbManager.dateFormat.format(user.getCreationDate()));

        return values;
    }

    public List<User> getAllUsers() {
        String sortOrder = DBContracts.UserTable.CREATION_DATE + " DESC";
        Cursor cursor = database.query(DBContracts.UserTable.TABLE_NAME,
                getColumns(), null, null, null, null, sortOrder);

        List<User> userList = new ArrayList<User>();
        while (cursor.moveToNext()) {
            User medicalUser = getUserByCursor(cursor);
            userList.add(medicalUser);
        }
        Log.i(UserManager.class.getSimpleName(), userList.size() + ". users has been found:");
        Log.i(UserManager.class.getSimpleName(), userList.toString());

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return userList;
    }

    @NonNull
    private String[] getColumns() {
        return new String[]{
                DBContracts.UserTable.NAME_ID_PK,
                DBContracts.UserTable.CREATION_DATE,
                //DBContracts.UserTable.UPDATE_DATE,
                //DBContracts.UserTable.BIRTH_DATE,
                //DBContracts.UserTable.GENDER
        };
    }

    @NonNull
    private User getUserByCursor(Cursor cursor) {
        User medicalUser = new User(cursor.getString(0));
        try {
            medicalUser.setCreationDate(EntityDbManager.dateFormat.parse(cursor.getString(1)));
        } catch (Exception e) {
            Log.i(UserManager.class.getSimpleName(), "Could not load creation date for  user" + e.getLocalizedMessage());
        }
        //medicalUser.setBmi(cursor.getDouble(6));
        //medicalUser.setMarkedAsDeleted((cursor.getInt(7) == 1) ? true : false);
        return medicalUser;
    }

    public User getUserByName(String name) {
        String sortOrder = DBContracts.UserTable.CREATION_DATE + " DESC";
        String WHERE_CLAUSE = DBContracts.UserTable.NAME_ID_PK + " like '" + name + "'";

        Cursor cursor = database.query(DBContracts.UserTable.TABLE_NAME,
                getColumns(), WHERE_CLAUSE, null, null, null, sortOrder);

        User user = null;
        while (cursor.moveToNext()) {
            user = getUserByCursor(cursor);
        }
        Log.i(UserManager.class.getSimpleName(), user + " has been found:");

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return user;
    }

    public List<Date> getQuestionnaireDateListByUserName(String userName) {
        List<Date> resultList = new ArrayList<>();

        List<String> list = getQuestionnaireDatesByUserName(userName);
        for (String date : list) {
            try {
                resultList.add(EntityDbManager.dateFormat.parse(date));
            } catch (ParseException e) {
                Log.e(CLASS_NAME, e.getLocalizedMessage());
            }
        }

        return resultList;
    }

    public List<String> getQuestionnaireDatesByUserName(String userName) {
        List<QolQuestionnaire> listA = new QualityOfLifeManager().getQolQuestionnaireList(userName);
        List<HADSDQuestionnaire> listB = new HADSDQuestionnaireManager().getHadsdQuestionnaireListByUserName(userName);
        List<DistressThermometerQuestionnaire> listC = new DistressThermometerQuestionnaireManager().getDistressThermometerQuestionnaireList(userName);

        List<String> dateList = new ArrayList<>();
        for (QolQuestionnaire item : listA) {
            String questionnaireDate = EntityDbManager.dateFormat.format(item.getCreationDate_PK());
            if (!dateList.contains(questionnaireDate)) {
                dateList.add(questionnaireDate);
            }
        }

        for (HADSDQuestionnaire item : listB) {
            String questionnaireDate = EntityDbManager.dateFormat.format(item.getCreationDate_PK());
            if (!dateList.contains(questionnaireDate)) {
                dateList.add(questionnaireDate);
            }
        }

        for (DistressThermometerQuestionnaire item : listC) {
            String questionnaireDate = EntityDbManager.dateFormat.format(item.getCreationDate_PK());
            if (!dateList.contains(questionnaireDate)) {
                dateList.add(questionnaireDate);
            }
        }
        Log.i(CLASS_NAME, "Questionnaire Dates found: " + dateList.toString());
        return dateList;
    }


    /*
    * updates a user in database
    */
    public long update(User user) {
        try {
            String WHERE_CREATION_DATE_EQUALS = DBContracts.UserTable.CREATION_DATE + " =?";
            ContentValues values = getUserContentValues(user);
            String creationDate = EntityDbManager.dateFormat.format(user.getCreationDate());
            long i = database.update(DBContracts.UserTable.TABLE_NAME, values, WHERE_CREATION_DATE_EQUALS, new String[]{creationDate});
            if (i > 0) {
                Log.i(CLASS_NAME, "Updated user(" + user.toString() + ")");
                return 1;  // 1 for successful
            } else {
                Log.w(CLASS_NAME, "Failed to update user" + user.toString());
                return 0;  // 0 for unsuccessful
            }
        } catch (Exception e) {
            Log.e(CLASS_NAME, "Exception! Could not update user" + user.toString() + "! " + e.getLocalizedMessage());
            return 0;
        }
    }

    /**
     * Updates a user by
     */
    public long renameUser(final String oldName, final String newName) {
        if (oldName != null) {
            Log.i(CLASS_NAME, "rename user from '" + oldName + "' to '" + newName + "'");
            User u = new UserManager().getUserByName(oldName);
            if (u != null) {
                u.setName(newName);
                return new UserManager().update(u);
            }
        }
        return 0;
    }


    /**
     * Deletes user from database
     *
     * @param user the user to delete
     * @return the number of rows affected if a whereClause is passed in, 0
     * otherwise.
     */
    public int delete(User user) {
        int resultCode = 0;

        // validation
        if (user == null)
            return resultCode;

        String WHERE_CLAUSE = DBContracts.UserTable.CREATION_DATE + " =?";
        try {
            resultCode = database.delete(
                    DBContracts.UserTable.TABLE_NAME,
                    WHERE_CLAUSE,
                    new String[]{EntityDbManager.dateFormat.format(user.getCreationDate())}
            );
            Log.i(CLASS_NAME, "User(" + user + ") has been deleted from database");
        } catch (Exception e) {
            Log.e(CLASS_NAME, "Exception! Could not delete User(" + user + ") from databse" + " " + e.getLocalizedMessage());
        }

        return resultCode;
    }
}
