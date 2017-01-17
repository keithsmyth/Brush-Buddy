package com.keithsmyth.brushbuddy.model;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Collections;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Action;

import static io.reactivex.schedulers.Schedulers.io;

public class UserDao {

    private static final String CURRENT_USER_UUID = "CurrentUserUuid";

    private final SQLiteOpenHelper sqlHelper;
    private final SharedPreferences prefs;

    public UserDao(SQLiteOpenHelper sqlHelper, SharedPreferences prefs) {
        this.sqlHelper = sqlHelper;
        this.prefs = prefs;
    }

    public static void create(SQLiteDatabase db) {
        db.execSQL("create table User (uuid text PRIMARY KEY, name text)");
    }

    public String currentUserUuid() {
        return prefs.getString(CURRENT_USER_UUID, null);
    }

    public void setCurrentUser(@Nullable String uuid) {
        if (TextUtils.isEmpty(uuid)) {
            prefs.edit().remove(CURRENT_USER_UUID).apply();
        } else {
            prefs.edit().putString(CURRENT_USER_UUID, uuid).apply();
        }
    }

    public Single<User> byUuid(String uuid) {
        return Single.fromCallable(new UserByUuidCallable(sqlHelper, uuid))
                .subscribeOn(io());
    }

    public Completable save(User user) {
        return Completable.fromAction(new SaveUserAction(sqlHelper, user))
                .subscribeOn(io());
    }

    private static class UserByUuidCallable implements Callable<User> {

        private final SQLiteOpenHelper sqlHelper;
        private final String uuid;

        private UserByUuidCallable(SQLiteOpenHelper sqlHelper, String uuid) {
            this.sqlHelper = sqlHelper;
            this.uuid = uuid;
        }

        @Override
        public User call() throws Exception {
            final SQLiteDatabase db = sqlHelper.getReadableDatabase();
            try (final Cursor cursor = db.query("User", new String[]{"uuid, name"}, "uuid = ?", new String[]{uuid}, null, null, null)) {
                if (cursor.moveToNext()) {
                    return new User(cursor.getString(0), cursor.getString(1), Collections.<Buddy>emptyList());
                }
            }
            throw new IllegalArgumentException("Invalid uuid " + uuid);
        }
    }

    private static class SaveUserAction implements Action {

        private final SQLiteOpenHelper sqlHelper;
        private final User user;

        private SaveUserAction(SQLiteOpenHelper sqlHelper, User user) {
            this.sqlHelper = sqlHelper;
            this.user = user;
        }

        @Override
        public void run() throws Exception {
            final SQLiteDatabase db = sqlHelper.getWritableDatabase();
            final ContentValues values = new ContentValues();
            values.put("uuid", user.uuid);
            values.put("name", user.name);
            db.insert("User", null, values);
        }
    }
}
