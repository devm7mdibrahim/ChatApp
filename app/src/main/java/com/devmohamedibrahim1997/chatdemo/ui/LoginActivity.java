package com.devmohamedibrahim1997.chatdemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.devmohamedibrahim1997.chatdemo.R;
import com.devmohamedibrahim1997.chatdemo.databinding.ActivityLoginBinding;
import com.devmohamedibrahim1997.chatdemo.ui.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


public class LoginActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    static LoginActivity loginActivity;
    ActivityLoginBinding loginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        loginActivity = this;
        loginBinding.loginButton.setOnClickListener(view -> logIn());
        loginBinding.loginRegisterButton.setOnClickListener(view -> register());
    }

    public void logIn() {
        loginBinding.loginButton.setEnabled(false);

        String email = loginBinding.loginEmailTextInputLayout.getEditText().getText().toString().trim();
        String password = loginBinding.loginPassWordTextInputLayout.getEditText().getText().toString().trim();

        if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {
            mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainActivity.class));
                LoginActivity.this.finish();
            }).addOnFailureListener(e -> {
                Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                loginBinding.loginButton.setEnabled(true);
            });


        } else {
            Toast.makeText(LoginActivity.this, "Email and password can't be empty", Toast.LENGTH_LONG).show();
            loginBinding.loginButton.setEnabled(true);
        }
    }

    public void register() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    public static LoginActivity getInstance() {
        return loginActivity;
    }
}
