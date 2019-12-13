package com.devmohamedibrahim1997.chatdemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.devmohamedibrahim1997.chatdemo.R;
import com.devmohamedibrahim1997.chatdemo.ui.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> {
            if (firebaseAuth.getCurrentUser() != null) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            SplashActivity.this.finish();
        }, 800);
    }
}
