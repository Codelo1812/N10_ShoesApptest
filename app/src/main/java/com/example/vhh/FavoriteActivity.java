package com.example.vhh;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorite);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private List<Product> fetchFavoriteProducts() {
        // Triển khai phương thức này để lấy sản phẩm yêu thích từ database hoặc SharedPreferences
        List<Product> favoriteProducts = new ArrayList<>();
        // Ví dụ: lấy từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        // Lấy danh sách sản phẩm từ SharedPreferences và thêm vào danh sách favoriteProducts
        // ...
        return favoriteProducts;
    }
}