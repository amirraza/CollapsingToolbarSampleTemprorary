package com.world.extremeapkeditor.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.world.extremeapkeditor.AllAppsFragment;
import com.world.extremeapkeditor.ApkFragment;
import com.world.extremeapkeditor.SystemAppsFragment;
import com.world.extremeapkeditor.UserAppsFragment;


/**
 * Created by Amir on 7/8/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    CharSequence Titles[];
    int NumbOfTabs;
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0)
            return AllAppsFragment.getInstance();
        else if(position == 1)
            return UserAppsFragment.getInstance();
        else if(position == 2)
            return SystemAppsFragment.getInstance();
        else
            return ApkFragment.getInstance();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
