package com.home.amirraza.collapsingtoolbarsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Amir on 7/8/2015.
 */
public class ApkFragment extends Fragment {
    public static ApkFragment apkFragment;

    public static ApkFragment getInstance(){
        if(apkFragment == null)
            apkFragment = new ApkFragment();
        return apkFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_apps,container,false);
        return view;
    }
}
