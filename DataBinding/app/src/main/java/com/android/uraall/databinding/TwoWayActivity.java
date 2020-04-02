package com.android.uraall.databinding;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.uraall.databinding.databinding.ActivityTwoWayBinding;

public class TwoWayActivity extends AppCompatActivity {

    private ActivityTwoWayBinding activityTwoWayBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_way);

        activityTwoWayBinding = DataBindingUtil.setContentView(this, R.layout.activity_two_way);
        activityTwoWayBinding.setGreeting(getCurrentGreeting());
    }

    private Greeting getCurrentGreeting() {
        return new Greeting("John", "Hello, dude");
    }
}
