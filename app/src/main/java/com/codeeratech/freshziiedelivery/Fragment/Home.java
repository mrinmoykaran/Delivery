package com.codeeratech.freshziiedelivery.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.codeeratech.freshziiedelivery.Activity.OrderDetail;
import com.codeeratech.freshziiedelivery.Adapter.PagerAdapter;
import com.codeeratech.freshziiedelivery.AppController;
import com.codeeratech.freshziiedelivery.Config.BaseURL;
import com.codeeratech.freshziiedelivery.Model.ListAssignAndUnassigned;
import com.codeeratech.freshziiedelivery.Model.My_order_model;
import com.codeeratech.freshziiedelivery.R;
import com.codeeratech.freshziiedelivery.util.CustomVolleyJsonArrayRequest;
import com.codeeratech.freshziiedelivery.util.CustomVolleyJsonRequest;
import com.codeeratech.freshziiedelivery.util.Session_management;
import com.codeeratech.freshziiedelivery.util.TodayOrderClickListner;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeeratech.freshziiedelivery.Config.BaseURL.dOutforDelivery;
import static com.codeeratech.freshziiedelivery.Config.BaseURL.nextOrder;
import static com.codeeratech.freshziiedelivery.Config.BaseURL.todayOrder;

public class Home extends Fragment {

    private static String TAG = Home.class.getSimpleName();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String get_id, delivery_boy_id;
    SwipeRefreshLayout refresh_layout;
    Handler handler = new Handler();
    Runnable timeCounter;
    //    private RecyclerView rv_myorder;
    private List<My_order_model> my_order_modelList = new ArrayList<>();
    private List<My_order_model> my_next_modelList = new ArrayList<>();
    private Session_management sessionManagement;
    private ProgressDialog progressDialog;
    private ViewPager2 pager;
    private TabLayout tabLayout;
    private PagerAdapter adapter;
    private List<ListAssignAndUnassigned> listAssignAndUnassigneds = new ArrayList<>();

    public Home() {

    }


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,
                container, false);
        sessionManagement = new Session_management(requireActivity());
        progressDialog = new ProgressDialog(requireActivity());
        get_id = sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
        delivery_boy_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
//        rv_myorder = view.findViewById(R.id.rv_myorder);
//        rv_myorder.setLayoutManager(new LinearLayoutManager(getActivity()));

        pager = view.findViewById(R.id.pager);
        tabLayout = view.findViewById(R.id.tab_layout);
        sharedPreferences = requireActivity().getSharedPreferences("check", 0);
        editor = sharedPreferences.edit();
        refresh_layout = view.findViewById(R.id.refresh_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Today's Order"));
        tabLayout.addTab(tabLayout.newTab().setText("Nextday's Order"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        listAssignAndUnassigneds.add(new ListAssignAndUnassigned("assigned", my_order_modelList, my_next_modelList));
        listAssignAndUnassigneds.add(new ListAssignAndUnassigned("unassigned", my_order_modelList, my_next_modelList));



        adapter = new PagerAdapter(requireActivity(), listAssignAndUnassigneds, new TodayOrderClickListner() {

            @Override
            public void callAction(String number) {
                if (isPermissionGranted()) {
                    call_action(number);
                }
            }

            @Override
            public void orderDetailsClick(String viewType, int position) {
                if (viewType.equalsIgnoreCase("assigned")) {
                    String saleid = my_order_modelList.get(position).getSale_id();
                    String placedon = my_order_modelList.get(position).getOn_date();
                    String time = my_order_modelList.get(position).getDelivery_time_from();
                    String item = my_order_modelList.get(position).getTotal_items();
                    String ammount = my_order_modelList.get(position).getTotal_amount();
                    String ssstatus = my_order_modelList.get(position).getStatus();
                    String society = my_order_modelList.get(position).getSocityname();
                    String house = my_order_modelList.get(position).getHouse();
                    String recivername = my_order_modelList.get(position).getRecivername();
                    String recivermobile = my_order_modelList.get(position).getRecivermobile();
                    String stotreAddr = my_order_modelList.get(position).getStore_name();
                    Intent intent = new Intent(requireActivity(), OrderDetail.class);
                    intent.putExtra("sale_id", saleid);
                    intent.putExtra("placedon", placedon);
                    intent.putExtra("time", time);
                    intent.putExtra("item", item);
                    intent.putExtra("ammount", ammount);
                    intent.putExtra("status", ssstatus);
                    intent.putExtra("socity_name", society);
                    intent.putExtra("house_no", house);
                    intent.putExtra("receiver_name", recivername);
                    intent.putExtra("receiver_mobile", recivermobile);
                    intent.putExtra("storeAddr", stotreAddr);
                    startActivityForResult(intent, 7);
                } else if (viewType.equalsIgnoreCase("unassigned")) {
                    String saleid = my_next_modelList.get(position).getSale_id();
                    String placedon = my_next_modelList.get(position).getOn_date();
                    String time = my_next_modelList.get(position).getDelivery_time_from();
                    String item = my_next_modelList.get(position).getTotal_items();
                    String ammount = my_next_modelList.get(position).getTotal_amount();
                    String ssstatus = my_next_modelList.get(position).getStatus();
                    String society = my_next_modelList.get(position).getSocityname();
                    String house = my_next_modelList.get(position).getHouse();
                    String recivername = my_next_modelList.get(position).getRecivername();
                    String recivermobile = my_next_modelList.get(position).getRecivermobile();
                    String stotreAddr = my_next_modelList.get(position).getStore_name();
                    Intent intent = new Intent(requireActivity(), OrderDetail.class);
                    intent.putExtra("sale_id", saleid);
                    intent.putExtra("placedon", placedon);
                    intent.putExtra("time", time);
                    intent.putExtra("item", item);
                    intent.putExtra("ammount", ammount);
                    intent.putExtra("status", ssstatus);
                    intent.putExtra("socity_name", society);
                    intent.putExtra("house_no", house);
                    intent.putExtra("receiver_name", recivername);
                    intent.putExtra("receiver_mobile", recivermobile);
                    intent.putExtra("storeAddr", stotreAddr);
                    startActivityForResult(intent, 7);
                }

            }

            @Override
            public void orderConfirm(String viewType, int position) {
                if (viewType.equalsIgnoreCase("assigned")) {
                    order_OutDelivery(my_order_modelList.get(position).getSale_id());
                } else if (viewType.equalsIgnoreCase("unassigned")) {
                    order_OutDelivery(my_next_modelList.get(position).getSale_id());
                }
            }
        });
        pager.setAdapter(adapter);
        wrapTabIndicatorToTitle(tabLayout, 80, 80);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, pager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Today's Order");
            } else if (position == 1) {
                tab.setText("Nextday's Order");
            }

        });
        tabLayoutMediator.attach();

        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
