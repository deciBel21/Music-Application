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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText email2;
    EditText password2;
    Button login2;
    ImageView back;
    TextView forgot;
    private FirebaseAuth mAuth;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       LoginActivity.this.finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email2 = (EditText) findViewById(R.id.email2);
        password2 = (EditText) findViewById(R.id.password2);
        login2 = (Button) findViewById(R.id.button);
        login2.setAlpha(0.5f);
        forgot=(TextView)findViewById(R.id.forgot);
        login2.setOnClickListener(this);
        forgot.setOnClickListener(this);
        back=findViewById(R.id.Back_Arrow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        });
        mAuth = FirebaseAuth.getInstance();
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
        switch (v.getId()) {
            case R.id.button:
                login2.setAlpha(1);
                userlogin();
                break;
            case R.id.forgot:
                forgot.setAlpha(1);
                startActivity(new Intent(this,forgotpasswordActivity.class));
                finish();
        }

    }

    private void userlogin() {
        String email = email2.getText().toString().trim();
        String password = password2.getText().toString().trim();
        if (email.isEmpty()) {
            email2.requestFocus();
            email2.setError("Please fill the required detail!");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email2.requestFocus();
            email2.setError("Please enter valid email!");
            return;
        }
        if (password.isEmpty()) {
            password2.requestFocus();
            password2.setError("Please fill the required detail");
            return;
        }
        if (password.length() < 6) {
            password2.requestFocus();
            password2.setError("Minimum 6 chahrcters required");
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified())
                    {
                        startActivity(new Intent(
                                LoginActivity.this, MainPlayer.class));
                        login2.setAlpha(1f);
                        finish();

                    }
                    else {
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this, "Please check your email for verification.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Not able to Login please check fill the details correctly!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}