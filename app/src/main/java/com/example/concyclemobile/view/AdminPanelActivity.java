package com.example.concyclemobile.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.concyclemobile.R;

public class AdminPanelActivity extends AppCompatActivity {

    Button buttonUsers, buttonPosts, buttonTransactions;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        buttonUsers = findViewById(R.id.buttonUsers);
        buttonPosts = findViewById(R.id.buttonPosts);
        buttonTransactions = findViewById(R.id.buttonTransactions);

        buttonTransactions.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this, TransactionListActivity.class);
            startActivity(intent);
        });

        buttonUsers.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this, UserListActivity.class);
            startActivity(intent);
        });

        buttonPosts.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this, PostListActivity.class);
            startActivity(intent);
        });
    }
}
