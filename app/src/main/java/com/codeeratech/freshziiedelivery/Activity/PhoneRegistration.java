package com.codeeratech.freshziiedelivery.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codeeratech.freshziiedelivery.R;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneRegistration extends AppCompatActivity {
    EditText edMobile;
    Button btnNext1;
    private String verificationId;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_registration);

        initViews();
        if (String.valueOf(edMobile.getText())!="")
        {
            Intent intent=new Intent(PhoneRegistration.this,OtpVerify.class);
            intent.putExtra("mobile_no","8918523064");
            startActivity(intent);
        }

    }

    private void initViews() {
        edMobile = findViewById(R.id.edMobile);
        btnNext1 = findViewById(R.id.btnNext1);
        dialog=new ProgressDialog(this);
        dialog.setTitle("Sending OTP...");
    }

}