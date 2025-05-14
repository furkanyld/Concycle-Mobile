package com.example.concyclemobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.concyclemobile.R;
import com.example.concyclemobile.model.ConRequest;

import java.util.List;

public class ConRequestAdapter extends RecyclerView.Adapter<ConRequestAdapter.ConRequestViewHolder> {

    private List<ConRequest> requestList;

    public ConRequestAdapter(List<ConRequest> requestList) {
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public ConRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request, parent, false);
        return new ConRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConRequestViewHolder holder, int position) {
        ConRequest request = requestList.get(position);
        holder.textPostTitle.setText("📌 " + request.getPostTitle());
        holder.textMessage.setText("🗒️ " + request.getMessage());
        holder.textStatus.setText("📎 Durum: " + request.getStatus());

        boolean isCompleted = request.isCompleted();
        holder.textCompleted.setText("✅ Tamamlandı: " + (isCompleted ? "Evet" : "Hayır"));
        holder.textCompleted.setTextColor(
                holder.itemView.getContext().getColor(isCompleted ? R.color.green : R.color.red)
        );
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class ConRequestViewHolder extends RecyclerView.ViewHolder {
        TextView textPostTitle, textMessage, textStatus, textCompleted;

        public ConRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            textPostTitle = itemView.findViewById(R.id.textPostTitle);
            textMessage = itemView.findViewById(R.id.textMessage);
            textStatus = itemView.findViewById(R.id.textStatus);
            textCompleted = itemView.findViewById(R.id.textCompleted);
        }
    }
}
