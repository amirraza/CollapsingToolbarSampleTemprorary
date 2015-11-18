package com.plutontech.apkmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.plutontech.apkmanager.Adapters.ViewPagerAdapter;
import com.plutontech.apkmanager.Utils.FileUtils;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    CharSequence Titles[] = {"All", "User", "System"};
    int Numboftabs = 3;
    public ViewPagerAdapter viewPagerAdapter;
    //    private DrawerLayout mDrawerLayout;
    public ViewPager viewPager;
    View floatingActionButton;
    private AlertDialog fileDialog;
    private View mainView;
    public static View coverView;
    public static boolean done =false;

    //    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coverView = findViewById(R.id.loadingCover);
        final AdView mAdView = (AdView) findViewById(R.id.adView);
//        mAdView.setAdSize();
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
//                mAdView.setVisibility(View.VISIBLE);
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

//        mainView =((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_main,null,false);
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/vnd.android.package-archive");
//                Intent.createChooser(intent, "Choose APK From File");
                startActivityForResult(intent, 0);
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle(" APK Extractor");
//        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
//        toolbar.setLogo(R.drawable.app_icon_invert);
//        toolbar.setNavigationIcon(R.drawable.app_icon1);
        toolbar.setCollapsible(true);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("APK Extractor");
//        ActionBar ab = getSupportActionBar();
//        ab.setDisplayShowTitleEnabled(true);

//        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
//        ab.setDisplayHomeAsUpEnabled(true);

//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//
//        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.abc_action_bar_home_description, R.string.abc_action_bar_home_description)
//        {
//            public void onDrawerSlide(View drawerView, float slideOffset)
//            {
//                findViewById(R.id.main_content).setTranslationX((drawerView.getWidth()*slideOffset));
//            }
//        };

//        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(viewPagerAdapter);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && data != null && resultCode == Activity.RESULT_OK) {
            Uri path = data.getData();
            try {
                PackageManager packageManager = getPackageManager();
                String pathString = path.getPath();
                final PackageInfo packageInfo = packageManager.getPackageArchiveInfo(pathString, 0);
                packageInfo.applicationInfo.loadLabel(packageManager);
            } catch (Exception e) {
                requestCode = 10;
            }


            String name = "content://" + path.getPath().replace(".apk", "");
            Log.d("PATH", "" + Uri.parse(name));

            Log.d("PATH", "" + Boolean.toString(path.getAuthority() == null));

            if (requestCode == 0) {
                fileDialog = new AlertDialog.Builder(this).create();
                PackageManager packageManager2 = getPackageManager();
                String pathString2 = path.getPath();
                final PackageInfo packageInfo2 = packageManager2.getPackageArchiveInfo(pathString2, 0);
                Log.d("PATH", "" + packageInfo2.packageName);
                View dialogView = View.inflate(this, R.layout.dialog, null);
                Button extractButton = (Button) dialogView.findViewById(R.id.extract_dialog);
                Button shareButton = (Button) dialogView.findViewById(R.id.share_dialog);
                extractButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ApplicationInfo[] appInfo = new ApplicationInfo[1];
                        appInfo[0] = packageInfo2.applicationInfo;
                        FileUtils.getInstance(MainActivity.this).extract(0, appInfo, MainActivity.this.findViewById(android.R.id.content).getRootView());
                        fileDialog.dismiss();

                    }
                });

                shareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FileUtils.getInstance(MainActivity.this).share(new File(packageInfo2.applicationInfo.publicSourceDir));

                    }
                });


                ImageView file_icon = (ImageView) dialogView.findViewById(R.id.file_icon);
                TextView file_title = (TextView) dialogView.findViewById(R.id.file_title);
                TextView file_package = (TextView) dialogView.findViewById(R.id.file_package);
                TextView file_size = (TextView) dialogView.findViewById(R.id.file_size);
                packageInfo2.applicationInfo.sourceDir = pathString2;
                packageInfo2.applicationInfo.publicSourceDir = pathString2;
                fileDialog.setView(dialogView);
                file_icon.setImageDrawable(packageInfo2.applicationInfo.loadIcon(getPackageManager()));
                file_title.setText(packageInfo2.applicationInfo.loadLabel(packageManager2));
                file_package.setText(packageInfo2.applicationInfo.packageName);
                file_size.setText(String.format("%.2f", (float) new File(pathString2).length() / (1024 * 1024)) + " mb");
                fileDialog.setButton(Dialog.BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        fileDialog.dismiss();
                    }
                });
                fileDialog.show();
            } else {
                Snackbar.make(MainActivity.this.findViewById(android.R.id.content)
                        .getRootView(), "The file selected is not .apk extension file", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }

        }
    }
}