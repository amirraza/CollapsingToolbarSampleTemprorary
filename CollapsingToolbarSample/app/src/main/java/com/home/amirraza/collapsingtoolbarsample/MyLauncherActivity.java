package com.home.amirraza.collapsingtoolbarsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by AmirRaza on 7/5/2015.
 */
public class MyLauncherActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView open;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        open = (ImageView) findViewById(R.id.open);
        open.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.open){
            Intent intent = new Intent(MyLauncherActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }
}
