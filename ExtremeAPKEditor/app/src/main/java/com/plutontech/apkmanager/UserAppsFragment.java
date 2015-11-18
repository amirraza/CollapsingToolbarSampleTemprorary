package com.plutontech.apkmanager;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;


/**
 * Created by AmirRaza on 7/6/2015.
 */
public class UserAppsFragment extends Fragment {

    public ListView listView;
    ProgressBar progressBar;
    public static UserAppsFragment userAppsFragment;

    public static UserAppsFragment getInstance(){
        if(userAppsFragment == null)
            userAppsFragment = new UserAppsFragment();
        return userAppsFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_apps, container, false);
        progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        listView = (ListView) v.findViewById(R.id.myListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent fileIntent = new Intent(getActivity(),FileActivity.class);
                fileIntent.putExtra("INFO",((ApplicationInfo)listView.getItemAtPosition(i)).sourceDir);
                startActivity(fileIntent);
            }
        });
        new MyBackgroundTask(getActivity(),progressBar, listView,1).execute();
        return v;

    }
}
