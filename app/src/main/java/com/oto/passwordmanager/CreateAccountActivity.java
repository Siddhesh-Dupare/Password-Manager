package com.oto.passwordmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText titleEditText, emailEditText, passwordEditText;
    private Button saveButton;
    private Spinner iconSpinner;
    DatabaseReference databaseReference;
    AuthResult authResult;
    ArrayList<String> arrayList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        titleEditText = findViewById(R.id.create_title);
        emailEditText = findViewById(R.id.create_email);
        passwordEditText = findViewById(R.id.create_password);
        saveButton = findViewById(R.id.create_save);
        iconSpinner = findViewById(R.id.icon_spinner);
        spinnerDropDown();
        AndroidUtil.fieldEmptyOrNot(passwordEditText, "Password");
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertData();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
    private void InsertData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        String title = titleEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        Account account = new Account(title, email, password);
        databaseReference.child("users").child(uid).child(title).setValue(account).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CreateAccountActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateAccountActivity.this, "Not Inserted", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void spinnerDropDown() {
        String[] dataList = new String[] {
                "Google",
                "Facebook"
        };
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.dropdownlist, dataList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        iconSpinner.setAdapter(adapter);
    }
}