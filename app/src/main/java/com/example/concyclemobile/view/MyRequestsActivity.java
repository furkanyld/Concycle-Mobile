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
import com.example.concyclemobile.adapter.ConRequestAdapter;
import com.example.concyclemobile.model.ConRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyRequestsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ConRequestAdapter adapter;
    List<ConRequest> requestList = new ArrayList<>();
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);

        userId = getIntent().getStringExtra("userId");
        recyclerView = findViewById(R.id.recyclerViewRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ConRequestAdapter(requestList);
        recyclerView.setAdapter(adapter);

        loadRequests();
    }

    private void loadRequests() {
        String apiUrl = "http://10.0.2.2:5047/api/conrequests/applicant/" + userId;

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                apiUrl,
                null,
                response -> {
                    requestList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            ConRequest con = new ConRequest();
                            con.setPostTitle(obj.getString("postTitle"));
                            con.setMessage(obj.getString("message"));
                            con.setStatus(obj.getString("status"));
                            con.setIsCompleted(obj.getBoolean("isCompleted"));
                            requestList.add(con);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                },
                error -> Toast.makeText(this, "Başvurular alınamadı", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }
}
