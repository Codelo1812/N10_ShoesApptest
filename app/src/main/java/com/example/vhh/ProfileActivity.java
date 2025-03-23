package com.example.vhh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfileActivity extends AppCompatActivity {
    Button btnSetting;
    Button btnOrder;
    Button buttonQrcode;
    Button  btnEvent;
    Button btnEditPro;
    ImageView order;
    ImageView qrcode;
    ImageView eventcalendar;
    ImageView setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        btnSetting = findViewById(R.id.btnSetting);
        btnOrder = findViewById(R.id.btnOrder);
        buttonQrcode = findViewById(R.id.buttonQrcode);
        btnEvent = findViewById(R.id.btnEvent);
        btnEditPro = findViewById(R.id.btnEditPro);
        order = findViewById(R.id.order);
        qrcode = findViewById(R.id.qrcode);
        eventcalendar = findViewById(R.id.eventcalendar);
        setting = findViewById(R.id.setting);
        btnSetting.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, SettingActivity.class);
            startActivity(intent);
        });
        btnOrder.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, OrderActivity.class);
            startActivity(intent);
        });
        buttonQrcode.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, QRCodeActivity.class);
            startActivity(intent);
        });
        btnEvent.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EventActivity.class);
            startActivity(intent);
        });
        btnEditPro.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });
        order.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, OrderActivity.class);
            startActivity(intent);
        });
        eventcalendar.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EventActivity.class);
            startActivity(intent);
        });
        setting.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, SettingActivity.class);
            startActivity(intent);
        });
        qrcode.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, QRCodeActivity.class);
            startActivity(intent);
        });
    }
}