package com.example.vhh;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.ImageButton;
public class InfoActivity extends AppCompatActivity {

    ImageButton imbbackinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info);
        imbbackinfo = findViewById(R.id.imbbackinfo);
        imbbackinfo.setOnClickListener(v -> {
            Intent intent = new Intent(InfoActivity.this,SettingActivity.class);
        });
    }
}