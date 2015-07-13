package com.home.amirraza.collapsingtoolbarsample.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Environment;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.home.amirraza.collapsingtoolbarsample.CheeseDetailActivity;
import com.home.amirraza.collapsingtoolbarsample.R;
import com.home.amirraza.collapsingtoolbarsample.Utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Amir on 7/8/2015.
 */
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
        PackageStats stats = new PackageStats(applicationInfo[position].sourceDir+"/"+applicationInfo[position].packageName);


        TextView appSize = (TextView) convertView.findViewById(R.id.appSize);
        float[] apppSize=null;
        long fileSize=0;
        try {
             fileSize = new FileInputStream(applicationInfo[position].sourceDir).getChannel().size();
             apppSize = new float[1];
            apppSize[0] = fileSize;
        } catch (IOException e) {
            e.printStackTrace();
        }
        appSize.setText(stats.codeSize+" "+stats.dataSize+ "Mb");

        ImageView appImage = (ImageView) convertView.findViewById(R.id.appImage);
        appImage.setImageDrawable(applicationInfo[position].loadIcon(context.getPackageManager()));

        ImageView popupImage = (ImageView) convertView.findViewById(R.id.myPopUpButton);
        popupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popupMenu = new PopupMenu(context,view);
                popupMenu.getMenuInflater().inflate(R.menu.my_popup_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
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
                                    FileUtils.getInstance(context).copy(new File(applicationInfo[position].sourceDir),
                                            new File(Environment.getExternalStorageDirectory().getPath() + "/ExtremeApkEditor/APKs/"
                                                    +appNameString,appNameString+".apk"));
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
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CheeseDetailActivity.class);
//                temList = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
//                String[] myListArray = temList.toArray(new String[temList.size()]);
//                Log.d("TAG", "" + temList);
                intent.putExtra("Name",applicationInfo[position]);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}