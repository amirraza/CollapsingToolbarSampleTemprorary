package com.home.amirraza.collapsingtoolbarsample;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;
//import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by AmirRaza on 7/4/2015.
 */
public class CheeseDetailActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "cheese_name";
//
//    public static String AppName, PackageName;
//    public static Drawable AppImage;
    private ApplicationInfo applicaionInfo;
    String[] t;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        applicaionInfo = intent.getParcelableExtra("Name");
t=intent.getStringArrayExtra("lis");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
//        systemBarTintManager.setStatusBarTintEnabled(true);
//        systemBarTintManager.setTintColor(Color.parseColor("#60000000"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(applicaionInfo.loadLabel(getPackageManager()));

        TextView pkg = (TextView) findViewById(R.id.detailPackageName);
        pkg.setText(
                t+" \n\n"+

                "Label\n " + applicaionInfo.loadLabel(getPackageManager()) +
                        "permission\n" + applicaionInfo.permission +
                        "icon\n" + applicaionInfo.loadIcon(getPackageManager()) +
                        "backupAgentName " + applicaionInfo.backupAgentName +
                        "dataDir " + applicaionInfo.dataDir +
                        "manageSpaceActivityName " + applicaionInfo.manageSpaceActivityName +
                        "nativeLibraryDir " + applicaionInfo.nativeLibraryDir +
                        "className " + applicaionInfo.className +
                        "processName " + applicaionInfo.processName +
                        "publicSourceDir " + applicaionInfo.publicSourceDir +
                        "sourceDir " + applicaionInfo.sourceDir +
                        "taskAffinity " + applicaionInfo.taskAffinity +
                        "name " + applicaionInfo.name +
                        "describeContents() " + applicaionInfo.describeContents() +
                        "descriptionRes " + applicaionInfo.descriptionRes +
                        "compatibleWidthLimitDp " + applicaionInfo.compatibleWidthLimitDp +
                        "nonLocalizableLabel " + applicaionInfo.nonLocalizedLabel +
                        "sharedLibraryFiles " + applicaionInfo.sharedLibraryFiles +
                        "uid " + applicaionInfo.uid
        );

        TextView detail = (TextView) findViewById(R.id.detailDetails);
        detail.setText( applicaionInfo.packageName);


        loadBackdrop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(Cheeses.getRandomCheeseDrawable()).centerCrop().into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }
}
