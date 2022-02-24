package com.example.timber.ui;

import static android.widget.Toast.LENGTH_SHORT;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.timber.databinding.DemoActivityBinding;

import timber.log.Timber;

public class DemoActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DemoActivityBinding binding = DemoActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Timber.d("Activity Created");
        Timber.tags("LifeCycles", "LifeCycles1", "LifeCycles2").d("Activity Created2");

        binding.hello.setOnClickListener(this);
        binding.hey.setOnClickListener(this);
        binding.hi.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timber.tags("LifeCycles", "LifeCycles1", "LifeCycles2").d("Activity onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Timber.tags("LifeCycles", "LifeCycles1", "LifeCycles2").d("Activity onStart");
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        Timber.i("A button with ID %s was clicked to say '%s'.", button.getId(), button.getText());
        Toast.makeText(this, "Check logcat for a greeting!", LENGTH_SHORT).show();
    }
}
