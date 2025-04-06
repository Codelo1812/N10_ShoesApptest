package com.example.vhh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.google.gson.Gson;

public class InfoActivity extends AppCompatActivity {
    private TextView tvUserName, tvEmail, tvPhoneNumber, tvGender;
    ImageButton imbbackinfo;
    private SharedPreferences sharedPreferences;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // Ánh xạ UI
        tvUserName = findViewById(R.id.tvUserName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvGender = findViewById(R.id.tvGender);
        imbbackinfo = findViewById(R.id.imbbackinfo);
        imbbackinfo.setOnClickListener(v -> {
            Intent intent = new Intent(InfoActivity.this,SettingActivity.class);
            startActivity(intent);
        });

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        String userJson = intent.getStringExtra("user");

        // Nếu không có dữ liệu từ Intent, lấy từ SharedPreferences
        if (userJson == null) {
            sharedPreferences = getSharedPreferences(Utils.SHARE_PREFERENCES_APP, Context.MODE_PRIVATE);
            userJson = sharedPreferences.getString(Utils.KEY_USER, null);
        }

        if (userJson != null) {
            try {
                User user = gson.fromJson(userJson, User.class);
                if (user != null) {
                    tvUserName.setText(user.getUserName());
                    tvEmail.setText(user.getEmail());
                    tvPhoneNumber.setText(user.getPhoneNumber());
                    tvGender.setText((user.getSex() == 1 ? "Male" : "Female"));
                } else {
                    Toast.makeText(this, "User data is corrupted!", Toast.LENGTH_SHORT).show();
                }
            } catch (JsonSyntaxException e) {
                Toast.makeText(this, "Failed to parse user data!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No user data found!", Toast.LENGTH_SHORT).show();
        }
    }
}