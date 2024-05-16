package com.example.indproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText editUsername, editEmail, editPw, editConfirmPw;
    private Button btnRegister;
    private TextView btn;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DBHelper(this);

        btn = findViewById(R.id.textViewAccount);
        editUsername = findViewById(R.id.editUsername);
        editEmail = findViewById(R.id.editEmail);
        editPw = findViewById(R.id.editPw);
        editConfirmPw = findViewById(R.id.editConfirmPw);
        btnRegister = findViewById(R.id.button);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCredentials();
            }
        });

        btn.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
    }

    private void checkCredentials() {
        String username = editUsername.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPw.getText().toString().trim();
        String confirmPassword = editConfirmPw.getText().toString().trim();

        if (username.isEmpty() || username.length() < 7) {
            showError(editUsername, "Your username is not valid!");
        } else if (email.isEmpty() || !email.contains("@")) {
            showError(editEmail, "Email address is not valid!");
        } else if (password.isEmpty() || password.length() < 7) {
            showError(editPw, "Password must be at least 7 characters.");
        } else if (confirmPassword.isEmpty() || !confirmPassword.equals(password)) {
            showError(editConfirmPw, "Password does not match.");
        } else {
            boolean isAdded = dbHelper.addUser(email, password);
            if (isAdded) {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showError(EditText field, String message) {
        field.setError(message);
        field.requestFocus();
    }
}
