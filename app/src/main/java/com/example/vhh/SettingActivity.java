package com.example.vhh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingActivity extends AppCompatActivity {
    Button btnLogout;
    Button btninfo;
    ImageButton imbbackst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
        btnLogout = findViewById(R.id.btnLogout);
        imbbackst = findViewById(R.id.imbbackst);
        btninfo = findViewById(R.id.btninfo);
        btnLogout.setOnClickListener(v -> {
            // Hiển thị hộp thoại xác nhận đăng xuất
            showLogoutConfirmation();
        });
        btninfo.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, InfoActivity.class);
            startActivity(intent);
        });
        // Xử lý sự kiện click nút quay lại
        imbbackst.setOnClickListener(v -> {
            finish(); // Đóng màn hình hiện tại và quay lại màn hình trước đó
        });

        // Thiết lập insets để tương thích với hệ thống (edge-to-edge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Hiển thị hộp thoại xác nhận đăng xuất
     */
    private void showLogoutConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Log out");
        builder.setMessage("Are you sure?");

        // Nút đồng ý đăng xuất
        builder.setPositiveButton("Log out", (dialog, which) -> {
            performLogout();
        });

        // Nút hủy
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });

        builder.show();
    }

    /**
     * Thực hiện đăng xuất
     */
    private void performLogout() {
        // Xóa dữ liệu người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Hiển thị thông báo đăng xuất thành công
        Toast.makeText(this, "Log out successfully", Toast.LENGTH_SHORT).show();

        // Chuyển về màn hình đăng nhập và xóa ngăn xếp hoạt động
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}