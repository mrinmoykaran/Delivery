package com.codeeratech.freshziiedelivery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codeeratech.freshziiedelivery.Config.BaseURL;
import com.codeeratech.freshziiedelivery.Fonts.CustomTypefaceSpan;
import com.codeeratech.freshziiedelivery.Fragment.Home;
import com.codeeratech.freshziiedelivery.Fragment.NextOrder;
import com.codeeratech.freshziiedelivery.Fragment.Order_history;
import com.codeeratech.freshziiedelivery.NetworkConnectivity.NoInternetConnection;
import com.codeeratech.freshziiedelivery.util.ConnectivityReceiver;
import com.codeeratech.freshziiedelivery.util.CustomVolleyJsonRequest;
import com.codeeratech.freshziiedelivery.util.Session_management;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static BottomNavigationView navigation;
    ImageView imageView;
    TextView mTitle;
    Toolbar toolbar;
    TextView locationText;
    int padding = 0;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Handler handler;
    double lng;
    double lat;
    TextView _latitude, _longitude;
    LocationManager locationManager;
    String userid;
    private ImageView iv_profile;
    private Menu nav_menu;
    private TextView tv_name;
    private Bitmap bitmap;
    private Session_management sessionManagement;
    private SwitchCompat sw;
    private TextView status;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor2;
    private ProgressDialog progressDialog;
    private String delivery_boy_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("person", MODE_PRIVATE);
        sharedPreferences = getSharedPreferences("check", 0);
        sessionManagement = new Session_management(MainActivity.this);
        progressDialog = new ProgressDialog(MainActivity.this);
        editor2 = sharedPreferences.edit();
        editor = preferences.edit();
        delivery_boy_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        userid = preferences.getString("userid", "");
        toolbar = findViewById(R.id.toolbar);
        sw = findViewById(R.id.switch_but);
        status = findViewById(R.id.status);
        setSupportActionBar(toolbar);

        sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                if (isChecked) {
                    status("1", true);
                    // The toggle is enabled
                } else {
                    status("0", true);

                    // The toggle is disabled
                }
            }
        });

        String check_onoff = sharedPreferences.getString("status", "");
//        status(check_onoff, false);

        if (sessionManagement.isLoggedIn()) {
            if (check_onoff != null) {
                if (check_onoff.contains("1")) {
                    sw.setChecked(true);
                    status.setText("Active");
                } else if (check_onoff.contains("0")) {
                    sw.setChecked(false);
                    status.setText("InActive");
                }
            }

        } else {
            editor.clear();
        }

        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);

            if (view instanceof TextView) {
                TextView textView = (TextView) view;

                Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "Font/Bold.otf");
                textView.setTypeface(myCustomFont);
            }

        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        }

        new Thread(this::getCurrency).start();
//        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        Menu m = navigationView.getMenu();

        for (
                int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            applyFontToMenuItem(mi);
        }
        View headerView = navigationView.getHeaderView(0);
        navigationView.getBackground().setColorFilter(0x80000000, PorterDuff.Mode.MULTIPLY);
        navigationView.setNavigationItemSelectedListener(this);
        nav_menu = navigationView.getMenu();
        View header = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
        iv_profile = header.findViewById(R.id.iv_header_img);

        tv_name = header.findViewById(R.id.tv_header_name);
        updateHeader();
        sideMenu();
        // initComponent();


//        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
//
//        }


//        handler = new Handler();
//        Runnable mStatusChecker = new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    getLocation();
//                } catch (Exception e) {
//                    Log.e("tag", e.getMessage());
//                }
//                handler.postDelayed(this, 3000);
//            }
////                            handler.postDelayed(mStatusChecker, 3000);
//        };
//        mStatusChecker.run();


        if (savedInstanceState == null) {
//            TodayOrder tm = new TodayOrder();
            Home tm = new Home();
            FragmentManager manager21 = getSupportFragmentManager();
            FragmentTransaction transaction21 = manager21.beginTransaction();
            transaction21.replace(R.id.contentPanel, tm);
            transaction21.commit();
        }
