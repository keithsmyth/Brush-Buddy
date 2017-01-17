package com.keithsmyth.brushbuddy.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    public final String uuid;
    public final String name;
    public final List<Buddy> buddies = new ArrayList<>();

    public User(String uuid, String name, List<Buddy> buddies) {
        this.uuid = uuid;
        this.name = name;
        this.buddies.addAll(buddies);
    }
}
