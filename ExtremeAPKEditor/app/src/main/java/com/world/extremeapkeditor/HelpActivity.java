package com.world.extremeapkeditor;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.world.extremeapkeditor.R;
import com.world.extremeapkeditor.Utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;

/**
 * Created by abdulazizniazi on 8/12/15.
 */
public class HelpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_activity);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
