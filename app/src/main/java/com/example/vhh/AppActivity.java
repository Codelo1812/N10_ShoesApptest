package com.example.vhh;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AppActivity extends AppCompatActivity implements AppAdapter.ItemClickListener {
    private RecyclerView recyclerView;
    private AppAdapter adapter;
    private List<App> appList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        // Initialize app list
        appList = new ArrayList<>();
        initAppList();

        // Set up the RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4)); // 4 columns in grid
        adapter = new AppAdapter(this, appList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void initAppList() {
        appList.add(new App("Shoe store", R.drawable.app_icon_desc));

        // Add more apps...
    }

    @Override
    public void onItemClick(View view, int position) {
        App app = adapter.getItem(position);
        Toast.makeText(this, "You clicked " + app.getName(), Toast.LENGTH_SHORT).show();
        // Handle click event, for example, start a new activity
        // Intent intent = new Intent(this, AppDetailActivity.class);
        // intent.putExtra("app_name", app.getName());
        // startActivity(intent);
    }
}