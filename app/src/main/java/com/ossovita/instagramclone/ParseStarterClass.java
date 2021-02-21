package com.ossovita.instagramclone;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;

public class ParseStarterClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        Parse.initialize(new Parse.Configuration.Builder(this)
        .applicationId("bxqnJASx2Kkzg5DDjC9ShgInCejpvOanCGcecuFW")
        .clientKey("zl4V7YS916xyaKrliVBWl00nI2xl7JNwCC62m9Kd")
        .server("https://parseapi.back4app.com/")
        .build());


        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL,true);

    }
}
