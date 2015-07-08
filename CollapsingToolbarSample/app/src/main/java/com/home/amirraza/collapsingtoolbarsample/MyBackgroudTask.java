package com.home.amirraza.collapsingtoolbarsample;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Amir on 7/8/2015.
 */
public class MyBackgroudTask extends AsyncTask<String, String, String> {
    private PackageManager packageManager = null;
    private List<ApplicationInfo> applist = null;
    private ApplicationInfo[] mAppInfo;
    private String[] names;
    Drawable[] icons;
    private String[] packageName;
    float[] appSize;
    Context context;
    ProgressBar progressBar;
    ListView listView;
    MyBackgroudTask(Context c, ProgressBar progressBar, ListView listView){
        this.context=c;
        this.progressBar=progressBar;
        this.listView =listView;
    }
    @Override
    public String doInBackground(String... params) {
        packageManager = context.getPackageManager();
        applist = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        mAppInfo = applist.toArray(new ApplicationInfo[applist.size()]);
        int count = 0;
        int i = 0;
        for (ApplicationInfo s : mAppInfo) {
            if (s.publicSourceDir.contains("/system/")) {

            } else {
                count++;
            }
        }

        names = new String[count];
        icons = new Drawable[count];
        packageName = new String[count];
        appSize = new float[count];

        for (ApplicationInfo s : mAppInfo) {

            if (s.publicSourceDir.contains("/system/")) {

            } else {
                try {
                    names[i] = (String) s.loadLabel(context.getPackageManager());
                    icons[i] = s.loadIcon(context.getPackageManager());
                    packageName[i] = s.packageName;
                    long fileSize = new FileInputStream(mAppInfo[i].sourceDir).getChannel().size();
                    appSize[i] = fileSize;

                } catch (IOException e) {
                    e.printStackTrace();
                }
                i++;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressBar.setVisibility(View.INVISIBLE);
        GeneralAdapter generalAdapter = new GeneralAdapter(context,mAppInfo);
        listView.setAdapter(generalAdapter);
    }

}