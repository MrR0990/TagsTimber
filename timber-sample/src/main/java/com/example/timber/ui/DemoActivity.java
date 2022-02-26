package com.example.timber.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.timber.databinding.DemoActivityBinding;

import java.util.Random;

import timber.log.Timber;

public class DemoActivity extends Activity {
    private static final String TAG = "DemoActivity";

    private static final String TAG_LOGICAL2 = "TAG_LOGICAL2";
    private static final String TAG_LOGICAL3 = "TAG_LOGICAL3";


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
        Timber.start(TAG_LOGICAL2);
        method2();
    }

    //实际执行步骤是method3-5
    void logical3() {
        Timber.start(TAG_LOGICAL3);
        method3();
    }


    //Method
    private void method1() {
        Timber.d("method1: ");
        method2();
    }

    private void method2() {
        //logical2()方法的逻辑从这里开始运行,就从这里开始添加上logical2的tag
        Timber.tags(TAG_LOGICAL2).d("method2: ");
        method3();
    }

    private void method3() {
        //logical2()和logical3()方法的逻辑从这里开始运行,就从这里开始添加上logical2和logical3的tag
        Timber.tags(TAG_LOGICAL2, TAG_LOGICAL3).d("method3: ");
        method4();
    }

    private void method4() {
        Timber.tags(TAG_LOGICAL2, TAG_LOGICAL3).d("method4: ");
        method5();

    }

    private void method5() {
        Timber.tags(TAG_LOGICAL2, TAG_LOGICAL3).d("method5: ");
        Timber.end(TAG_LOGICAL2, TAG_LOGICAL3);
    }

}
