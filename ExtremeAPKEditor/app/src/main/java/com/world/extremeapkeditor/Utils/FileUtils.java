package com.world.extremeapkeditor.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Formatter;

/**
 * Created by Abdul Wahid on 7/5/2015.
 */
public class FileUtils {
    Context mContext;
    Formatter formatter;
    ExtractUtils extractUtils;
    public static FileUtils fileUtils;

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
        ProgressDialog dialog = ProgressDialog.show(mContext, "Extracting...", "Please wait", true, true);
        File appFile = new File(mAppInfo[i].sourceDir);
        String PATH = Environment.getExternalStorageDirectory().getPath() + "/ExtremeApkEditor/";
        File newDirectory = new File(PATH + "/APKs/" + mAppInfo[i].loadLabel(mContext.getPackageManager()));
        newDirectory.mkdirs();
        File newLocation = new File(newDirectory, "app.apk");

//                Boolean b = appFile.renameTo(newLocation);
        try {
            extractUtils.unzip(mAppInfo[i].sourceDir, PATH + "/Extracted/" + mAppInfo[i].loadLabel(mContext.getPackageManager()));
            String androidManifestPath = PATH + "/Extracted/" + mAppInfo[i].loadLabel(mContext.getPackageManager())+"/AndroidManifest.xml";
            File xmlFile = new File(androidManifestPath);
            String manifest = extractUtils.getIntents(newLocation.getAbsolutePath()); //method to decompress calls decompressXML
            formatter = new Formatter(xmlFile);
            formatter.format(manifest);
            formatter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.dismiss();
        Snackbar.make(snackBarView,newLocation.getAbsolutePath(),Snackbar.LENGTH_LONG).setAction("Action", null).show();
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
}
