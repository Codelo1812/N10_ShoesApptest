package com.example.vhh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView productImageView;
    private TextView productNameTextView;
    private TextView productDescriptionTextView;
    private TextView productPriceTextView;
    private RadioGroup sizeGroup;
    private EditText discountCodeEditText; // Trường nhập mã giảm giá
    private Button applyDiscountButton; // Nút áp dụng mã giảm giá
    private Button btnAddToCart;
    private Button btnBuyNow; // Nút mua hàng
    private Button btnBack; // Nút quay về
    private String selectedSize;
    private double originalPrice; // Giá gốc của sản phẩm

    // Danh sách mã giảm giá hợp lệ và tỷ lệ giảm giá tương ứng
    private Map<String, Double> discountCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productImageView = findViewById(R.id.product_img);
        productNameTextView = findViewById(R.id.productName);
        productDescriptionTextView = findViewById(R.id.productDescrip);
        productPriceTextView = findViewById(R.id.productPrice);
        sizeGroup = findViewById(R.id.sizeGroup);
        discountCodeEditText = findViewById(R.id.discountCode); // Tìm trường nhập mã giảm giá
        applyDiscountButton = findViewById(R.id.applyDiscount); // Tìm nút áp dụng mã giảm giá
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBuyNow = findViewById(R.id.btnBuyNow); // Tìm nút mua hàng
        btnBack = findViewById(R.id.btnBack); // Tìm nút quay về

        // Khởi tạo danh sách mã giảm giá hợp lệ
        discountCodes = new HashMap<>();
        discountCodes.put("DISCOUNT10", 0.10); // Giảm 10%
        discountCodes.put("DISCOUNT20", 0.20); // Giảm 20%
        discountCodes.put("DISCOUNT30", 0.30); // Giảm 30%

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        String productName = intent.getStringExtra("product_name");
        String productDescription = intent.getStringExtra("product_description");
        int productImageResId = intent.getIntExtra("product_image_res_id", 0);
        originalPrice = intent.getDoubleExtra("product_price", 0.0);

        // Hiển thị thông tin sản phẩm
        productImageView.setImageResource(productImageResId);
        productNameTextView.setText(productName);
        productDescriptionTextView.setText(productDescription);
        productPriceTextView.setText("Gía: $" + originalPrice);

        // Lắng nghe sự kiện chọn kích thước giày
        sizeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                selectedSize = selectedRadioButton.getText().toString();
                Toast.makeText(ProductDetailActivity.this, "chọn size: " + selectedSize, Toast.LENGTH_SHORT).show();
            }
        });

        // Lắng nghe sự kiện khi nhấn nút "Áp dụng mã giảm giá"
        applyDiscountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String discountCode = discountCodeEditText.getText().toString().trim();
                applyDiscountCode(discountCode); // Gọi phương thức áp dụng mã giảm giá
            }
        });

        // Lắng nghe sự kiện khi nhấn nút "Thêm vào giỏ hàng"
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedSize == null) {
                    Toast.makeText(ProductDetailActivity.this, "hãy chọn size giày", Toast.LENGTH_SHORT).show();
                } else {
                    // Lưu trữ thông tin sản phẩm trong SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("CartPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    // Lấy số lượng sản phẩm từ giao diện (giả sử bạn có một EditText để nhập số lượng)
                    int quantity = Integer.parseInt(quantityEditText.getText().toString());
                    String cartItems = sharedPreferences.getString("cart_items", "");

                    // Tạo chuỗi JSON cho sản phẩm mới
                    String newProduct = String.format("{\"name\":\"%s\",\"size\":\"%s\",\"price\":%f,\"imageResId\":%d,\"quantity\":%d}",
                            productName, selectedSize, originalPrice, productImageResId, quantity);

                    // Thêm sản phẩm vào chuỗi JSON hiện tại
                    if (!cartItems.isEmpty()) {
                        cartItems += ",";
                    }
                    cartItems += newProduct;

                    editor.putString("cart_items", "[" + cartItems + "]");
                    editor.apply();

                    // Thông báo thành công
                    Toast.makeText(ProductDetailActivity.this, "Thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();

                    // Chuyển về trang chủ (MainActivity)
                    Intent intent = new Intent(ProductDetailActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });

        // Lắng nghe sự kiện khi nhấn nút "Mua hàng"
        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedSize == null) {
                    Toast.makeText(ProductDetailActivity.this, "hãy chọn size giày", Toast.LENGTH_SHORT).show();
                } else {
                    // Xử lý logic mua hàng (giả lập)
                    Toast.makeText(ProductDetailActivity.this, "Purchasing: " + productName + " - Size: " + selectedSize, Toast.LENGTH_SHORT).show();
                    // Thông báo thành công
                    Toast.makeText(ProductDetailActivity.this, "Mua hàng thành công!", Toast.LENGTH_SHORT).show();
                    // Thêm logic mua hàng tại đây, ví dụ: điều hướng đến màn hình thanh toán
                }
            }
        });

        // Lắng nghe sự kiện khi nhấn nút "Quay về"
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Quay về màn hình trước đó
            }
        });
    }

    // Phương thức áp dụng mã giảm giá
    private void applyDiscountCode(String discountCode) {
        if (discountCodes.containsKey(discountCode)) {
            double discount = discountCodes.get(discountCode);
            double discountedPrice = originalPrice * (1 - discount);
            productPriceTextView.setText("Discounted Price: $" + discountedPrice);
            Toast.makeText(this, "Mã giảm giá áp dụng thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Mã giảm giá không hợp lệ!", Toast.LENGTH_SHORT).show();
        }
    }
}