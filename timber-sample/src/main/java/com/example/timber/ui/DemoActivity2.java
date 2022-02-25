package com.example.timber.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.timber.databinding.DemoActivityBinding;

import java.util.Random;

import timber.log.Timber;

public class DemoActivity2 extends Activity {
    private static final String TAG = "DemoActivity";

    public static final String TAG_LOGICAL2 = "TAG_LOGICAL2";
    public static final String TAG_LOGICAL3 = "TAG_LOGICAL3";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DemoActivityBinding binding = DemoActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        logical1();
        logical2();
        logical3();
    }


    //实际执行步骤是method1-5
    void logical1() {
        method1();
    }

    //实际执行步骤是method2-5
    void logical2() {
        method2();
    }

    //实际执行步骤是method3-5
    void logical3() {
        method3();
    }


    //Method
    private void method1() {
        Log.d(TAG, "method1: ");
        Log.d(TAG_LOGICAL2, "method1: ");
        Log.d(TAG_LOGICAL3, "method1: ");
        method2();
    }

    private void method2() {

        Log.d(TAG, "method2: ");
        Log.d(TAG_LOGICAL2, "method2: ");
        Log.d(TAG_LOGICAL3, "method2: ");
        method3();
    }

    private void method3() {

        Log.d(TAG, "method3: ");
        Log.d(TAG_LOGICAL2, "method3: ");
        Log.d(TAG_LOGICAL3, "method3: ");
        method4();
    }

    private void method4() {

        Log.d(TAG, "method4: ");
        Log.d(TAG_LOGICAL2, "method4: ");
        Log.d(TAG_LOGICAL3, "method4: ");
        method5();

    }

    private void method5() {

        Log.d(TAG, "method5: ");
        Log.d(TAG_LOGICAL2, "method5: ");
        Log.d(TAG_LOGICAL3, "method5: ");
    }

}
