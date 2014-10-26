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
        if(index==0)return new Page("summary");
        if(index==1)return new Page("arena");
        if(index==2)return new Page("game");
        if(index==3)return new Page("robot");
        if(index==4)return new Page("tournament");
        if(index==5)return new Page("glossary");
        return null;
    }

    @Override
    public int getCount() {
        //number of tabs
        return 6;
    }

}