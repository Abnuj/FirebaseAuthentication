package com.abnuj.FirebaseMDJamal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ManageOTP extends AppCompatActivity {
    EditText opt;
    Button veryOtpbtn;
    String Phonenumber, Otpid;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_o_t_p);
        opt = findViewById(R.id.otped);
        veryOtpbtn = findViewById(R.id.verifybtn);
        Phonenumber = getIntent().getStringExtra("phonenumber");

        mAuth = FirebaseAuth.getInstance();

        initiateOtp();

       veryOtpbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(opt.getText().toString().isEmpty())
               {
                   Toast.makeText(ManageOTP.this, "Blank field cannot be processed", Toast.LENGTH_SHORT).show();
               }
               else if (opt.getText().toString().trim().length() == 6)
               {
                   Toast.makeText(ManageOTP.this, "Invalid Otp", Toast.LENGTH_SHORT).show();
               }
               else{
                   PhoneAuthCredential credential = PhoneAuthProvider.getCredential(Otpid, opt.getText().toString());
                   signInWithPhoneAuthCredential(credential);
               }
           }
       });
    }

    private void initiateOtp() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(Phonenumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(ManageOTP.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                Otpid = s;
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ManageOTP.this, "Sign in success", Toast.LENGTH_LONG).show();
                          startActivity(new Intent(ManageOTP.this,Dashboard.class));
                        } else {
                            Toast.makeText(ManageOTP.this, "Sign in Code Error", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}