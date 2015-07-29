package com.world.extremeapkeditor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageStats;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;


import com.world.extremeapkeditor.CheeseDetailActivity;
import com.world.extremeapkeditor.R;
import com.world.extremeapkeditor.Utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Formatter;
import java.util.List;

/**
 * Created by Amir on 7/8/2015.
 */

//http://keepsafe.github.io/2014/11/19/building-a-custom-overflow-menu.html
//Custom popup menu

public class GeneralAdapter extends BaseAdapter {
    Context context;
    ApplicationInfo applicationInfo[];
    List<PackageInfo> temList;

    public GeneralAdapter(Context context, ApplicationInfo[] applicationInfo) {
        this.context = context;
        this.applicationInfo = applicationInfo;
    }

    @Override
    public int getCount() {
        return applicationInfo.length;
    }

    @Override
    public Object getItem(int position) {
        return applicationInfo[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.single_item, null, false);
        }

        final TextView appName = (TextView) convertView.findViewById(R.id.appName);
        final String appNameString = (String) applicationInfo[position].loadLabel(context.getPackageManager());
        appName.setText(appNameString);

        final TextView packageName = (TextView) convertView.findViewById(R.id.packageName);
        packageName.setText(applicationInfo[position].packageName);
//        PackageStats stats = new PackageStats(applicationInfo[position].sourceDir + "/" + applicationInfo[position].packageName);


        TextView appSize = (TextView) convertView.findViewById(R.id.appSize);
        float[] apppSize = null;
        long fileSize = new File(applicationInfo[position].sourceDir).length();
        try {
//            fileSize = new FileInputStream(applicationInfo[position].sourceDir).getChannel().size();
//            apppSize = new float[1];
//            apppSize[0] = fileSize;
        } catch (Exception e) {
            e.printStackTrace();
        }
        String size = String.format("%.2f", (float)fileSize / (1024 * 1024));
        appSize.setText( size+ "Mb");

        ImageView appImage = (ImageView) convertView.findViewById(R.id.appImage);
        appImage.setImageDrawable(applicationInfo[position].loadIcon(context.getPackageManager()));

        final ImageView popupImage = (ImageView) convertView.findViewById(R.id.myPopUpButton);
        popupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenuInflater().inflate(R.menu.my_popup_menu, popupMenu.getMenu());

                Object menuHelper;
                Class[] argTypes;
                try {
                    Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
                    fMenuHelper.setAccessible(true);
                    menuHelper = fMenuHelper.get(popupMenu);
                    argTypes = new Class[]{boolean.class};
                    menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
                } catch (Exception e) {
                    // Possible exceptions are NoSuchMethodError and NoSuchFieldError
                    //
                    // In either case, an exception indicates something is wrong with the reflection code, or the
                    // structure of the PopupMenu class or its dependencies has changed.
                    //
                    // These exceptions should never happen since we're shipping the AppCompat library in our own apk,
                    // but in the case that they do, we simply can't force icons to display, so log the error and
                    // show the menu normally.

                    Log.d("TAG", "error forcing menu icons to show", e);
                    popupMenu.show();
                    return;
                }

                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.extract:
                                FileUtils.getInstance(context).extract(position, applicationInfo, view);
                                break;
                            case R.id.share:
                                FileUtils.getInstance(context).share(new File(applicationInfo[position].publicSourceDir));
                                break;
                            case R.id.uninstall:
                                FileUtils.getInstance(context).getInfo(applicationInfo[position].packageName);
                                break;
                            case R.id.copy:
                                try {
                                    File fileDirectory = new File(Environment.getExternalStorageDirectory().getPath() + "/ExtremeApkEditor/APKs/"
                                            + appNameString);
                                    fileDirectory.mkdirs();
                                    FileUtils.getInstance(context).copy(new File(applicationInfo[position].publicSourceDir),
                                            new File(fileDirectory, appNameString + ".apk"));
                                    Snackbar.make(view, "" + fileDirectory.getAbsolutePath(), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                        return false;
                    }
                });

            }
        });
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, CheeseDetailActivity.class);
////                temList = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
////                String[] myListArray = temList.toArray(new String[temList.size()]);
////                Log.d("TAG", "" + temList);
//                intent.putExtra("Name", applicationInfo[position]);
//                context.startActivity(intent);
//            }
//        });
        return convertView;
    }
}