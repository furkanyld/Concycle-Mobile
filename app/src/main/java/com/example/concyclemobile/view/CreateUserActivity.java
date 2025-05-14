package com.example.concyclemobile.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.concyclemobile.R;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateUserActivity extends AppCompatActivity {
    EditText editTextName, editTextEmail, editTextPassword;
    Button buttonCreateUser;

    String apiUrl = "http://10.0.2.2:5047/api/users";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonCreateUser = findViewById(R.id.buttonCreateUser);

        buttonCreateUser.setOnClickListener(view -> {
            String name = editTextName.getText().toString();
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();

            JSONObject userJson = new JSONObject();
            try {
                userJson.put("name", name);
                userJson.put("email", email);
                userJson.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    apiUrl,
                    userJson,
                    response -> Toast.makeText(this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show(),
                    error -> {
                        String errorMsg = "Bilinmeyen hata";
                        if (error.networkResponse != null) {
                            errorMsg = "Hata kodu: " + error.networkResponse.statusCode;
                        }
                        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
                    }
            );

            Volley.newRequestQueue(this).add(request);
        });

    }
}
