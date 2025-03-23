package com.example.vhh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
public class LoginActivity extends AppCompatActivity {
    EditText edUserName, edPassWord;
    Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");
//Ánh xạ
        edUserName = findViewById(R.id.edUserName);
        edPassWord = findViewById(R.id.edPassWord);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
//Bắt sự kiện khi user click lên 2 button
        btnLogin.setOnClickListener(nhanvaoLogin());
        btnRegister.setOnClickListener(nhanvaoRegister());

    }

    @NonNull
    private View.OnClickListener nhanvaoLogin() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//Lấy thông tin người dùng đã nhập

                String username = edUserName.getText().toString().trim();
                String password = edPassWord.getText().toString().trim();
                if (CheckUserName(username) && CheckPassword(password)) {
//Di chuyển từ Login qua Main

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                }
            }
        };
    }

    @NonNull
    private View.OnClickListener nhanvaoRegister() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        };
    }

    boolean CheckUserName(String username) {
        if (username.isEmpty()) {
            edUserName.setError("Please enter username");
            return false;
        }
        return true;
    }

    boolean CheckPassword(String password) {
        if (password.isEmpty()) {
            edPassWord.setError("Please enter password");
            return false;
        }
        return true;
    }
}