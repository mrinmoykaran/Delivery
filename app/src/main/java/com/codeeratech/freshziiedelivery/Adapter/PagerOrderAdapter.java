package com.codeeratech.freshziiedelivery.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.codeeratech.freshziiedelivery.Fragment.My_Past_Order;
import com.codeeratech.freshziiedelivery.Fragment.My_Pending_Order;


public class PagerOrderAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerOrderAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                My_Pending_Order tab1 = new My_Pending_Order();
                return tab1;
            case 1:
                My_Past_Order tab2 = new My_Past_Order();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}