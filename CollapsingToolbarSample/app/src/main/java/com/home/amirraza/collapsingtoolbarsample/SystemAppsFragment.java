package com.home.amirraza.collapsingtoolbarsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

/**
 * Created by AmirRaza on 7/6/2015.
 */
public class SystemAppsFragment extends Fragment {

    public ListView listView;
    ProgressBar progressBar;
    public static SystemAppsFragment systemAppsFragment;

    public static SystemAppsFragment getInstance(){
        if(systemAppsFragment == null)
            systemAppsFragment = new SystemAppsFragment();
        return systemAppsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_system_apps, container, false);
        progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        listView = (ListView) v.findViewById(R.id.myListView);
        new MyBackgroundTask(getActivity(),progressBar,listView,3).execute();
        return v;
    }
}
