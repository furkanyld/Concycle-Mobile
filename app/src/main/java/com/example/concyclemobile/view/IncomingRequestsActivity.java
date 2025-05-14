package com.example.concyclemobile.view;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.concyclemobile.R;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.concyclemobile.adapter.IncomingRequestAdapter;
import com.example.concyclemobile.model.ConRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IncomingRequestsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<ConRequest> incomingRequests = new ArrayList<>();
    IncomingRequestAdapter adapter;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_requests);

        userId = getIntent().getStringExtra("userId");
        recyclerView = findViewById(R.id.recyclerIncomingRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new IncomingRequestAdapter(incomingRequests);
        recyclerView.setAdapter(adapter);

        loadUserPostRequests();
    }

    private void loadUserPostRequests() {
        String url = "http://10.0.2.2:5047/api/posts/user/" + userId;

        JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject postObj = response.getJSONObject(i);
                            String postId = postObj.getString("id");

                            String requestUrl = "http://10.0.2.2:5047/api/conrequests/post/" + postId;

                            JsonArrayRequest conRequest = new JsonArrayRequest(Request.Method.GET, requestUrl, null,
                                    conResponse -> {
                                        for (int j = 0; j < conResponse.length(); j++) {
                                            try {
                                                JSONObject obj = conResponse.getJSONObject(j);
                                                ConRequest req = new ConRequest();
                                                req.setId(obj.getString("id"));
                                                req.setPostTitle(obj.getString("postTitle"));
                                                req.setMessage(obj.getString("message"));
                                                req.setStatus(obj.getString("status"));
                                                req.setIsCompleted(obj.getBoolean("isCompleted"));
                                                incomingRequests.add(req);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        adapter.notifyDataSetChanged();
                                    },
                                    error -> Log.e("ERROR", "Başvuru alınamadı"));
                            Volley.newRequestQueue(this).add(conRequest);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> Log.e("ERROR", "Postlar alınamadı")
        );
        Volley.newRequestQueue(this).add(postRequest);
    }
}
