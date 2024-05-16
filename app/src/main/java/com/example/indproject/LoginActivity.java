package com.example.indproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText editEmail, editPassword;
    Button btnLogin;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DBHelper(this);

        TextView btn=findViewById(R.id.textViewSignup);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.button2);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCredentials();
            }
        });

        btn.setOnClickListener((v) -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void checkCredentials() {
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        if (email.isEmpty() || !email.contains("@")) {
            showError(editEmail, "Email address is not valid!");
        } else if (password.isEmpty() || password.length() < 7) {
            showError(editPassword, "Password must be at least 7 characters.");
        } else {
            boolean userExists = dbHelper.checkUser(email, password);
            if (userExists) {
                Intent intent = new Intent(LoginActivity.this, TodoActivity.class);
                intent.putExtra("EMAIL", email); // Pass the email to the TodoActivity
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid credentials, please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}
