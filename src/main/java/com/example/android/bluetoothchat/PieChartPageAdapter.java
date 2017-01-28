package com.example.android.bluetoothchat;

/**
 * Created by aman on 4/1/17.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PieChartPageAdapter  extends FragmentStatePagerAdapter {
    public PieChartPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new PieChartObjectFragment();
        Bundle args = new Bundle();
        // Our object is just an integer :-P
        args.putInt(PieChartObjectFragment.ARG_OBJECT, i + 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 100;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
}
