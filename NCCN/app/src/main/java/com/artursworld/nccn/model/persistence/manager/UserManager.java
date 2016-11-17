package com.artursworld.nccn.model.persistence.manager;


import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.artursworld.nccn.controller.config.App;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.contracts.DBContracts;

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
}
