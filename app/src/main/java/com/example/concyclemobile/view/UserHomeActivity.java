package com.example.concyclemobile.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.concyclemobile.adapter.PostAdapter;
import com.example.concyclemobile.model.Post;
import androidx.appcompat.app.AppCompatActivity;
import com.example.concyclemobile.R;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class UserHomeActivity extends AppCompatActivity{
    TextView textWelcome, textScore;
    Button buttonProfile, buttonCreatePost;
    RecyclerView recyclerView;
    PostAdapter adapter;
    List<Post> postList = new ArrayList<>();
    String userId, userName;
    int score;
    String postApiUrl = "http://10.0.2.2:5047/api/posts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        textWelcome = findViewById(R.id.textWelcome);
        textScore = findViewById(R.id.textScore);
        buttonProfile = findViewById(R.id.buttonProfile);
        buttonCreatePost = findViewById(R.id.buttonCreatePost);

        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("userName");
        score = getIntent().getIntExtra("userScore", 0);

        textWelcome.setText("Hoş geldin, " + userName + "!");
        textScore.setText("Katkı Puanı: " + score);

        recyclerView = findViewById(R.id.recyclerViewPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PostAdapter(postList, userId, userName);
        recyclerView.setAdapter(adapter);
        loadPosts();

        buttonProfile.setOnClickListener(v -> {
            Intent intent = new Intent(UserHomeActivity.this, ProfileActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("userName", userName);
            intent.putExtra("userScore", score);
            startActivity(intent);
        });

        buttonCreatePost.setOnClickListener(v -> {
            Intent intent = new Intent(UserHomeActivity.this, CreatePostActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("userName", userName);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPosts();
        loadUserScore();
    }

    private void loadPosts() {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                postApiUrl,
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
                            postList.add(0, post);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                },
                error -> Toast.makeText(this, "Postlar alınamadı", Toast.LENGTH_SHORT).show()
        );
        Volley.newRequestQueue(this).add(request);
    }

    private void loadUserScore() {
        String url = "http://10.0.2.2:5047/api/users/" + userId;

        JsonArrayRequest userRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://10.0.2.2:5047/api/users",
                null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject userObj = response.getJSONObject(i);
                            if (userObj.getString("id").equals(userId)) {
                                int updatedScore = userObj.getInt("score");
                                textScore.setText("Katkı Puanı: " + updatedScore);
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> Toast.makeText(this, "Puan güncellenemedi", Toast.LENGTH_SHORT).show()
        );
        Volley.newRequestQueue(this).add(userRequest);
    }
}
