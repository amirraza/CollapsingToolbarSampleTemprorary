package com.world.extremeapkeditor;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.world.extremeapkeditor.Utils.ExtractUtils;

import net.dongliu.apk.parser.ApkParser;

import java.io.File;
import java.io.IOException;

/**
 * Created by abdulazizniazi on 8/12/15.
 */
public class FileActivity extends AppCompatActivity {
    private ExtractUtils extractUtils;
    private ApkParser parser;
    private ListView fileList;
    boolean visible = false;
    private File[] listOfFiles;
    File currentLocation;
    private View cover;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_activity);
        final String currentApp = getIntent().getStringExtra("INFO");
        Log.d("APPXC", "" + currentApp);
        extractUtils = new ExtractUtils();
        try {
            Toolbar appbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(appbar);
            parser = new ApkParser(currentApp);
            File fileDir = new File(Environment.getExternalStorageDirectory() + "/tmp/" + currentApp.substring(currentApp.lastIndexOf("/")));
            fileDir.mkdirs();
            Log.d("APPX", "" + fileDir.getAbsolutePath());
            extractUtils.unzip(currentApp, fileDir.getAbsolutePath());
            final File allFiles = new File(Environment.getExternalStorageDirectory() + "/tmp/" + currentApp.substring(currentApp.lastIndexOf("/")));
            FileAdapter adapter = new FileAdapter(this, android.R.layout.simple_list_item_1, allFiles);
            listOfFiles = allFiles.listFiles();
            fileList = (ListView) findViewById(R.id.file_list);
            fileList.setAdapter(adapter);
            fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    if (listOfFiles[i].isFile()) {
                        Log.d("APPX type", "" + listOfFiles[i]);

                        if (listOfFiles[i].getAbsolutePath().endsWith("xml")) {
                            cover = findViewById(R.id.xml_scrollView);
                            TextView xml_text = (TextView) findViewById(R.id.xml_text);
                            try {
                                if (listOfFiles[i].toString().contains("AndroidManifest")) {
                                    String text = parser.getManifestXml();
                                    xml_text.setText(text);
                                    cover.setVisibility(View.VISIBLE);
                                    visible = true;
                                } else {
                                    String text = parser.transBinaryXml(listOfFiles[i].toString().substring(listOfFiles[i].toString().lastIndexOf(".apk/") + 5));
                                    xml_text.setText(text);
                                    cover.setVisibility(View.VISIBLE);
                                    visible = true;
//                                    Log.d("APPX type",""+ listOfFiles[i].toString().substring(listOfFiles[i].toString().lastIndexOf(".apk/")+5));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (listOfFiles[i].getAbsolutePath().endsWith("png")) {
                            cover = findViewById(R.id.mLayout);

                            ImageView imageView = (ImageView) findViewById(R.id.image_viewed);
                            imageView.setImageURI(Uri.fromFile(listOfFiles[i]));
                            cover.setVisibility(View.VISIBLE);
                            visible = true;

                        }
                    } else {
                        fileList.setAdapter(new FileAdapter(FileActivity.this, 0, listOfFiles[i]));
                        listOfFiles = listOfFiles[i].listFiles();
                    }

                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (visible) {
            cover.setVisibility(View.INVISIBLE);
            visible = false;
        } else {
            fileList.setAdapter(new FileAdapter(this, 0, currentLocation.getParentFile().getParentFile()));
            listOfFiles = currentLocation.getParentFile().getParentFile().listFiles();
        }
    }

    class FileAdapter extends ArrayAdapter<String> {
        View view;
        File files;

        public FileAdapter(Context context, int resource, File mainFile) {
            super(context, resource, mainFile.list());
            files = mainFile;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            view = getLayoutInflater().inflate(R.layout.file_list_item, null, false);
            ImageView fileIcon = (ImageView) view.findViewById(R.id.file_icon);
            TextView fileName = (TextView) view.findViewById(R.id.file_name);
            fileName.setText(getItem(position));
            Log.d("APPX", "" + getItem(position));
            if (files.listFiles()[position].isDirectory()) {
                fileIcon.setBackgroundResource(R.drawable.folder);
            } else {
                if (getItem(position).endsWith(".xml")) {
                    fileIcon.setBackgroundResource(R.drawable.xml_negro);

                } else {
                    fileIcon.setBackgroundResource(R.drawable.file_outline);
                }
            }

            return view;
        }
    }
}
