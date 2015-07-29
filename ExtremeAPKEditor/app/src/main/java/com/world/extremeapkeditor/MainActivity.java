package com.world.extremeapkeditor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.world.extremeapkeditor.Adapters.ViewPagerAdapter;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    CharSequence Titles[]={"All","User","System"};
    int Numboftabs =3;
    public ViewPagerAdapter viewPagerAdapter;
    private DrawerLayout mDrawerLayout;
    public ViewPager viewPager;
    private AlertDialog.Builder fileDialog;

    //    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Extreme APK Extractor");
        toolbar.setLogo(R.drawable.app_icon12);
//        toolbar.setNavigationIcon(R.drawable.app_icon1);
        toolbar.setCollapsible(true);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" Extreme APK Extractor");
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
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);
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

            case R.id.from_file:

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
//                intent.putExtra("FORMAT_FILTER","apk");
                Intent.createChooser(intent, "Choose APK From File");
                startActivityForResult(intent,0);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && data != null && resultCode == Activity.RESULT_OK){
            Uri path = data.getData();
            Log.d("PATH",""+path.toString().replaceAll("%20",""));
            if (path.toString().endsWith("apk")){
                fileDialog = new  AlertDialog.Builder(this);
                PackageManager packageManager = getPackageManager();
                String pathString = path.getPath();
                PackageInfo packageInfo = packageManager.getPackageArchiveInfo(pathString, 0);
                Log.d("PATH",""+packageInfo.packageName);

                View dialogView = View.inflate(this, R.layout.dialog, null);
                ImageView file_icon  = (ImageView) dialogView.findViewById(R.id.file_icon);
                TextView file_title = (TextView) dialogView.findViewById(R.id.file_title);
                TextView file_pakage = (TextView) dialogView.findViewById(R.id.file_package);
                TextView file_size = (TextView) dialogView.findViewById(R.id.file_size);
                packageInfo.applicationInfo.sourceDir = pathString;
                packageInfo.applicationInfo.publicSourceDir= pathString;
                fileDialog.setView(dialogView);
                file_icon .setImageDrawable(packageInfo.applicationInfo.loadIcon(getPackageManager()));
                file_title.setText(packageInfo.applicationInfo.loadLabel(packageManager));
                file_pakage.setText(packageInfo.applicationInfo.packageName);
                file_size.setText(String.format("%.2f",(float)new File(pathString).length()/(1024*1024))+" mb");
                fileDialog.setNegativeButton("Close", null);
                fileDialog.create();
                fileDialog.show();
            }

        }
    }
}