//        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//                    @Override
//                    public void onBackStackChanged() {
//                        try {
////                            InputMethodManager inputMethodManager = (InputMethodManager)
////                                    getSystemService(Context.INPUT_METHOD_SERVICE);
////                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//                            Fragment fr = getFragmentManager().findFragmentById(R.id.contentPanel);
//                            if (fr!=null){
//                                final String fm_name = fr.getClass().getSimpleName();
//                                Log.e("backstack: ", ": " + fm_name);
//                                if (fm_name.contentEquals("Home_fragment")) {
//                                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//                                    toggle.setDrawerIndicatorEnabled(true);
//                                    if (getSupportActionBar()!=null) {
//                                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//                                    }
//                                    toggle.syncState();
//
//                                } else if (fm_name.contentEquals("My_order_fragment") ||
//                                        fm_name.contentEquals("Thanks_fragment")) {
//                                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//
//                                    toggle.setDrawerIndicatorEnabled(false);
//                                    if (getSupportActionBar()!=null){
//                                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//                                    }
//
//                                    toggle.syncState();
//                                    toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            TodayOrder tm = new TodayOrder();
//                                            androidx.fragment.app.FragmentManager manager21 = getSupportFragmentManager();
//                                            androidx.fragment.app.FragmentTransaction transaction21 = manager21.beginTransaction();
//                                            transaction21.replace(R.id.contentPanel, tm);
//                                            transaction21.commit();
//                                        }
//                                    });
//                                } else {
//                                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//                                    toggle.setDrawerIndicatorEnabled(false);
//                                    if (getSupportActionBar()!=null){
//                                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//                                    }
//
//                                    toggle.syncState();
//                                    toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//
//                                            onBackPressed();
//                                        }
//                                    });
//                                }
//                            }
//
//
//                        } catch (NullPointerException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });

    }


    private void status(final String check, final boolean statusProgress) {
        if (statusProgress) {
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        String tag_json_obj = "json_socity_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("dboy_id", delivery_boy_id);

        params.put("status", check);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.update_status, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, response.toString());
                try {

                    String value = response.getString("status");
                    String msg = response.getString("message");

                    if (value.equalsIgnoreCase("1")) {
                        if (check.equalsIgnoreCase("1")) {
                            status.setText("Active");
                            sw.setChecked(true);
                        } else {
                            status.setText("Inactive");
                            sw.setChecked(false);
                        }
                        editor.putString("status", check).apply();
                        Toast.makeText(MainActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (statusProgress) {
                        progressDialog.dismiss();
                    }
                }
            }
        }, error -> {

            VolleyLog.d(TAG, "Error: " + error.getMessage());
            if (sharedPreferences.getString("status", "0").equalsIgnoreCase("1")) {
                status.setText("Active");
                sw.setChecked(true);
            } else {
                status.setText("Inactive");
                sw.setChecked(false);
            }
            if (statusProgress) {
                progressDialog.dismiss();
            }
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 1;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }

    public void updateHeader() {
        if (sessionManagement.isLoggedIn()) {
            String getname = sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
            tv_name.setText(getname);
        }
    }


    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "Font/Bold.otf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }


    public void sideMenu() {
        if (sessionManagement.isLoggedIn()) {
            nav_menu.findItem(R.id.nav_log_out).setVisible(true);
        } else {
//            tv_name.setText(getResources().getString(R.string.btn_login));
//            tv_name.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent i = new Intent(MainActivity.this, LogInActivity.class);
//                    startActivity(i);
//                }
//            });
            nav_menu.findItem(R.id.nav_log_out).setVisible(false);

        }
    }


    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void getCurrency() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BaseURL.currencyApi, response -> {

            try {

                JSONObject currencyObject = new JSONObject(response);
                if (currencyObject.getString("status").equalsIgnoreCase("1") && currencyObject.getString("message").equalsIgnoreCase("currency")) {

                    JSONObject dataObject = currencyObject.getJSONObject("data");

                    sessionManagement.setCurrency(dataObject.getString("currency_name"), dataObject.getString("currency_sign"));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fm = null;
        Bundle args = new Bundle();
        if (id == R.id.nav_order) {
            Home home = new Home();
//            TodayOrder tm = new TodayOrder();
            FragmentManager manager21 = getSupportFragmentManager();
            FragmentTransaction transaction21 = manager21.beginTransaction();
            transaction21.replace(R.id.contentPanel, home).addToBackStack(null);
            transaction21.commit();

        } else if (id == R.id.nav_Nextorder) {
            NextOrder category_fragment = new NextOrder();
            FragmentManager manager2 = getSupportFragmentManager();
            FragmentTransaction transaction2 = manager2.beginTransaction();
            transaction2.replace(R.id.contentPanel, category_fragment).addToBackStack(null);
            transaction2.commit();

        } else if (id == R.id.nav_order_history) {
            Order_history fma = new Order_history();
//            FragmentManager fragmentManager = getFragmentManager();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.contentPanel, fma, "Order_History")
//                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                    .addToBackStack(null)
//                    .commit();
            FragmentManager manager2 = getSupportFragmentManager();
            FragmentTransaction transaction2 = manager2.beginTransaction();
            transaction2.replace(R.id.contentPanel, fma).addToBackStack(null);
            transaction2.commit();

        } else if (id == R.id.nav_log_out) {
            sessionManagement.logoutSession();
            finish();
        }
        if (fm != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contentPanel, fm).addToBackStack(null).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {

        if (!isConnected) {
            Intent intent = new Intent(MainActivity.this, NoInternetConnection.class);
            startActivity(intent);
        }
    }


    void getLocation() {
        try {
//            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5,  this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }






/*
    private  void setapiloction(final String lat, final String lng){

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("lat",lat);
//
//
//            jsonObject.put("lng", lng);
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        Map<String, String> params = new HashMap<>();
        params.put("lat", lat);
        params.put("user_id",userid);
        params.put("lng",lng);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.Loction, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("dsd",response.toString());

                Log.d("latas", String.valueOf(lat+lng));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("dfs","asdfasdf");
            }
        });


        queue.add(jsonObjReq);

    }
*/

//    @Override
//    public void onLocationChanged(Location location) {
//
//        if (location!=null){
//            Log.d("sd",location.toString());
//            //        locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
//
////        Toast.makeText(this, ""+location.getLatitude()+location.getLongitude(), Toast.LENGTH_SHORT).show();
//
////            lat= location.getLatitude();
////
////            lng= location.getLongitude();
//
//
//
//
////            try {
////                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
////                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//
////                Log.i("asd", String.valueOf(lat+lng));
//
//                //  setapiloction(String.valueOf(lat),String.valueOf(lng));
//
////            locationText.setText(locationText.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+
////                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
////            }catch(Exception e)
////            {
////
////            }
//        }
//
//
//    }
//
//    //    @Override
////    public void onStatusChanged(String s, int i, Bundle bundle) {
////
////    }
////
////    @Override
////    public void onProviderEnabled(String s) {
////
////    }
////
////    @Override
////    public void onProviderDisabled(String s) {
////
////    }
////
//    @Override
//    public void onProviderDisabled(String provider) {
//        Toast.makeText(MainActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }


/*
    private void initComponent() {
        navigation = (BottomNavigationView) findViewById(R.id.nav_view12);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_today:

                        TodayOrder appNewsHome1Fragment = new TodayOrder();
                        androidx.fragment.app.FragmentManager manager = getSupportFragmentManager();
                        androidx.fragment.app.FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.contentPanel, appNewsHome1Fragment);
                        transaction.commit();
                        return true;
                    case R.id.navigation_next:

                        NextOrder category_fragment = new NextOrder();
                        androidx.fragment.app.FragmentManager manager2 = getSupportFragmentManager();
                        androidx.fragment.app.FragmentTransaction transaction2 = manager2.beginTransaction();
                        transaction2.replace(R.id.contentPanel, category_fragment);
                        transaction2.commit();

                      */
/*  CategoryFragment category_fragment = new CategoryFragment();
                        FragmentManager manager2 = getSupportFragmentManager();
                        FragmentTransaction transaction2 = manager2.beginTransaction();
                        transaction2.replace(R.id.contentPanel, category_fragment);
                        transaction2.commit();*//*

                        return true;
                }
                return false;
            }
        });
    }
*/

}
