package com.codeeratech.freshziiedelivery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codeeratech.freshziiedelivery.Model.ListAssignAndUnassigned;
import com.codeeratech.freshziiedelivery.R;
import com.codeeratech.freshziiedelivery.util.TodayOrderClickListner;

import java.util.List;

public class PagerAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<ListAssignAndUnassigned> listAssignAndUnassigneds;
    private TodayOrderClickListner todayOrderClickListner;

    public PagerAdapter(Context context, List<ListAssignAndUnassigned> listAssignAndUnassigneds, TodayOrderClickListner todayOrderClickListner) {
        this.context = context;
        this.listAssignAndUnassigneds = listAssignAndUnassigneds;
        this.todayOrderClickListner = todayOrderClickListner;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assign_view, parent, false);
            return new MyAssignView(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.unassign_view, parent, false);
            return new MyUnAssignView(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ListAssignAndUnassigned assignAndUnassigned = listAssignAndUnassigneds.get(position);
        switch (assignAndUnassigned.getViewType()) {
            case "assigned":
                MyAssignView myAssignView = (MyAssignView) holder;
                myAssignView.assign_recy.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                myAssignView.assign_recy.setItemAnimator(new DefaultItemAnimator());
                myAssignView.assign_recy.setAdapter(new My_Today_Order_Adapter(context,assignAndUnassigned.getTodayOrderModels(),"assigned",todayOrderClickListner));
                break;
            case "unassigned":
                MyUnAssignView myUnAssignView = (MyUnAssignView) holder;
                myUnAssignView.unAssign_recy.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                myUnAssignView.unAssign_recy.setItemAnimator(new DefaultItemAnimator());
                myUnAssignView.unAssign_recy.setAdapter(new My_Today_Order_Adapter(context,assignAndUnassigned.getNextDayOrders(),"unassigned",todayOrderClickListner));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (listAssignAndUnassigneds.get(position).getViewType()) {
            case "assigned":
                return 0;
            case "unassigned":
                return 1;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return listAssignAndUnassigneds.size();
    }

    public class MyAssignView extends RecyclerView.ViewHolder {
        private RecyclerView assign_recy;

        public MyAssignView(@NonNull View itemView) {
            super(itemView);
            assign_recy = itemView.findViewById(R.id.assign_recy);
        }
    }

    public class MyUnAssignView extends RecyclerView.ViewHolder {
        private RecyclerView unAssign_recy;

        public MyUnAssignView(@NonNull View itemView) {
            super(itemView);
            unAssign_recy = itemView.findViewById(R.id.unassign_recy);
        }
    }

}
