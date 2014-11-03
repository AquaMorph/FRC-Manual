package com.aquamorph.frcmanual;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class Tabs extends FragmentPagerAdapter {

    public Tabs(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        //starts fragments
        return Page.newInstance(index);
    }

    @Override
    public int getCount() {
        //number of tabs
        return 6;
    }

}