package com.example.concyclemobile.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.concyclemobile.R;
import org.json.JSONObject;

public class CreatePostActivity extends AppCompatActivity {

    EditText editTitle, editDescription, editScore;
    Spinner spinnerType, spinnerCategory;
    Button buttonSubmit;
    String userId, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("userName");

        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        editScore = findViewById(R.id.editScore);
        spinnerType = findViewById(R.id.spinnerType);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Type spinner
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
                this, R.array.type_array, R.layout.spinner_item);
        typeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                this, R.array.category_skill, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int arrayId = spinnerType.getSelectedItem().toString().equals("skill")
                        ? R.array.category_skill
                        : R.array.category_help;

                ArrayAdapter<CharSequence> newCategoryAdapter = ArrayAdapter.createFromResource(
                        CreatePostActivity.this, arrayId, R.layout.spinner_item);
                newCategoryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinnerCategory.setAdapter(newCategoryAdapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        buttonSubmit.setOnClickListener(v -> {
            String title = editTitle.getText().toString();
            String description = editDescription.getText().toString();
            String type = spinnerType.getSelectedItem().toString();
            String category = spinnerCategory.getSelectedItem().toString();

            if (title.isEmpty() || description.isEmpty() || editScore.getText().toString().isEmpty()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
                return;
            }

            int scoreCost = Integer.parseInt(editScore.getText().toString());

            JSONObject postObj = new JSONObject();
            try {
                postObj.put("title", title);
                postObj.put("description", description);
                postObj.put("type", type);
                postObj.put("category", category);
                postObj.put("scoreCost", scoreCost);
                postObj.put("ownerId", userId);
                postObj.put("ownerName", userName);
            } catch (Exception e) {
                e.printStackTrace();
            }

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    "http://10.0.2.2:5047/api/posts",
                    postObj,
                    response -> {
                        Toast.makeText(this, "Post oluşturuldu!", Toast.LENGTH_SHORT).show();
                        finish(); // geri dön
                    },
                    error -> {
                            String message = "Hata oluştu!";
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                message = new String(error.networkResponse.data, "UTF-8");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    }
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            });

            Volley.newRequestQueue(this).add(request);
        });
    }
}
