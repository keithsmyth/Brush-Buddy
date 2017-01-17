package com.keithsmyth.brushbuddy;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;

import com.keithsmyth.brushbuddy.model.SqlHelper;
import com.keithsmyth.brushbuddy.model.UserDao;
import com.keithsmyth.brushbuddy.model.UserProvider;

public class Injector {

    private final Application application;

    private UserProvider userProvider;
    private UserDao userDao;
    private SQLiteOpenHelper sqlHelper;
    private SharedPreferences prefs;

    public Injector(Application application) {
        this.application = application;
    }

    public UserProvider userProvider() {
        if (userProvider == null) {
            synchronized (this) {
                if (userProvider == null) userProvider = new UserProvider(userDao());
            }
        }
        return userProvider;
    }

    private UserDao userDao() {
        if (userDao == null) {
            synchronized (this) {
                if (userDao == null) userDao = new UserDao(sqlHelper(), prefs());
            }
        }
        return userDao;
    }

    private SQLiteOpenHelper sqlHelper() {
        if (sqlHelper == null) {
            synchronized (this) {
                if (sqlHelper == null) sqlHelper = new SqlHelper(application);
            }
        }
        return sqlHelper;
    }

    private SharedPreferences prefs() {
        if (prefs == null) {
            synchronized (this) {
                if (prefs == null) prefs = application.getSharedPreferences("BrushBuddy", Context.MODE_PRIVATE);
            }
        }
        return prefs;
    }
}
