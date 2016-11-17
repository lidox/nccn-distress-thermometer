package com.artursworld.nccn.model.persistence.manager;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.artursworld.nccn.model.persistence.contracts.DBContracts;

import java.text.SimpleDateFormat;

/*
* Manages entities via application context
*/
public class EntityDbManager {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    protected SQLiteDatabase database;
    private DBContracts.DatabaseHelper dbHelper;
    private Context mContext;

    public EntityDbManager(Context context) {
        this.mContext = context;
        dbHelper = DBContracts.DatabaseHelper.getHelper(mContext);
        open();
    }

    /*
    * opens database interconnection
    */
    public void open() throws SQLException {
        if(dbHelper == null)
            dbHelper = DBContracts.DatabaseHelper.getHelper(mContext);
        database = dbHelper.getWritableDatabase();
        database.execSQL("PRAGMA foreign_keys=ON");
    }

    /*public void close() {
        dbHelper.close();
        database = null;
    }*/
}
