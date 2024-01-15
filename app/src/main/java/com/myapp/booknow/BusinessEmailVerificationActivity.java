package com.myapp.booknow;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BusinessEmailVerificationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Handler handler = new Handler();
    private final int CHECK_INTERVAL = 5000; // Check every 5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_email_verification);

        mAuth = FirebaseAuth.getInstance();

        TextView textViewStatus = findViewById(R.id.textViewStatus);
        textViewStatus.setText("Waiting for email verification...");

        // Start the handler to check email verification status
        handler.postDelayed(checkEmailVerificationRunnable, CHECK_INTERVAL);


        Button buttonResendEmail = findViewById(R.id.buttonResendEmail);
        buttonResendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationEmail();
            }
        });
    }

    private void resendVerificationEmail() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && !user.isEmailVerified()) {
            user.sendEmailVerification().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(BusinessEmailVerificationActivity.this, "Verification email sent.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BusinessEmailVerificationActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private Runnable checkEmailVerificationRunnable = new Runnable() {
        @Override
        public void run() {
            checkEmailVerification();
        }
    };

    private void checkEmailVerification() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.reload().addOnCompleteListener(task -> {
                if (user.isEmailVerified()) {
                    // Email is verified
                    handler.removeCallbacks(checkEmailVerificationRunnable);
                    Toast.makeText(BusinessEmailVerificationActivity.this, "Email verified!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), BusinessDashboardActivity.class));
                    finish();
                } else {
                    // Email not verified, check again after the interval
                    handler.postDelayed(checkEmailVerificationRunnable, CHECK_INTERVAL);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove callbacks when the activity is destroyed to avoid memory leaks
        handler.removeCallbacks(checkEmailVerificationRunnable);
    }
}