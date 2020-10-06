package com.codeeratech.freshziiedelivery.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.codeeratech.freshziiedelivery.Model.Daily_model;
import com.codeeratech.freshziiedelivery.R;
import com.codeeratech.freshziiedelivery.util.Session_management;

import java.util.List;

public class Delivered_historyAdapter extends RecyclerView.Adapter<Delivered_historyAdapter.MyViewHolder> {

    private List<Daily_model> modelList;
    private LayoutInflater inflater;
    private Fragment currentFragment;

    private Context context;
    private Session_management session_management;

    public Delivered_historyAdapter(Context context, List<Daily_model> modemodelList, final Fragment currentFragment) {

        this.context = context;
        this.modelList = modemodelList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.currentFragment = currentFragment;

    }


    public Delivered_historyAdapter(List<Daily_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public Delivered_historyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_delivered_history, parent, false);
        context = parent.getContext();
        session_management = new Session_management(context);
        return new Delivered_historyAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(Delivered_historyAdapter.MyViewHolder holder, int position) {
        final Daily_model mList = modelList.get(position);
        try {
            holder.tv_orderno.setText(mList.getSale_id());
        } catch (Exception e) {
        }
        holder.tv_date.setText(mList.getOn_date());
        holder.tv_tracking_date.setText(mList.getOn_date());
        holder.tv_time.setText(mList.getDelivery_time_from());
        holder.tv_price.setText(session_management.getCurrency() + mList.getTotal_amount());
        holder.tv_item.setText(context.getResources().getString(R.string.tv_cart_item) + mList.getTotal_items());
        holder.tv_socity.setText(mList.getSocityname());
        holder.tv_house.setText(mList.getHouse());
        holder.tv_recivername.setText(modelList.get(position).getRecivername());
        holder.tv_storename.setText(mList.getStore_name());

    }

    @Override
    public int getItemCount() {
        return modelList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_orderno, tv_status, tv_date, tv_time, tv_price, tv_item, relativetextstatus, tv_tracking_date, tv_socity,
                tv_recivername, tv_house, tv_storename;

        ImageView call;
        CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            tv_storename = view.findViewById(R.id.store_name);

            tv_orderno = (TextView) view.findViewById(R.id.tv_order_no);
            tv_status = (TextView) view.findViewById(R.id.tv_order_status);
            relativetextstatus = (TextView) view.findViewById(R.id.status);
            tv_tracking_date = (TextView) view.findViewById(R.id.tracking_date);
            tv_date = (TextView) view.findViewById(R.id.tv_order_date);
            tv_time = (TextView) view.findViewById(R.id.tv_order_time);
            tv_price = (TextView) view.findViewById(R.id.tv_order_price);
            tv_item = (TextView) view.findViewById(R.id.tv_order_item);
            tv_socity = view.findViewById(R.id.tv_societyname);
            tv_house = view.findViewById(R.id.tv_house);
            tv_recivername = view.findViewById(R.id.tv_recivername);
            call = view.findViewById(R.id.call);
            cardView = view.findViewById(R.id.card_view);
        }
    }


}
