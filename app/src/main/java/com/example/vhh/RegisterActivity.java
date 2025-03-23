package com.example.vhh;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.example.vhh.User;
import com.google.gson.Gson;

public class RegisterActivity extends AppCompatActivity {
    private EditText edUserNameC;
    private EditText edPassWordC;
    private EditText edConfirmPasswordC;
    private EditText edEmailC;
    private EditText edPhoneNumberC;
    private RadioGroup rbSex;
    private Button btnRegister;
    private ImageButton imbBack;
    private SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    private final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Register");
//Khai báo Shared Preferences
        sharedPreferences = getSharedPreferences(Utils.SHARE_PREFERENCES_APP,  Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
//Ánh xạ các component từ giao diện vào các biến cục bộ
        AnhXaDuLieu();
//Đăng kí sự kiện cho 2 nút
        TaoSuKien();
    }
    void AnhXaDuLieu() {
        edUserNameC = findViewById(R.id.edUserNameRe);
        edPassWordC = findViewById(R.id.edPassWordRe);
        edConfirmPasswordC = findViewById(R.id.edConfirmPassRe);
        edEmailC = findViewById(R.id.edEmailRe);
        edPhoneNumberC = findViewById(R.id.edPhoneRe);
        rbSex = findViewById(R.id.rgGioiTinh);
        btnRegister = findViewById(R.id.btnRegisterRe);
        imbBack = findViewById(R.id.imbBack);
    }
    void TaoSuKien() {
        btnRegister.setOnClickListener(v -> SuKienRegister());
        imbBack.setOnClickListener(v -> finish());
    }
    void SuKienRegister() {
        String userName = edUserNameC.getText().toString().trim();
        String password = edPassWordC.getText().toString().trim();
        String confirmPassword =
                edConfirmPasswordC.getText().toString().trim();
        String email = edEmailC.getText().toString().trim();
        String phone = edPhoneNumberC.getText().toString().trim();
//Nếu sex = 1 là nam, ngược lại nữ
        int sex = 1;
        boolean isValid = CheckUserName(userName) && CheckPassword(password,
                confirmPassword);
        if(isValid) {
//Khi dữ liệu hợp lệ, tạo đối tượng User để lưu vào Shared
            User newUser = new User();
            newUser.setUserName(userName);
            newUser.setPassWord(password);
            newUser.setEmail(email);
            newUser.setPhoneNumber(phone);
//Lấy thông tin radiobutton đang được checked
            int sexSelected = rbSex.getCheckedRadioButtonId();
            if(sexSelected == R.id.rbNu) //Giới tính nữ
            {
                sex = 0;
            }
            newUser.setSex(sex);

            String StrUser = gson.toJson(newUser);
            editor.putString(Utils.KEY_USER, StrUser);
            editor.commit();
//Thông báo đăng kí thành công bằng Toast
            Toast.makeText(RegisterActivity.this, "Sign Up Successfully", Toast.LENGTH_LONG).show();
//Hoàn thành
            finish();
        }
    }
    boolean CheckUserName(String username)
    {
        if(username.isEmpty()) {
            edUserNameC.setError("Please enter username");
            return false;
        }
        return true;
    }
    boolean CheckPassword(String password, String confirmPassword)
    {
        if(password.isEmpty()) {
            edPassWordC.setError("Please enter password");
            return false;
        }
        if(!password.equals(confirmPassword)) {
            edConfirmPasswordC.setError("Password is incorrect");
            return false;
        }
        return true;
    }
}