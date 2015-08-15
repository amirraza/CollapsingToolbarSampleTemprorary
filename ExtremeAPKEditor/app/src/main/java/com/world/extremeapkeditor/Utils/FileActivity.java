package com.world.extremeapkeditor.Utils;

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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;

/**
 * Created by abdulazizniazi on 8/12/15.
 */
public class FileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_activity);
        ApplicationInfo appInfo = getIntent().getParcelableExtra("appInfo");
        Intent fileExploreIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        fileExploreIntent.setType("file/*");
        Uri uri = Uri.parse(FileUtils.getInstance(this).PATH + "/Extracted/" + appInfo.loadLabel(getPackageManager()));
        fileExploreIntent.setDataAndType(uri, "file/*");
//        fileExploreIntent.setData(uri);
        startActivityForResult(Intent.createChooser(fileExploreIntent, "Choose file"), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && data != null) {
            finish();
//            try {
//                Uri mUri = data.getData();
//                if (mUri.toString().endsWith(".xml")) {
//                    ExtractUtils extractUtils = new ExtractUtils();
//                    FileInputStream fileInputStream = new FileInputStream(mUri.getPath());
//                    ArrayList<byte[]> hello = new ArrayList<>();
////                    while(fileInputStream.available()>0){
////                        byte[] temp = new byte[fileInputStream.available()];
////                        fileInputStream.read(temp);
////                        Log.d("TAGFILE", temp.length + " //////");
////                        hello.add(temp);
////                    }
//
//                    byte[] FINAL = new byte[10240];
//                    fileInputStream.read(FINAL);
////                    int total = 0;
////                    for (byte[] mByte : hello){
////                        total += mByte.length;
////                    }
////                    byte[] stream = new byte[total];
////                    int fIndex = 0;
////                    for (byte[] mByte : hello){
////                        for (int index = 0;index<mByte.length;index++){
////                            stream[fIndex] = mByte[index];
////                            fIndex++;
////                        }
////                    }
//                    fileInputStream.close();
//                    String result = extractUtils.decode(FINAL);
//                    TextView textView = (TextView) findViewById(R.id.xml_text);
//                    textView.setText(result);
//                    Log.v("TAGFILE", "askldj" + mUri+result);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        }
    }
}
