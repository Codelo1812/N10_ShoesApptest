package com.example.vhh;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText searchEditText;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList; // Danh sách sản phẩm gốc
    private List<Product> filteredList; // Danh sách sản phẩm sau khi lọc

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchEditText = findViewById(R.id.search_edit_text);
        recyclerView = findViewById(R.id.recycler_view);

        // Khởi tạo danh sách sản phẩm gốc
        productList = new ArrayList<>();
        // Thêm các sản phẩm vào productList
        productList.add(new Product("Nike Air Max", "Running", "A comfortable running shoe with air cushioning.", 199.99, R.drawable.giay1, true,1));
        // Khởi tạo danh sách sản phẩm sau khi lọc (ban đầu là danh sách trống)
        filteredList = new ArrayList<>();

        // Khởi tạo adapter và RecyclerView
        productAdapter = new ProductAdapter(this, filteredList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productAdapter);

        // Thiết lập chức năng tìm kiếm
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Phương thức lọc sản phẩm theo từ khóa tìm kiếm
    private void filter(String query) {
        filteredList.clear();
        if (!query.isEmpty()) {
            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(query.toLowerCase()) ||
                        product.getDescription().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(product);
                }
            }
        }
        productAdapter.updateData(filteredList); // Cập nhật dữ liệu trong adapter
    }
}