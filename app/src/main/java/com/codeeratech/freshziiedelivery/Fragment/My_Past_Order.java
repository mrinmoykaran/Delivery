package com.codeeratech.freshziiedelivery.Fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.codeeratech.freshziiedelivery.Adapter.My_Past_Order_adapter;
import com.codeeratech.freshziiedelivery.AppController;
import com.codeeratech.freshziiedelivery.Config.BaseURL;
import com.codeeratech.freshziiedelivery.MainActivity;
import com.codeeratech.freshziiedelivery.Model.My_Past_order_model;
import com.codeeratech.freshziiedelivery.R;
import com.codeeratech.freshziiedelivery.util.ConnectivityReceiver;
import com.codeeratech.freshziiedelivery.util.CustomVolleyJsonArrayRequest;
import com.codeeratech.freshziiedelivery.util.Session_management;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class My_Past_Order extends Fragment {
    TabHost tHost;
    String get_id;
    private RecyclerView rv_myorder;
    private List<My_Past_order_model> my_order_modelList = new ArrayList<>();
    private Session_management sessionManagement;

    public My_Past_Order() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_past_order, container, false);

        sessionManagement = new Session_management(getActivity());
        get_id = sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // check user can press back button or not
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

//                    Fragment fm = new Home_fragment();
//                    androidx.fragment.app.FragmentManager fragmentManager = getFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                            .addToBackStack(null).commit();
                    return true;
                }
                return false;
            }
        });

        rv_myorder = (RecyclerView) view.findViewById(R.id.rv_myorder);
        rv_myorder.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (ConnectivityReceiver.isConnected()) {
            makeGetOrderRequest();
        } else {
            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }
        return view;
    }

    private void makeGetOrderRequest() {
        String tag_json_obj = "json_socity_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("dboy_id", get_id);

        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
                BaseURL.todayOrder, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<My_Past_order_model>>() {
                }.getType();
                my_order_modelList = gson.fromJson(response.toString(), listType);
                My_Past_Order_adapter adapter = new My_Past_Order_adapter(my_order_modelList);
                rv_myorder.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                if (my_order_modelList.isEmpty()) {
                    // Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }


}
