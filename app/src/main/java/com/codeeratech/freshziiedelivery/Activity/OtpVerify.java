package com.codeeratech.freshziiedelivery.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codeeratech.freshziiedelivery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpVerify extends AppCompatActivity {
    String mobileNo="";
    EditText edOtp;
    Button btnNext2;
    String verificationCodeBySystem="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);
        mobileNo=getIntent().getStringExtra("mobile_no");

        edOtp=findViewById(R.id.edOtp);
        btnNext2=findViewById(R.id.btnNext2);
        sendVerificationCode(mobileNo);

        btnNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                
            }
        });


    }

    private void sendVerificationCode(String mobileNo)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+mobileNo,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem=s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code=phoneAuthCredential.getSmsCode();
            if (code!=null)
            {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(OtpVerify.this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("ErrorMK",e.getMessage());
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationCodeBySystem,code);
        signUsingUserCredentials(credential);
        

    }

    private void signUsingUserCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(OtpVerify.this, "Verified", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendNext()
    {
        Intent intent=new Intent(OtpVerify.this, PhoneRegistration.class);
        intent.putExtra("mobile_no",getIntent().getStringExtra("mobile_no"));

    }

}