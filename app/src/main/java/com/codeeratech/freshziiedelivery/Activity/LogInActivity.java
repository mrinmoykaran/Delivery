package com.codeeratech.freshziiedelivery.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codeeratech.freshziiedelivery.MainActivity;
import com.codeeratech.freshziiedelivery.R;
import com.codeeratech.freshziiedelivery.util.Session_management;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.codeeratech.freshziiedelivery.Config.BaseURL.dLogin;

public class LogInActivity extends AppCompatActivity {
    EditText Et_login_email, et_login_pass;
    RelativeLayout Btn_Sign_in;
    TextView tv_login_email;
    String getemail, getPass, deviceID;
    ProgressDialog progressDialog;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_log_in);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);

        token = FirebaseInstanceId.getInstance().getToken();

        preferences = getSharedPreferences("person", MODE_PRIVATE);
        editor = preferences.edit();

//        String token1 = FirebaseInstanceId.getInstance().getToken();
//        String token = SharedPref.getString(LogInActivity.this,SharedPrefManager.getInstance(LogInActivity.this).getDeviceToken());
        Et_login_email = (EditText) findViewById(R.id.et_login_email);
        et_login_pass = (EditText) findViewById(R.id.et_login_pass);
        tv_login_email = (TextView) findViewById(R.id.tv_login_email);
        Btn_Sign_in = (RelativeLayout) findViewById(R.id.btn_Sign_in);

        getemail = Et_login_email.getText().toString();
        getPass = et_login_pass.getText().toString();
        Btn_Sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Et_login_email.equals("")) {
                    Toast.makeText(LogInActivity.this, "Please enter valid ID", Toast.LENGTH_SHORT).show();
                } else if (et_login_pass.equals("")) {
                    Toast.makeText(LogInActivity.this, "Please enter valid Password", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    makeLoginRequest();

                }
            }
        });


    }


    private void makeLoginRequest() {
        progressDialog.show();
        final String UserName = Et_login_email.getText().toString().trim();
        final String UserPasswrd = et_login_pass.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, dLogin, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Log.d("Login", response);

                String msg = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    msg = jsonObject.getString("message");
                    if (status.equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject obj = jsonArray.getJSONObject(i);

                            String user_id = obj.getString("dboy_id");
                            String user_fullname = obj.getString("boy_name");
                            String phoenNo = obj.getString("boy_phone");
                            String user_password = obj.getString("password");
                            editor.putString("userid", user_id);
                            editor.putString("phoenNo", phoenNo);
                            editor.putString("password", user_password);
                            editor.commit();

                            Session_management sessionManagement = new Session_management(LogInActivity.this);
                            sessionManagement.createLoginSession(user_id, user_fullname, user_password);
                            MyFirebaseRegister myFirebaseRegister = new MyFirebaseRegister(LogInActivity.this);
                            myFirebaseRegister.RegisterUser(user_id);
                            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                            startActivity(intent);
                            Btn_Sign_in.setEnabled(false);
                            progressDialog.dismiss();
                            finish();
                        }
                    } else {
                        progressDialog.dismiss();
                        Btn_Sign_in.setEnabled(true);
                        Toast.makeText(LogInActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error [" + error + "]");

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone", UserName);
                params.put("password", UserPasswrd);
                params.put("device_id", token);

                // Log.d("fgh",UserName);
                //Log.d("fgh",UserPasswrd);
                // Log.d("fgh",deviceID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 2;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(stringRequest);


    }


}
