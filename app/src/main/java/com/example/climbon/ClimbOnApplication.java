package com.example.climbon;

import android.app.Application;

public class ClimbOnApplication extends Application {
    /* Holds gloabl data for storage.

    TODO:
    Refator with Unviversal Data?
    */
    private static ClimbOnApplication singleton;

    public ClimbOnApplication getInstance(){
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }
    public UniversalData data = new UniversalData();
}
