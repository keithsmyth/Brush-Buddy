package com.keithsmyth.brushbuddy.model;

import android.text.TextUtils;

import java.util.Collections;
import java.util.UUID;

import io.reactivex.Single;

public class UserProvider {

    private final UserDao userDao;

    public UserProvider(UserDao userDao) {
        this.userDao = userDao;
    }

    public Single<User> current() {
        // check shared prefs for uuid
        final String currentUserUuid = userDao.currentUserUuid();

        if (TextUtils.isEmpty(currentUserUuid)) {
            // default create user
            final User user = new User(UUID.randomUUID().toString(),
                    "Oscar",
                    Collections.<Buddy>emptyList());

            userDao.setCurrentUser(user.uuid);
            return userDao.save(user)
                    .toSingleDefault(user);
        } else {
            // retrieve from db
            return userDao.byUuid(currentUserUuid);
        }
    }

    public void logout() {
        userDao.setCurrentUser(null);
    }
}
