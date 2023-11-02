package com.oto.passwordmanager;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private EditText fullNameTextView, emailTextView, passwordTextView, confirmPasswordTextView;
    private Button createAccountButton;
    private ProgressBar progressBar;
    private TextView signInActivity;
    FirebaseUser user;
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        fullNameTextView = findViewById(R.id.signup_full_name);
        emailTextView = findViewById(R.id.signup_email);
        passwordTextView = findViewById(R.id.signup_password);
        confirmPasswordTextView = findViewById(R.id.signup_confirm_password);
        createAccountButton = findViewById(R.id.signup_create_account);
        progressBar = findViewById(R.id.signup_progressbar);
        signInActivity = findViewById(R.id.signup_login_activity);
        progressBar.setVisibility(View.GONE);
        int color = getResources().getColor(R.color.primaryVariant);
        AndroidUtil.fieldEmptyOrNot(passwordTextView, "Password");
        AndroidUtil.fieldEmptyOrNot(confirmPasswordTextView, "Confirm Password");
        AndroidUtil.coloringTextView(signInActivity, color, 7);
        OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
        OnBackPressedCallback callBack = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setMessage("Do you want to exit?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        };
        dispatcher.addCallback(this, callBack);
        signInActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = fullNameTextView.getText().toString();
                String email = emailTextView.getText().toString();
                String password = passwordTextView.getText().toString();
                String confirmPassword = confirmPasswordTextView.getText().toString();
                if (fullName.isEmpty())
                    fullNameTextView.setError(AndroidUtil.field);
                else if (email.isEmpty())
                    emailTextView.setError(AndroidUtil.field);
                else if (password.isEmpty())
                    passwordTextView.setError(AndroidUtil.field);
                else if (confirmPassword.isEmpty())
                    confirmPasswordTextView.setError(AndroidUtil.field);
                else {
                    if (!isConnection()) {
                        AndroidUtil.toast(getApplicationContext(), "Please, connect to the internet");
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        signUpAuthentication();
                    }
                }
            }
        });

    }
    private void signUpAuthentication() {
        String email = emailTextView.getText().toString();
        String password = passwordTextView.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            progressBar.setVisibility(View.GONE);
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Account is not created", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
    private boolean isConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}