//                super.onPageSelected(position);
                pager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        //            if (handler != null && timeCounter != null) {
        //                handler.removeCallbacks(timeCounter);
        //            }
        refresh_layout.setOnRefreshListener(this::makeGetOrderRequest);

        makeGetOrderRequest();


        return view;
    }

    public void wrapTabIndicatorToTitle(TabLayout tabLayout, int externalMargin, int internalMargin) {
        View tabStrip = tabLayout.getChildAt(0);
        if (tabStrip instanceof ViewGroup) {
            ViewGroup tabStripGroup = (ViewGroup) tabStrip;
            int childCount = ((ViewGroup) tabStrip).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View tabView = tabStripGroup.getChildAt(i);
                tabView.setMinimumWidth(0);
                tabView.setPadding(0, tabView.getPaddingTop(), 0, tabView.getPaddingBottom());
                if (tabView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tabView.getLayoutParams();
                    if (i == 0) {
                        setMargin(layoutParams, externalMargin, internalMargin);
                    } else if (i == childCount - 1) {
                        setMargin(layoutParams, internalMargin, externalMargin);
                    } else {
                        setMargin(layoutParams, internalMargin, internalMargin);
                    }
                }
            }

            tabLayout.requestLayout();
        }
    }

    private void setMargin(ViewGroup.MarginLayoutParams layoutParams, int start, int end) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.setMarginStart(start);
            layoutParams.setMarginEnd(end);
        } else {
            layoutParams.leftMargin = start;
            layoutParams.rightMargin = end;
        }
    }


    private void makeGetOrderRequest() {
        progressDialog.setMessage("Please wait while we fetching your today's orders..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        my_order_modelList.clear();
        String tag_json_obj = "json_socity_req";
        HashMap<String, String> param = new HashMap<>();
        param.put("dboy_id", delivery_boy_id);
        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST, todayOrder, param, response -> {
            Log.d("rdtfyghj", response.toString());
            try {
                if (response.toString().contains("no orders found")){
                    Toast.makeText(requireActivity(), "No Today's Order Found!", Toast.LENGTH_SHORT).show();
                }else {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);
                        String saleid = obj.getString("cart_id");
                        String placedon = obj.getString("delivery_date");
                        String timefrom = obj.getString("time_slot");
                        //  String timeto=obj.getString("delivery_time_from");
                        //String item = obj.getString("total_items");
                        String ammount = obj.getString("remaining_price");
                        String status = obj.getString("order_status");
                        String user_address = obj.getString("user_address");
                        String store_Address = obj.getString("store_address");
                        //  String house = obj.getString("house_no");
                        String rename = obj.getString("user_name");
                        String renumber = obj.getString("user_phone");
                        String store_name = obj.getString("store_name");
                        String total_items = obj.getString("total_items");
                        String store_lat = obj.getString("store_lat");
                        String store_lng = obj.getString("store_lng");
                        String user_lat = obj.getString("user_lat");
                        String user_lng = obj.getString("user_lng");
                        String dboy_lat = obj.getString("dboy_lat");
                        String dboy_lng = obj.getString("dboy_lng");
                        My_order_model my_order_model = new My_order_model();
                        // my_order_model.setSocityname(society);
                        my_order_model.setHouse(user_address);
                        my_order_model.setRecivername(rename);
                        my_order_model.setRecivermobile(renumber);
                        my_order_model.setDelivery_time_from(timefrom);
                        my_order_model.setSale_id(saleid);
                        my_order_model.setOn_date(placedon);
                        my_order_model.setTotal_amount(ammount);
                        my_order_model.setStatus(status);
                        my_order_model.setStore_name(store_name);
                        my_order_model.setTotal_items(total_items);
                        my_order_model.setLat(store_lat);
                        my_order_model.setLng(store_lng);
                        my_order_model.setUserLat(user_lat);
                        my_order_model.setUserLong(user_lng);
                        my_order_model.setDbLat(dboy_lat);
                        my_order_model.setDbuserLong(dboy_lng);
                        //   my_order_model.setTotal_items(item);
                        // my_order_model.setDelivery_time_to(timefrom);
                        my_order_modelList.add(my_order_model);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                makeGetNextOrderRequest();
            }
        }, error -> makeGetNextOrderRequest());
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.getCache().clear();
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
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
        requestQueue.add(jsonObjReq);

    }


    private void makeGetNextOrderRequest() {
        my_next_modelList.clear();
        String tag_json_obj = "json_socity_req";
        HashMap<String, String> param = new HashMap<>();
        param.put("dboy_id", delivery_boy_id);
        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST, nextOrder, param, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("rdtfyghj", response.toString());
                try {

                    if (response.toString().contains("no orders found")){
                        if (adapter!=null && my_order_modelList.size()>0){
                            adapter.notifyDataSetChanged();
                        }
                        refresh_layout.setRefreshing(false);
                        Toast.makeText(requireActivity(), "No Nextday's Order Found!", Toast.LENGTH_SHORT).show();
                    }else {
                        for (int i = 0; i < response.length(); i++) {

                            JSONObject obj = response.getJSONObject(i);
                            String saleid = obj.getString("cart_id");
                            String placedon = obj.getString("delivery_date");
                            String timefrom = obj.getString("time_slot");
                            //  String timeto=obj.getString("delivery_time_from");
                            //String item = obj.getString("total_items");
                            String ammount = obj.getString("remaining_price");
                            String status = obj.getString("order_status");

                            String user_address = obj.getString("user_address");
                            String store_Address = obj.getString("store_address");
                            //  String house = obj.getString("house_no");
                            String rename = obj.getString("user_name");
                            String renumber = obj.getString("user_phone");
                            String store_name = obj.getString("store_name");
                            String total_items = obj.getString("total_items");

                            String store_lat = obj.getString("store_lat");
                            String store_lng = obj.getString("store_lng");
                            String user_lat = obj.getString("user_lat");
                            String user_lng = obj.getString("user_lng");
                            String dboy_lat = obj.getString("dboy_lat");
                            String dboy_lng = obj.getString("dboy_lng");

                            My_order_model my_order_model = new My_order_model();
                            // my_order_model.setSocityname(society);
                            my_order_model.setHouse(user_address);
                            my_order_model.setTotal_items(total_items);
                            my_order_model.setRecivername(rename);
                            my_order_model.setRecivermobile(renumber);
                            my_order_model.setDelivery_time_from(timefrom);
                            my_order_model.setSale_id(saleid);
                            my_order_model.setOn_date(placedon);
                            my_order_model.setTotal_amount(ammount);
                            my_order_model.setStatus(status);
                            my_order_model.setStore_name(store_name);

                            my_order_model.setLat(store_lat);
                            my_order_model.setLng(store_lng);

                            my_order_model.setUserLat(user_lat);
                            my_order_model.setUserLong(user_lng);

                            my_order_model.setDbLat(dboy_lat);
                            my_order_model.setDbuserLong(dboy_lng);


                            //   my_order_model.setTotal_items(item);
                            // my_order_model.setDelivery_time_to(timefrom);

                            my_next_modelList.add(my_order_model);
                        }
                        if (adapter!=null){
                            adapter.notifyDataSetChanged();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    progressDialog.dismiss();
                    refresh_layout.setRefreshing(false);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if (adapter != null && my_order_modelList.size()>0) {
                    adapter.notifyDataSetChanged();
                }
                refresh_layout.setRefreshing(false);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity());
        requestQueue.getCache().clear();
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
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
        requestQueue.add(jsonObjReq);

    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }

    public void call_action(String number) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        startActivity(callIntent);
    }

    private void order_OutDelivery(final String saleid) {
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("cart_id", saleid);
        Log.d("fgh", saleid);

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, dOutforDelivery, params, response -> {
            Log.d("Tag", response.toString());

            try {
                String status = response.getString("status");
                String msg = response.getString("message");
                if (status.equals("1")) {
                    progressDialog.dismiss();
                    makeGetOrderRequest();

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

            }

        }, error -> {
            progressDialog.dismiss();
//                Toast.makeText(getApplicationContext(), ""+error, Toast.LENGTH_SHORT).show();
        });

        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
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
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7) {
            makeGetOrderRequest();
        }
    }
}