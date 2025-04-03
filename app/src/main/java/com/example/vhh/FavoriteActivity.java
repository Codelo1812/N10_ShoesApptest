package com.example.vhh;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> favoriteProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorite);
        // Initialize favorite product list
        favoriteProductList = new ArrayList<>();
        loadFavorites();

        // Set up the RecyclerView
        recyclerView = findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(this, favoriteProductList);
        recyclerView.setAdapter(adapter);
    }
    private void loadFavorites() {
        SharedPreferences sharedPreferences = getSharedPreferences("FavoritePrefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        // Lấy tất cả các sản phẩm yêu thích từ SharedPreferences
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String json = (String) entry.getValue();
            Product product = gson.fromJson(json, new TypeToken<Product>() {
            }.getType());
            favoriteProductList.add(product);

        }
    }
}