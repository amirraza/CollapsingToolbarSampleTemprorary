package com.plutontech.apkmanager.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.plutontech.apkmanager.FragmentCopyIntro;
import com.plutontech.apkmanager.FragmentExtractIntro;
import com.plutontech.apkmanager.FragmentIntro;
import com.plutontech.apkmanager.FragmentShareIntro;

/**
 * Created by abdulazizniazi on 8/21/15.
 */
public class IntroAdapter extends FragmentStatePagerAdapter {
    public IntroAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return new FragmentIntro();
        else if (position == 1)
            return new FragmentExtractIntro();
        else if (position == 2)
            return new FragmentCopyIntro();
        else
            return new FragmentShareIntro();
    }

    @Override
    public int getCount() {
        return 4;
    }
}
