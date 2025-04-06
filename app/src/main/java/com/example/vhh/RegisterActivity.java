package com.example.vhh;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText edUserNameC, edPassWordC, edConfirmPasswordC, edEmailC, edPhoneNumberC;
    private RadioGroup rbSex;
    private Button btnRegister;
    private ImageButton imbBack;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Register");

        initViews();
        initEvents();
    }

    private void initViews() {
        edUserNameC = findViewById(R.id.edUserNameRe);
        edPassWordC = findViewById(R.id.edPassWordRe);
        edConfirmPasswordC = findViewById(R.id.edConfirmPassRe);
        edEmailC = findViewById(R.id.edEmailRe);
        edPhoneNumberC = findViewById(R.id.edPhoneRe);
        rbSex = findViewById(R.id.rgGioiTinh);
        btnRegister = findViewById(R.id.btnRegisterRe);
        imbBack = findViewById(R.id.imbBack);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    private void initEvents() {
        btnRegister.setOnClickListener(v -> registerUser());
        imbBack.setOnClickListener(v -> finish());
    }

    private void registerUser() {
        String userName = edUserNameC.getText().toString().trim();
        String password = edPassWordC.getText().toString().trim();
        String confirmPassword = edConfirmPasswordC.getText().toString().trim();
        String email = edEmailC.getText().toString().trim();
        String phone = edPhoneNumberC.getText().toString().trim();
        int sex = (rbSex.getCheckedRadioButtonId() == R.id.rbNu) ? 0 : 1;

        if (!validateInput(userName, password, confirmPassword, email, phone)) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("username", userName);
                            userData.put("email", email);
                            userData.put("phone", phone);
                            userData.put("sex", sex);
                            userData.put("role", "user");

                            fStore.collection("users").document(uid)
                                    .set(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, "Sign Up Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(this, LoginActivity.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean validateInput(String username, String password, String confirmPassword, String email, String phone) {
        if (username.isEmpty()) {
            edUserNameC.setError("Please enter username");
            return false;
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edEmailC.setError("Invalid email");
            return false;
        }
        if (password.isEmpty() || password.length() < 6) {
            edPassWordC.setError("Password must be at least 6 characters");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            edConfirmPasswordC.setError("Passwords do not match");
            return false;
        }
        if (phone.isEmpty() || !phone.matches("^[0-9]{10,11}$")) {
            edPhoneNumberC.setError("Invalid phone number");
            return false;
        }
        return true;
    }
}