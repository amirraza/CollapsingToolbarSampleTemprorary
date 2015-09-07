package com.world.extremeapkeditor;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.ProgressBar;


import com.world.extremeapkeditor.Adapters.GeneralAdapter;

import java.util.List;

/**
 * Created by Amir on 7/8/2015.
 */
public class MyBackgroundTask extends AsyncTask<String, String, String> {
    private PackageManager packageManager = null;
    private List<ApplicationInfo> applist = null;
    private List<PackageInfo> temList = null;
    private ApplicationInfo[] mAppInfo, tempAppInfo;
    private Context context;
    private ProgressBar progressBar;
    private ListView listView;
    private int fragmentNumber = 0;

    MyBackgroundTask(Context c, ProgressBar progressBar, ListView listView, int i) {
        this.context = c;
        this.progressBar = progressBar;
        this.listView = listView;
        this.fragmentNumber = i;
    }

    @Override
    public String doInBackground(String... params) {
        packageManager = context.getPackageManager();
        applist = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        mAppInfo = applist.toArray(new ApplicationInfo[applist.size()]);

        if (fragmentNumber == 0) {
            tempAppInfo = applist.toArray(new ApplicationInfo[applist.size()]);
        } else if (fragmentNumber == 1) {
            int count = 0;
            int i = 0;
            for (int a = 0; a < mAppInfo.length; a++) {
                if (mAppInfo[a].publicSourceDir.contains("/system/")) {

                } else {
                    count++;
                }
            }
            tempAppInfo = new ApplicationInfo[count];
            Log.d("TAG", "2 >" + count);
            for (int a = 0; a < mAppInfo.length; a++) {
                if (mAppInfo[a].publicSourceDir.contains("/system/")) {

                } else {
                    tempAppInfo[i] = mAppInfo[a];
                    i++;
                }
            }
        } else if (fragmentNumber == 2) {
            int count = 0;
            int i = 0;
            for (int a = 0; a < mAppInfo.length; a++) {
                if (mAppInfo[a].publicSourceDir.contains("/system/")) {
                    count++;
                }
            }
            tempAppInfo = new ApplicationInfo[count];
            Log.d("TAG", "3 >" + count);
            for (int a = 0; a < mAppInfo.length; a++) {
                if (mAppInfo[a].publicSourceDir.contains("/system/")) {
                    tempAppInfo[i] = mAppInfo[a];
                    i++;
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
//        ObjectAnimator animator = new ObjectAnimator();
//        animator.
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.out);
        animation.setFillEnabled(true);
        animation.setFillAfter(true);
//        animation.setDuration(5000);
        if (!MainActivity.done) {
            MainActivity.coverView.startAnimation(animation);
            MainActivity.done = true;

        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                MainActivity.coverView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
//        MainActivity.coverView.setVisibility(View.GONE);

        progressBar.setVisibility(View.INVISIBLE);

//        GeneralAdapter generalAdapter = new GeneralAdapter(context, tempAppInfo);
        if (fragmentNumber == 0)
            listView.setAdapter(GeneralAdapter.getAdapter1(context, tempAppInfo));
        else if (fragmentNumber == 2)
            listView.setAdapter(GeneralAdapter.getAdapter2(context, tempAppInfo));
        else
            listView.setAdapter(GeneralAdapter.getAdapter3(context, tempAppInfo));


    }

}