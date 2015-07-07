package com.home.amirraza.collapsingtoolbarsample;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by AmirRaza on 7/5/2015.
 */
public class AllAppsFragment extends Fragment {
    private PackageManager packageManager = null;
    private List<ApplicationInfo> applist = null;
    private ApplicationInfo[] mAppInfo;
    private String[] names;
    Drawable[] icons;
    public MyAllAppsAdapter adapter;
    public ListView listView;
    ProgressBar progressBar;
    private String[] packageName;
    float[] appSize;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new MyTask().execute();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_apps, container, false);
        progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        listView = (ListView) v.findViewById(R.id.myListView);
        return v;

    }

    class MyAllAppsAdapter extends ArrayAdapter<String> {

        String[] name;
        Drawable[] icons;
        String[] packageName;
        float[] appSize;
        Context context;

        public MyAllAppsAdapter(Context context, Drawable[] icons, String[] name, String[] packageName, float[] appSize) {
            super(context, android.R.layout.simple_list_item_1, name);
            this.context = context;
            this.name = name;
            this.icons = icons;
            this.packageName = packageName;
            this.appSize = appSize;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.single_item, null, false);
            }

            TextView appName = (TextView) convertView.findViewById(R.id.appName);
            appName.setText(name[position]);

            TextView packageName = (TextView) convertView.findViewById(R.id.packageName);
            packageName.setText(this.packageName[position]);

            TextView appSize = (TextView) convertView.findViewById(R.id.appSize);
            appSize.setText(this.appSize[position] / (1024) + "Mb");

            ImageView appImage = (ImageView) convertView.findViewById(R.id.appImage);
            appImage.setImageDrawable(icons[position]);

            return convertView;
        }
    }

    class MyTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            packageManager = getActivity().getPackageManager();
            applist = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
            mAppInfo = applist.toArray(new ApplicationInfo[applist.size()]);
            int i = 0;
            names = new String[mAppInfo.length];
            icons = new Drawable[mAppInfo.length];
            packageName = new String[mAppInfo.length];
            appSize = new float[mAppInfo.length];


            for (ApplicationInfo s : mAppInfo) {

                try {
                    names[i] = (String) s.loadLabel(getActivity().getPackageManager());
                    icons[i] = s.loadIcon(getActivity().getPackageManager());
                    packageName[i] = s.packageName;
                    long fileSize = new FileInputStream(mAppInfo[i].sourceDir).getChannel().size();
                    appSize[i] = fileSize;

                } catch (IOException e) {
                    e.printStackTrace();
                }
                i++;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.INVISIBLE);
            adapter = new MyAllAppsAdapter(getActivity(), icons, names, packageName, appSize);

            listView.setAdapter(adapter);
        }

    }
}
