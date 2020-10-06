package com.codeeratech.freshziiedelivery.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codeeratech.freshziiedelivery.MainActivity;
import com.codeeratech.freshziiedelivery.R;
import com.codeeratech.freshziiedelivery.util.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.codeeratech.freshziiedelivery.Config.BaseURL.dRegister;

public class SignUp extends AppCompatActivity {
    EditText edName,edEmail,edPasword,edAddress;
    Button btnNext3;
    ProgressDialog progressDialog;
    String token;
    String deviceId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
        deviceId=getDeviceIMEI();
        btnNext3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerDriver();

            }
        });
    }

    private void registerDriver() {
        String name,email,password,address;
        name=String.valueOf(edName.getText());
        email=String.valueOf(edEmail.getText());
        password=String.valueOf(edPasword.getText());
        address=String.valueOf(edAddress.getText());

        if (name.isEmpty())
        {
            Toast.makeText(this, "Name required !", Toast.LENGTH_SHORT).show();
            edName.requestFocus();
            return;
        }
        if (email.isEmpty())
        {
            Toast.makeText(this, "Email required !", Toast.LENGTH_SHORT).show();
            edEmail.requestFocus();
            return;
        }
        if (password.isEmpty())
        {
            Toast.makeText(this, "Password required !", Toast.LENGTH_SHORT).show();
            edPasword.requestFocus();
            return;
        }
        if (address.isEmpty())
        {
            Toast.makeText(this, "Address required !", Toast.LENGTH_SHORT).show();
            edAddress.requestFocus();
            return;
        }

        requestRegister(name,email,password,address);

    }



    private void initView() {
        edName=findViewById(R.id.edName);
        edEmail=findViewById(R.id.edEmail);
        edPasword=findViewById(R.id.edPassword);
        edAddress=findViewById(R.id.edAddress);
        btnNext3=findViewById(R.id.btnNext3);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);

    }

    private void requestRegister(String name, String email, String password, String address)
    {
        progressDialog.show();
        String driver_url="https://gobobaka.com/admin/api/driver/driver_register";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,driver_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(SignUp.this, "Sucessed", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUp.this,DocumentsUpload.class));
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Connection Error"+error, Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("boy_name",name);
                params.put("boy_phone","0000000000");
                params.put("email",email);
                params.put("password",password);
                params.put("device_id", deviceId);
                params.put("address", address);

                return params;
            }
        };
        VolleySingleton.getInstance(SignUp.this).addToRequestQueue(stringRequest);
    }



    public String getDeviceIMEI() {
        String deviceUniqueIdentifier = "000000";
        return deviceUniqueIdentifier;
    }

}