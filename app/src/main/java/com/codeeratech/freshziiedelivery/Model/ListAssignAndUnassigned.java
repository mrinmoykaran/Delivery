package com.codeeratech.freshziiedelivery.Model;

import java.util.List;

public class ListAssignAndUnassigned {
    private String viewType;
    private List<My_order_model> todayOrderModels;
    private List<My_order_model> nextDayOrders;

    public ListAssignAndUnassigned(String viewType, List<My_order_model> todayOrderModels, List<My_order_model> nextDayOrders) {
        this.viewType = viewType;
        this.todayOrderModels = todayOrderModels;
        this.nextDayOrders = nextDayOrders;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public List<My_order_model> getTodayOrderModels() {
        return todayOrderModels;
    }

    public void setTodayOrderModels(List<My_order_model> todayOrderModels) {
        this.todayOrderModels = todayOrderModels;
    }

    public List<My_order_model> getNextDayOrders() {
        return nextDayOrders;
    }

    public void setNextDayOrders(List<My_order_model> nextDayOrders) {
        this.nextDayOrders = nextDayOrders;
    }
}
