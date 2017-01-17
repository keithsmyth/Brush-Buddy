package com.keithsmyth.brushbuddy;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

public class App extends Application {

    private Injector injector;

    @Override
    public void onCreate() {
        super.onCreate();
        injector = new Injector(this);
    }

    public static Injector inject(@NonNull Context context) {
        return ((App) context.getApplicationContext()).injector;
    }
}
