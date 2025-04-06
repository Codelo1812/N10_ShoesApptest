package com.example.vhh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {
    EditText edUserEmail, edPassWord;
    Button btnLogin, btnRegister;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            checkUserRole(currentUser.getUid());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");

        edUserEmail = findViewById(R.id.edUserEmail);
        edPassWord = findViewById(R.id.edPassWord);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        mAuth  = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnLogin.setOnClickListener(nhanvaoLogin());
        btnRegister.setOnClickListener(nhanvaoRegister());

        Intent intent = getIntent();
        if(intent!=null){
            Bundle ex = intent.getExtras();
            if(ex!=null){
                edUserEmail.setText(ex.getString("email"));
                edPassWord.setText(ex.getString("password"));
            }
        }
    }

    @NonNull
    private View.OnClickListener nhanvaoLogin() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(edUserEmail.getText());
                String password = String.valueOf(edPassWord.getText());
                if(email.isEmpty()||password.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Khong duoc bo trong! ",Toast.LENGTH_SHORT).show();
                }
                if(!isValidEmail(email)){
                    Toast.makeText(LoginActivity.this,"Dia chi email khong hop le!",Toast.LENGTH_SHORT).show();
                }
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user !=null){
                                checkUserRole(user.getUid());
                            }
                        }else{
                            Toast.makeText(LoginActivity.this,"Sai tai khoan hoac mat khau!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
    }
    
    @NonNull
    private View.OnClickListener nhanvaoRegister() {
        return view -> {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
        };
    }
    private void checkUserRole(String userId){
        db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){
                String role = documentSnapshot.getString("role");
                Intent intent;
                if("admin".equals(role)){
                    intent = new Intent(LoginActivity.this, AdminActivity.class);
                }
                else{
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        })
                .addOnFailureListener(e -> {Toast.makeText(LoginActivity.this,"Loi khi lay du lieu tai khoan!",Toast.LENGTH_SHORT).show();
        });
    }

    private boolean isValidEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}