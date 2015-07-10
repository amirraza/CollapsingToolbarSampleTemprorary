package com.home.amirraza.collapsingtoolbarsample.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.home.amirraza.collapsingtoolbarsample.CheeseDetailActivity;
import com.home.amirraza.collapsingtoolbarsample.R;
import com.home.amirraza.collapsingtoolbarsample.Utils.FileUtils;

import java.io.File;

/**
 * Created by Amir on 7/8/2015.
 */
public class GeneralAdapter extends BaseAdapter {

    Context context;
    ApplicationInfo applicationInfo[];
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

        TextView appName = (TextView) convertView.findViewById(R.id.appName);
        appName.setText(applicationInfo[position].loadLabel(context.getPackageManager()));

        final TextView packageName = (TextView) convertView.findViewById(R.id.packageName);
        packageName.setText(applicationInfo[position].packageName);

        TextView appSize = (TextView) convertView.findViewById(R.id.appSize);
//            appSize.setText(this.appSize[position] / (1024) + "Mb");

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
                                Toast.makeText(context, "Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                                FileUtils.getInstance(context).extract(position, applicationInfo);
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
                intent.putExtra("Name",applicationInfo[position]);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}