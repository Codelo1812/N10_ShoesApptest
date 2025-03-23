package com.example.vhh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CartActivity extends AppCompatActivity {
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<Product> cartProductList;
    private TextView cartTotal;
    private TextView emptyCartMessage;
    private Button checkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartRecyclerView = findViewById(R.id.cart_recycler_view);
        cartTotal = findViewById(R.id.cart_total);
        emptyCartMessage = findViewById(R.id.empty_cart_message);
        checkoutButton = findViewById(R.id.checkout_button);

        // Đọc dữ liệu từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("CartPrefs", Context.MODE_PRIVATE);
        String cartItems = sharedPreferences.getString("cart_items", "[]");

        // Giả lập danh sách sản phẩm trong giỏ hàng
        cartProductList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(cartItems);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String productName = jsonObject.getString("name");
                double productPrice = jsonObject.getDouble("price");
                int productImageResId = jsonObject.getInt("imageResId");
                int quantity = jsonObject.getInt("quantity");

                cartProductList.add(new Product(productName, "Category", "Description", productPrice, productImageResId, true, quantity));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Kiểm tra giỏ hàng trống
        if (cartProductList.isEmpty()) {
            emptyCartMessage.setVisibility(View.VISIBLE);
            cartRecyclerView.setVisibility(View.GONE);
            checkoutButton.setVisibility(View.GONE);
            cartTotal.setVisibility(View.GONE);
        } else {
            emptyCartMessage.setVisibility(View.GONE);
            cartRecyclerView.setVisibility(View.VISIBLE);
            checkoutButton.setVisibility(View.VISIBLE);
            cartTotal.setVisibility(View.VISIBLE);

            // Thiết lập RecyclerView
            cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            cartAdapter = new CartAdapter(this, cartProductList, sharedPreferences);
            cartRecyclerView.setAdapter(cartAdapter);

            // Tính tổng giá trị giỏ hàng
            calculateTotal();
        }

        // Xử lý sự kiện nút thanh toán
        checkoutButton.setOnClickListener(v -> processCheckout());
    }

    public void calculateTotal() {
        double total = 0;
        for (Product product : cartProductList) {
            total += product.getPrice() * product.getQuantity(); // Tính tổng giá trị sản phẩm dựa trên số lượng
        }
        cartTotal.setText("Tổng cộng: " + total + " VND");
    }

    private void processCheckout() {
        // Logic xử lý thanh toán
        // Hiển thị thông báo thanh toán thành công
        Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();

        // Xóa thông tin sản phẩm trong SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("CartPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Chuyển hướng về trang chủ (MainActivity)
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Đóng CartActivity
    }
}