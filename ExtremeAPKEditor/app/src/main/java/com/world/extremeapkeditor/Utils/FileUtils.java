package com.world.extremeapkeditor.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Formatter;

/**
 * Created by Abdul Aziz on 7/5/2015.
 */
public class FileUtils {
    Context mContext;
    View v;
    ExtractUtils extractUtils;
    public static FileUtils fileUtils;
    public String PATH;
    int i = 0;
    private ApplicationInfo[] array;
    private ProgressDialog dialog;
    private String mPath;

    public FileUtils(){}

    public FileUtils(Context context) {
        mContext = context;
        extractUtils = new ExtractUtils();
    }

    public static FileUtils getInstance(Context context){
        if(fileUtils == null)
            fileUtils = new FileUtils(context);
        return fileUtils;
    }

    public void share(File source ){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/zip");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(source));
        Intent.createChooser(intent, "Share");
        mContext.startActivity(intent);
    }

    public void getInfo(String packageName){
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
//            Uri packageName = Uri.parse(mAppInfo[i].packageName);
//            intent.setData(packageName);
//            intent.putExtra("package",mAppInfo[i].packageName);
//            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
            mContext.startActivity(intent);
        }
    }
    public void extract(int i, ApplicationInfo[] mAppInfo, View snackBarView) {
        v = snackBarView;
        array  = new ApplicationInfo[mAppInfo.length];
        array = mAppInfo;
        this.i = i;
        PATH = Environment.getExternalStorageDirectory().getPath() + "/Apk Extractor";
        mPath = PATH + "/APKs/" + array[i].loadLabel(mContext.getPackageManager())+"/"+array[i].loadLabel(mContext.getPackageManager());
        dialog = ProgressDialog.show(mContext,"Extracting","Extracting Files ...",true,true);
        new extractTask().execute((String[]) null);
//        dialog.dismiss();
//        Toast.makeText(mContext, " " + newLocation.getAbsolutePath(), Toast.LENGTH_SHORT).show();
    }


    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
    public class extractTask extends AsyncTask<String,String,String>{


        @Override
        protected String doInBackground(String... strings) {
//            File newDirectory = new File(PATH + "/APKs/" + array[i].loadLabel(mContext.getPackageManager()));
//            newDirectory.mkdirs();
//            File newLocation = new File(newDirectory, array[i].loadLabel(mContext.getPackageManager())+".apk");
            File extractedPath = new File(PATH + "/Extracted/" + array[i].loadLabel(mContext.getPackageManager()));
            extractedPath.mkdirs();
            try {
                extractUtils.unzip(array[i].sourceDir, extractedPath.getAbsolutePath());
//                String androidManifestPath = PATH + "/Extracted/" + array[i].loadLabel(mContext.getPackageManager()) + "/AndroidManifest.xml";
                Log.d("TAG sourceDir ",""+array[i].sourceDir);
//                File[] layouts = new File(PATH+"/Extracted/"+ array[i].loadLabel(mContext.getPackageManager())+"/res/layout").listFiles();
                String manifest = extractUtils.getIntents(array[i].sourceDir);
                Formatter formatter = new Formatter(extractedPath.getAbsolutePath()+"/AndroidManifest.xml");
                formatter.format(manifest);
                formatter.close();
                 //method to decompress calls decompressXML


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            Snackbar.make(v, "PATH : " + mPath, Snackbar.LENGTH_SHORT).setAction("Action", null).show();

        }
    }
}
