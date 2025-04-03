package com.example.vhh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<Product> cartProductList;
    private TextView cartTotal;
    private TextView emptyCartMessage;
    private Button checkoutButton;
    private DatabaseReference cartDatabase;
    private TextView cartDiscountedTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartRecyclerView = findViewById(R.id.cart_recycler_view);
        cartTotal = findViewById(R.id.cart_total);
        emptyCartMessage = findViewById(R.id.empty_cart_message);
        checkoutButton = findViewById(R.id.checkout_button);
        cartDiscountedTotal = findViewById(R.id.cart_discounted_total);

        cartDatabase = FirebaseDatabase.getInstance().getReference("cart");

        cartProductList = new ArrayList<>();

        loadCartData();

        checkoutButton.setOnClickListener(v -> showPaymentForm());
    }

    private void loadCartData() {
        cartDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cartProductList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    cartProductList.add(product);
                }
                updateCartUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CartActivity.this, "Failed to load cart data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCartUI() {
        if (cartProductList.isEmpty()) {
            emptyCartMessage.setVisibility(View.VISIBLE);
            cartRecyclerView.setVisibility(View.GONE);
            checkoutButton.setVisibility(View.GONE);
            cartTotal.setVisibility(View.GONE);
            cartDiscountedTotal.setVisibility(View.GONE);
        } else {
            emptyCartMessage.setVisibility(View.GONE);
            cartRecyclerView.setVisibility(View.VISIBLE);
            checkoutButton.setVisibility(View.VISIBLE);
            cartTotal.setVisibility(View.VISIBLE);
            cartDiscountedTotal.setVisibility(View.VISIBLE);

            cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            cartAdapter = new CartAdapter(this, cartProductList);
            cartRecyclerView.setAdapter(cartAdapter);

            calculateTotal();
        }
    }

    public void calculateTotal() {
        double total = 0;
        double discountedTotal = 0;
        for (Product product : cartProductList) {
            total += product.getPrice() * product.getQuantity();
            discountedTotal += product.getDiscountedPrice() * product.getQuantity();
        }
        cartTotal.setText("Tổng cộng: " + total + " VND");
        cartDiscountedTotal.setText("Giá sau giảm: " + discountedTotal + " VND");
    }
    private void showPaymentForm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
        View view = getLayoutInflater().inflate(R.layout.layoutpayment, null);

        EditText nameEditText = view.findViewById(R.id.nameEditText);
        EditText addressEditText = view.findViewById(R.id.addressEditText);
        EditText phoneEditText = view.findViewById(R.id.phoneEditText);
        RadioButton cashRadioButton = view.findViewById(R.id.cashRadioButton);
        Button submitButton = view.findViewById(R.id.submitButton);

        builder.setView(view);
        AlertDialog dialog = builder.create();

        submitButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String address = addressEditText.getText().toString();
            String phone = phoneEditText.getText().toString();
            boolean isCash = cashRadioButton.isChecked();

            if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(CartActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                dialog.dismiss();
                processCheckout();
            }
        });

        dialog.show();
    }

    private void processCheckout() {
        cartDatabase.removeValue();
        Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
        loadCartData();
    }
}