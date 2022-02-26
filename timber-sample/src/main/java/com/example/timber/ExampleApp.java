package com.example.timber;

import static com.example.timber.ui.DemoActivity2.TAG_LOGICAL2;
import static com.example.timber.ui.DemoActivity2.TAG_LOGICAL3;
import static timber.log.Timber.DebugTree;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import timber.log.Timber;

public class ExampleApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new DebugTree());
        Timber.openTags(TAG_LOGICAL3);

    }

}
