package com.rubatto.staralba;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2017-09-21.
 */

public class TabPagerAdapter extends FragmentStatePagerAdapter {
    private int mTabCount;

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        mTabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TabFragment0 tabFragment0 = new TabFragment0();
                return tabFragment0;
            case 1:
                TabFragment1 tabFragment1 = new TabFragment1();
                return tabFragment1;
            case 2:
                TabFragment2 tabFragment2 = new TabFragment2();
                return tabFragment2;
            case 3:
                TabFragment3 tabFragment3 = new TabFragment3();
                return tabFragment3;
            case 4:
                TabFragment4 tabFragment4 = new TabFragment4();
                return tabFragment4;
            case 5:
                TabFragment5 tabFragment5 = new TabFragment5();
                return tabFragment5;
            case 6:
                TabFragment6 tabFragment6 = new TabFragment6();
                return tabFragment6;
            case 7:
                TabFragment7 tabFragment7 = new TabFragment7();
                return tabFragment7;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mTabCount;
    }
}
