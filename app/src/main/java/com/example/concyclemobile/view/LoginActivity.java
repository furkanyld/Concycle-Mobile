package com.example.concyclemobile.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.concyclemobile.R;
import com.example.concyclemobile.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button buttonLogin;

    String apiUrl = "http://10.0.2.2:5047/api/users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(v -> {
            String inputEmail = editTextEmail.getText().toString();
            String inputPassword = editTextPassword.getText().toString();

            JsonArrayRequest request = new JsonArrayRequest(
                    Request.Method.GET,
                    apiUrl,
                    null,
                    response -> {
                        boolean found = false;
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                String email = obj.getString("email");
                                String password = obj.getString("passwordHash");

                                if (inputEmail.equals(email) && inputPassword.equals(password)) {
                                    found = true;

                                    Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                                    intent.putExtra("userId", obj.getString("id"));
                                    intent.putExtra("userName", obj.getString("name"));
                                    intent.putExtra("userScore", obj.getInt("score"));
                                    startActivity(intent);
                                    finish();
                                    break;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if (!found) {
                            Toast.makeText(this, "E-posta veya şifre yanlış", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> Toast.makeText(this, "Giriş başarısız: " + error.getMessage(), Toast.LENGTH_SHORT).show()
            );
            Volley.newRequestQueue(this).add(request);
        });
    }
}
