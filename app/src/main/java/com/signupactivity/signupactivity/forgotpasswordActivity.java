package com.signupactivity.signupactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotpasswordActivity extends AppCompatActivity implements View.OnClickListener {
    EditText email3;
    Button reset;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        email3=(EditText)findViewById(R.id.email3);
        email3.setOnClickListener(this);
        reset=(Button)findViewById(R.id.button2);
        reset.setOnClickListener(this);
        reset.setAlpha(0.5f);
        mAuth=FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ConnectivityManager manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=manager.getActiveNetworkInfo();
        if (null!=info)
        {
            if (info.getType()==ConnectivityManager.TYPE_MOBILE)
            {
                //Toast.makeText(this, "Data Enabled!", Toast.LENGTH_SHORT).show();
            }
            else if (info.getType()==ConnectivityManager.TYPE_WIFI)
            {
                // Toast.makeText(this, "Wifi Enabled!", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            startActivity(new Intent(this,nointernetactivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.button2:
                reset.setAlpha(1);
                regigsterUser();
                break;
            case R.id.email3:
                email3.setAlpha(1);
        }
    }
    private void regigsterUser()
    {
        String email=email3.getText().toString();
        if(email.isEmpty())
        {
            email3.requestFocus();
            email3.setError("Please fill the required detail!");
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            email3.requestFocus();
            email3.setError("Please enter valid email");
            return;
        }
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(forgotpasswordActivity.this, "Please check your email for password reset.", Toast.LENGTH_SHORT).show();
                    reset.setAlpha(1f);
                }
                else
                {
                    Toast.makeText(forgotpasswordActivity.this, "Try again! Later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}