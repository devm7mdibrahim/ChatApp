package com.devmohamedibrahim1997.chatdemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.devmohamedibrahim1997.chatdemo.R;
import com.devmohamedibrahim1997.chatdemo.databinding.ActivityRegisterBinding;
import com.devmohamedibrahim1997.chatdemo.pojo.User;
import com.devmohamedibrahim1997.chatdemo.ui.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding registerBinding;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        initActionBar();
        registerBinding.registerButton.setOnClickListener(view -> {
            registerBinding.registerButton.setEnabled(false);
            getUserData();
            register(user.getUserName(),user.getEmail(),user.getPassWord(),user.getPassWord2());
        });

    }

    private void getUserData() {
        String mUserName = registerBinding.registerUserNameTextInputLayout.getEditText().getText().toString().trim();
        String mEmail = registerBinding.registerEmailTextInputLayout.getEditText().getText().toString().trim();
        String mPassword = registerBinding.registerPassWordTextInputLayout.getEditText().getText().toString().trim();
        String mPassword2 = registerBinding.registerReEnterPassWordTextInputLayout.getEditText().getText().toString().trim();
        user = new User(mUserName,mEmail,mPassword,mPassword2);
    }

    private void register(String userName, String email, String password, String password2) {
        if (password.equals(password2) && !TextUtils.isEmpty(email) &&
                !TextUtils.isEmpty(password) && !TextUtils.isEmpty(password2)) {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(
                    authResult -> {

                        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                        assert currentUser != null;
                        String userId = currentUser.getUid();

                        reference = FirebaseDatabase.getInstance().getReference("contacts").child(userId);

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id",userId);
                        hashMap.put("userName",userName);
                        hashMap.put("imageURL","default");

                        reference.setValue(hashMap)
                                .addOnSuccessListener(aVoid -> {
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            LoginActivity.getInstance().finish();
                            finish();
                        })
                                .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());


                    }

            ).addOnFailureListener(e -> {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                registerBinding.registerButton.setEnabled(true);
            });
        } else {
            Toast.makeText(this, "All fields are required!!", Toast.LENGTH_SHORT).show();
            registerBinding.registerButton.setEnabled(true);
        }
    }

    private void initActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Sign Up");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
