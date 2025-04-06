package com.example.vhh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;

public class EditProfileActivity extends AppCompatActivity {
    ImageButton imbBackprofile;
    EditText ednameprofile;
    Button btnSave;
    private SharedPreferences sharedPreferences;
    private final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        // Khởi tạo SharedPreferences
        sharedPreferences = getSharedPreferences(Utils.SHARE_PREFERENCES_APP, Context.MODE_PRIVATE);

        // Ánh xạ các thành phần giao diện
        imbBackprofile = findViewById(R.id.imbBackprofile);
        ednameprofile = findViewById(R.id.ednameprofile);
        btnSave = findViewById(R.id.btnRegisterRe);

        // Hiển thị tên người dùng hiện tại
        loadCurrentUsername();

        // Xử lý sự kiện nút Back
        imbBackprofile.setOnClickListener(v -> {
            finish();
        });

        // Xử lý sự kiện nút Save
        btnSave.setOnClickListener(v -> {
            saveUsername();
        });
    }

    // Phương thức tải tên người dùng hiện tại
    private void loadCurrentUsername() {
        // Lấy tên hiện tại từ SharedPreferences
        String currentUsername = sharedPreferences.getString("CURRENT_USERNAME", "");

        // Nếu không có tên hiện tại, thử lấy từ thông tin đăng ký
        if (currentUsername.isEmpty()) {
            String userJson = sharedPreferences.getString(Utils.KEY_USER, "");
            if (!userJson.isEmpty()) {
                User user = gson.fromJson(userJson, User.class);
                if (user != null) {
                    currentUsername = user.getUserName();
                }
            }
        }

        // Hiển thị tên hiện tại trong EditText
        if (!currentUsername.isEmpty()) {
            ednameprofile.setText(currentUsername);
        }
    }

    // Phương thức lưu tên người dùng mới
    private void saveUsername() {
        String newUsername = ednameprofile.getText().toString().trim();

        // Kiểm tra tên mới có hợp lệ không
        if (newUsername.isEmpty()) {
            ednameprofile.setError("Enter Username");
            return;
        }

        // Lưu tên mới vào SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("CURRENT_USERNAME", newUsername);
        editor.apply();

        // Cập nhật User object nếu có
        String userJson = sharedPreferences.getString(Utils.KEY_USER, "");
        if (!userJson.isEmpty()) {
            User user = gson.fromJson(userJson, User.class);
            if (user != null) {
                user.setUserName(newUsername);
                editor.putString(Utils.KEY_USER, gson.toJson(user));
                editor.apply();
            }
        }

        // Hiển thị thông báo thành công
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();

        // Truyền kết quả về MainActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedUsername", newUsername);
        setResult(RESULT_OK, resultIntent);

        // Đóng Activity
        finish();
    }
}