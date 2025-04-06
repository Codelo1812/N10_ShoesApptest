package com.example.vhh;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ManageUserActivity extends AppCompatActivity {

    private ArrayList<String> users;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);

        ListView lvUsers = findViewById(R.id.lvUsers);
        Button btnAddUser = findViewById(R.id.btnAddUser);

        // Dummy data
        users = new ArrayList<>();
        users.add("user1@example.com");
        users.add("user2@example.com");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, users);
        lvUsers.setAdapter(adapter);

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });

        lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editUser(position);
            }
        });

        lvUsers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteUser(position);
                return true;
            }
        });
    }

    private void addUser() {
        // Logic to add a new user
        // Show a dialog to enter user details
    }

    private void editUser(int position) {
        // Logic to edit user at the given position
        // Show a dialog to edit user details
    }

    private void deleteUser(final int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete this user?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        users.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}