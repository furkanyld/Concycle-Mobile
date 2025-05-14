package com.example.concyclemobile.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.concyclemobile.R;
import com.example.concyclemobile.adapter.ConRequestAdapter;
import com.example.concyclemobile.adapter.PostAdapter;
import com.example.concyclemobile.adapter.TransactionAdapter;
import com.example.concyclemobile.model.ConRequest;
import com.example.concyclemobile.model.Post;
import com.example.concyclemobile.model.Transaction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    TextView textName, textEmail, textScore;
    Button buttonIncomingRequests;
    RecyclerView recyclerPosts, recyclerRequests, recyclerTransactions;
    String userId, userName;
    List<Post> postList = new ArrayList<>();
    List<ConRequest> requestList = new ArrayList<>();
    List<Transaction> transactionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("userName");

        textName = findViewById(R.id.textName);
        textEmail = findViewById(R.id.textEmail);
        textScore = findViewById(R.id.textScore);

        recyclerPosts = findViewById(R.id.recyclerPosts);
        recyclerRequests = findViewById(R.id.recyclerRequests);
        recyclerTransactions = findViewById(R.id.recyclerTransactions);

        recyclerPosts.setLayoutManager(new LinearLayoutManager(this));
        recyclerRequests.setLayoutManager(new LinearLayoutManager(this));
        recyclerTransactions.setLayoutManager(new LinearLayoutManager(this));

        loadUserInfo();
        loadUserPosts();
        loadUserRequests();
        loadUserTransactions();

        buttonIncomingRequests = findViewById(R.id.buttonIncomingRequests);
        buttonIncomingRequests.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, IncomingRequestsActivity.class);
            intent.putExtra("userId", userId); // kullanıcının id'siyle gelen başvurular alınacak
            startActivity(intent);
        });
    }

    private void loadUserInfo() {
        String url = "http://10.0.2.2:5047/api/users/" + userId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    textName.setText("👤 " + response.optString("name"));
                    textEmail.setText("✉️ " + response.optString("email"));
                    textScore.setText("🏆 Katkı Puanı: " + response.optInt("score"));
                },
                error -> Toast.makeText(this, "Kullanıcı bilgisi alınamadı", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void loadUserPosts() {
        String url = "http://10.0.2.2:5047/api/posts/user/" + userId;

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    postList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            Post post = new Post();
                            post.setId(obj.getString("id"));
                            post.setTitle(obj.getString("title"));
                            post.setDescription(obj.getString("description"));
                            post.setType(obj.getString("type"));
                            post.setCategory(obj.getString("category"));
                            post.setScoreCost(obj.getInt("scoreCost"));
                            post.setOwnerId(obj.getString("ownerId"));
                            post.setOwnerName(obj.getString("ownerName"));
                            postList.add(post);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    recyclerPosts.setAdapter(new PostAdapter(postList, userId, userName));
                },
                error -> Toast.makeText(this, "İlanlar alınamadı", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void loadUserRequests() {
        String url = "http://10.0.2.2:5047/api/conrequests/applicant/" + userId;

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
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
                    recyclerRequests.setAdapter(new ConRequestAdapter(requestList));
                },
                error -> Toast.makeText(this, "Başvurular alınamadı", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void loadUserTransactions() {
        String url = "http://10.0.2.2:5047/api/transaction/user/" + userId;

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    transactionList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            Transaction tr = new Transaction();
                            tr.setDescription(obj.optString("description", "Puan hareketi"));
                            tr.setScore(obj.getInt("score"));
                            transactionList.add(tr);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    recyclerTransactions.setAdapter(new TransactionAdapter(transactionList));
                },
                error -> Toast.makeText(this, "Puan geçmişi alınamadı", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserInfo();
    }
}
