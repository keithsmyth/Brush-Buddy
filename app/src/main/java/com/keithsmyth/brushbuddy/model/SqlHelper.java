package com.keithsmyth.brushbuddy.model;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlHelper extends SQLiteOpenHelper {

    private static final String NAME = "BrushBuddy.db";
    private static final int VERSION = 1;

    public SqlHelper(Application application) {
        super(application, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        UserDao.create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // no op
    }
}
