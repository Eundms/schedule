package com.example.user.scheduleitssu;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabPagerAdapter extends FragmentPagerAdapter {
    int tabcount;
    public TabPagerAdapter(@NonNull FragmentManager fm, int tabcount) {
        super(fm, tabcount);
        this.tabcount=tabcount;

    }

    @NonNull
    @Override
    //각 탭에 해당되는 fragment 설정
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                TabFragment1 tabFragment1=new TabFragment1();
                return tabFragment1;
            case 1:
                TabFragment2 tabFragment2=new TabFragment2();
                return tabFragment2;

            default: return null;
        }

    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
