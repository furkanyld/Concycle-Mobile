package com.example.concyclemobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.concyclemobile.R;
import com.example.concyclemobile.model.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    List<Post> postList;
    String userId, userName;

    public PostAdapter(List<Post> postList, String userId, String userName) {
        this.postList = postList;
        this.userId = userId;
        this.userName = userName;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.textTitle.setText(post.getTitle());
        holder.textDescription.setText(post.getDescription());
        holder.textScore.setText("Katkı Puanı: " + post.getScoreCost());
        holder.textOwner.setText("Sahibi: " + post.getOwnerName());

        String typeLabel;
        switch (post.getType().toLowerCase()) {
            case "skill":
                typeLabel = "Yetenek Paylaşımı";
                break;
            case "help":
                typeLabel = "Yardım Talebi";
                break;
            default:
                typeLabel = post.getType();
                break;
        }
        holder.textTypeCategory.setText(typeLabel + " / " + post.getCategory());

        if (post.getOwnerId() != null && post.getOwnerId().equals(userId)) {
            holder.buttonApply.setVisibility(View.GONE);
        } else {
            holder.buttonApply.setVisibility(View.VISIBLE);
            holder.buttonApply.setOnClickListener(v -> {
                String postId = post.getId();
                String apiUrl = "http://10.0.2.2:5047/api/conrequests";

                JSONObject conRequest = new JSONObject();
                try {
                    conRequest.put("postId", postId);
                    conRequest.put("applicantId", userId);
                    conRequest.put("message", "Mobil uygulama üzerinden başvuru yapıldı.");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST,
                        apiUrl,
                        conRequest,
                        response -> Toast.makeText(v.getContext(), "Başvuru gönderildi", Toast.LENGTH_SHORT).show(),
                        error -> Toast.makeText(v.getContext(), "Hata: " + error.getMessage(), Toast.LENGTH_LONG).show()
                );

                Volley.newRequestQueue(v.getContext()).add(request);
            });
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textTypeCategory, textDescription, textScore, textOwner;
        Button buttonApply;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textTypeCategory = itemView.findViewById(R.id.textTypeCategory);
            textDescription = itemView.findViewById(R.id.textDescription);
            textScore = itemView.findViewById(R.id.textScore);
            textOwner = itemView.findViewById(R.id.textOwner);
            buttonApply = itemView.findViewById(R.id.buttonApply);
        }
    }
}
