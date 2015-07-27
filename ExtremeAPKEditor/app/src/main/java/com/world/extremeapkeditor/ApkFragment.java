package com.world.extremeapkeditor;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        new ApkTask().execute();
        return view;
    }
    public class ApkTask extends AsyncTask<String,String,String>{

        private File[] allDirectoriesPhone;
        private ArrayList<File> allFiles = new ArrayList<>();
        ;

        @Override
        protected String doInBackground(String... params) {
            List<File> allFiles = new ArrayList<>();
            File[] allDirectoriesPhone = getActivity().getFilesDir().listFiles();
            File[] allDirectoriesCard= Environment.getExternalStorageDirectory().listFiles();
//            File[] apkFilesOnPhone = getActivity().getFilesDir().listFiles(new FilenameFilter() {
//                @Override
//                public boolean accept(File dir, String filename) {
//                    return filename.toLowerCase().endsWith(".apk");
//                }
//            });
//            File[] apkFilesOnCard = Environment.getExternalStorageDirectory().listFiles(new FilenameFilter() {
//                @Override
//                public boolean accept(File dir, String filename) {
//                    return filename.toLowerCase().endsWith(".apk");
//                }
//            });
//            Log.d("TAG","in ASYNC");

            for(File file : allDirectoriesPhone){
                if (file.isDirectory()) {
                    for (File inner : file.listFiles()) {
                        allFiles.addAll(Arrays.asList(inner.listFiles(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String filename) {
                                return filename.toLowerCase().endsWith(".apk");
                            }
                        })));
                    }
                }else{
                    file.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            return filename.toLowerCase().endsWith(".apk");
                        }
                    });
                }
                Log.d("TAG","p>"+file.getAbsolutePath());
            }

//            PackageManager pm =     getActivity().getPackageManager();
//            PackageInfo packageInfo = pm.getPackageInfo()
//done
            return null;
        }

        }
    }

