package com.signupactivity.signupactivity;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText email1;
    EditText password1;
    Button login1;
    Button signup1;
    EditText name1;
        private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectivityManager manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        setContentView(R.layout.activity_main);
        email1 = (EditText) findViewById(R.id.email1);
        password1 = (EditText) findViewById(R.id.password1);
        name1 = (EditText) findViewById(R.id.name1);
        login1 = (Button) findViewById(R.id.Login1);
        login1.setAlpha(0.5f);
        signup1 = (Button) findViewById(R.id.signup1);
        signup1.setAlpha(0.5f);
        mAuth = FirebaseAuth.getInstance();
        login1.setOnClickListener(this);
        signup1.setOnClickListener(this);
        email1.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Login1:
                login1.setAlpha(1);
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.signup1:
                signup1.setAlpha(1);
                RegisterUser();
                break;
            case R.id.email1:
                email1.setAlpha(1);
            case R.id.password1:
                password1.setAlpha(1);
            case R.id.name1:
                name1.setAlpha(1);
        }
    }

    private void RegisterUser() {
        String email = email1.getText().toString().trim();
        String password = password1.getText().toString().trim();
        String fullName = name1.getText().toString().trim();
        if (fullName.isEmpty()) {
            name1.requestFocus();
            name1.setError("Please fill the required detail!");
        }
        if (email.isEmpty()) {
            email1.requestFocus();
            email1.setError("Please fill the required detail!");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email1.requestFocus();
            email1.setError("Please enter valid email!");
            return;
        }
        if (password.isEmpty()) {
            password1.requestFocus();
            password1.setError("Please fill the required detail!");
            return;
        }
        if (password.length() < 6) {
            password1.requestFocus();
            password1.setError("Minimum 6 characters!");
            return;
        }
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                User user = new User(email, fullName);
                                FirebaseDatabase.getInstance().getReference("users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            startActivity(new Intent(
                                                    MainActivity.this, MainPlayer.class));
                                            signup1.setAlpha(1);
                                            finish();

                                        } else {
                                            Toast.makeText(MainActivity.this, "Failed! Try again later.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            } else {
                                Toast.makeText(MainActivity.this, "Failed to register user.", Toast.LENGTH_SHORT).show();
                            }
                        }


                    });

    }

   private void tomainplayer()
   {
       startActivity(new Intent(MainActivity.this, MainPlayer.class));
       finish();
   }


    @CallSuper
    public void onStart() {
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
        FirebaseUser fuser = mAuth.getCurrentUser();
        if (fuser != null) {

          tomainplayer();
        }

    }

}