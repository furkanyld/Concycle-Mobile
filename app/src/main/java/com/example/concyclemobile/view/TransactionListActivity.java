package com.example.concyclemobile.view;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.concyclemobile.R;
import com.example.concyclemobile.adapter.TransactionAdapter;
import com.example.concyclemobile.model.Transaction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TransactionListActivity extends AppCompatActivity {

    RecyclerView recyclerTransactions;
    List<Transaction> transactionList = new ArrayList<>();
    TransactionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);

        recyclerTransactions = findViewById(R.id.recyclerTransactions);
        recyclerTransactions.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TransactionAdapter(transactionList);
        recyclerTransactions.setAdapter(adapter);

        loadTransactions();
    }

    private void loadTransactions() {
        String url = "http://10.0.2.2:5047/api/transaction";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    transactionList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            Transaction transaction = new Transaction();
                            transaction.setDescription(obj.optString("description", "Puan işlemi"));
                            transaction.setScore(obj.getInt("score"));
                            transactionList.add(transaction);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                },
                error -> Toast.makeText(this, "Puan işlemleri alınamadı", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }
}
