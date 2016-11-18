package com.artursworld.nccn.model.persistence.manager;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.artursworld.nccn.controller.config.App;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.contracts.DBContracts;

import java.util.ArrayList;
import java.util.List;

public class UserManager extends EntityDbManager {

    public UserManager() {
        super(App.getAppContext());
    }

    /**
     * This constructor is used for unit tests
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
    public void insertUser(User user) {
        if (user == null) {
            Log.e(UserManager.class.getSimpleName(),"the user to insert is null!");
            return;
        }

        ContentValues values = getUserContentValues(user);

        try {
            database.insertOrThrow(DBContracts.UserTable.TABLE_NAME, null, values);
            Log.i(UserManager.class.getSimpleName(),"New user added successfully:" + user.toString());
        } catch (Exception e) {
            Log.e(UserManager.class.getSimpleName(),"Could not insert new user into db: " + user.toString() + "! " + e.getLocalizedMessage());
        }
    }

    private ContentValues getUserContentValues(User user) {
        ContentValues values = new ContentValues();

        if(user.getName() != null)
            values.put(DBContracts.UserTable.NAME_ID_PK, user.getName());

        if(user.getCreationDate() != null)
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
        Log.i(UserManager.class.getSimpleName(),userList.size() + ". users has been found:");
        Log.i(UserManager.class.getSimpleName(),userList.toString());

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
            Log.i(UserManager.class.getSimpleName(),"Could not load creation date for  user" + e.getLocalizedMessage());
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
}
