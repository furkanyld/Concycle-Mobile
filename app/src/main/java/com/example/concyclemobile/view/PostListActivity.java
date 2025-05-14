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
import com.example.concyclemobile.adapter.PostAdapter;
import com.example.concyclemobile.model.Post;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PostListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PostAdapter adapter;
    List<Post> postList = new ArrayList<>();

    String apiUrl = "http://10.0.2.2:5047/api/posts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        recyclerView = findViewById(R.id.recyclerViewPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PostAdapter(postList, "", "");
        recyclerView.setAdapter(adapter);

        loadPosts();
    }

    private void loadPosts() {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                apiUrl,
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
                            postList.add(post);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                },
                error -> Toast.makeText(this, "Hata: " + error.getMessage(), Toast.LENGTH_LONG).show()
        );

        Volley.newRequestQueue(this).add(request);
    }
}
