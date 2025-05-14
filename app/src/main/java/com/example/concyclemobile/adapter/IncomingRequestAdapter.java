package com.example.concyclemobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.concyclemobile.R;
import com.example.concyclemobile.model.ConRequest;

import java.util.List;

public class IncomingRequestAdapter extends RecyclerView.Adapter<IncomingRequestAdapter.RequestViewHolder> {

    private List<ConRequest> requestList;

    public IncomingRequestAdapter(List<ConRequest> requestList) {
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_incoming_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        ConRequest request = requestList.get(position);

        holder.textPostTitle.setText("📌 İlan: " + request.getPostTitle());
        holder.textMessage.setText("💬 Mesaj: " + request.getMessage());
        holder.textStatus.setText("📎 Durum: " + request.getStatus());

        if (!request.getStatus().equalsIgnoreCase("Pending")) {
            holder.buttonAccept.setVisibility(View.GONE);
            holder.buttonReject.setVisibility(View.GONE);
        } else {
            holder.buttonAccept.setVisibility(View.VISIBLE);
            holder.buttonReject.setVisibility(View.VISIBLE);
        }

        // Kabul Et Butonu
        holder.buttonAccept.setOnClickListener(v -> {
            String url = "http://10.0.2.2:5047/api/conrequests/" + request.getId() + "/status?status=Accepted";

            StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                    response -> {
                        request.setStatus("Accepted");
                        notifyItemChanged(position);
                        Toast.makeText(holder.itemView.getContext(), "Başvuru kabul edildi", Toast.LENGTH_SHORT).show();
                    },
                    error -> Toast.makeText(holder.itemView.getContext(), "Hata: " + error.getMessage(), Toast.LENGTH_LONG).show()
            );

            Volley.newRequestQueue(holder.itemView.getContext()).add(stringRequest);
        });

        // Reddet Butonu
        holder.buttonReject.setOnClickListener(v -> {
            String url = "http://10.0.2.2:5047/api/conrequests/" + request.getId() + "/status?status=Rejected";

            StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                    response -> {
                        request.setStatus("Rejected");
                        notifyItemChanged(position);
                        Toast.makeText(holder.itemView.getContext(), "Başvuru reddedildi", Toast.LENGTH_SHORT).show();
                    },
                    error -> Toast.makeText(holder.itemView.getContext(), "Hata: " + error.getMessage(), Toast.LENGTH_LONG).show()
            );

            Volley.newRequestQueue(holder.itemView.getContext()).add(stringRequest);
        });

        // Yardım Tamamlandı
        if (request.getStatus().equalsIgnoreCase("Accepted") && !request.isCompleted()) {
            holder.buttonComplete.setVisibility(View.VISIBLE);

            holder.buttonComplete.setOnClickListener(v -> {
                String url = "http://10.0.2.2:5047/api/conrequests/" + request.getId() + "/complete";

                StringRequest completeRequest = new StringRequest(Request.Method.PUT, url,
                        response -> {
                            request.setIsCompleted(true);
                            notifyItemChanged(position);
                            Toast.makeText(holder.itemView.getContext(), "Yardım tamamlandı", Toast.LENGTH_SHORT).show();
                        },
                        error -> Toast.makeText(holder.itemView.getContext(), "Hata: " + error.getMessage(), Toast.LENGTH_LONG).show()
                );

                Volley.newRequestQueue(holder.itemView.getContext()).add(completeRequest);
            });
        } else {
            holder.buttonComplete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView textPostTitle, textMessage, textStatus;
        Button buttonAccept, buttonReject, buttonComplete;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            textPostTitle = itemView.findViewById(R.id.textPostTitle);
            textMessage = itemView.findViewById(R.id.textMessage);
            textStatus = itemView.findViewById(R.id.textStatus);
            buttonAccept = itemView.findViewById(R.id.buttonAccept);
            buttonReject = itemView.findViewById(R.id.buttonReject);
            buttonComplete = itemView.findViewById(R.id.buttonComplete);
        }
    }
}
