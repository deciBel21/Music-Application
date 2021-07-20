package com.signupactivity.signupactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class nointernetactivity extends AppCompatActivity {
    ImageView warningimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nointernetactivity);
        warningimage=(ImageView)findViewById(R.id.warningimage);
    }
}