package com.plutontech.apkmanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.plutontech.apkmanager.Utils.ExtractUtils;

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
    File baseFile;
    private File allFiles;
    private File fileDir;
    private String currentApp;
    private ProgressDialog progressDialog;
    private boolean notBack = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_activity);

        currentApp = getIntent().getStringExtra("INFO");
        Log.d("APPXC", "" + currentApp);
        extractUtils = new ExtractUtils();
        try {
            Toolbar appbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(appbar);
            progressDialog = ProgressDialog.show(this,"","loading...",true,false);
            parser = new ApkParser(currentApp);
            baseFile = new File(Environment.getExternalStorageDirectory() + "/tmp/" );
            baseFile.mkdirs();

             fileDir = new File(baseFile, currentApp.substring(currentApp.lastIndexOf("/")));
            fileDir.mkdirs();
            if (!fileDir.exists()){
                finish();
                Toast.makeText(this,"There is an error in file handling", Toast.LENGTH_LONG).show();
            }
            new loaderTask().execute();
            fileList = (ListView) findViewById(R.id.file_list);
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
                        } else if (listOfFiles[i].getAbsolutePath().endsWith("png") || listOfFiles[i].getAbsolutePath().endsWith("jpg")) {
                            cover = findViewById(R.id.mLayout);

                            ImageView imageView = (ImageView) findViewById(R.id.image_viewed);
                            imageView.setImageURI(Uri.fromFile(listOfFiles[i]));
                            cover.setVisibility(View.VISIBLE);
                            visible = true;

                        }
                    } else {
                        currentLocation = listOfFiles[i];
                        fileList.setAdapter(new FileAdapter(FileActivity.this, 0, listOfFiles[i]));
//                        listOfFiles = listOfFiles[i].listFiles();
                    }

                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Log.d("APPX path", "" + currentLocation + " " + allFiles.getAbsolutePath());
        Log.d("APPX path", "VISIBLE " + visible);
        if (visible) {
            cover.setVisibility(View.INVISIBLE);
            visible = false;

        } else {
            if (currentLocation != null && currentLocation.getParentFile() != null) {
                currentLocation = currentLocation.getParentFile();
                if (currentLocation.getAbsolutePath().equals(allFiles.getAbsolutePath())) {
                    finish();
                } else {

                    Log.d("APPX path", "VISIBLE Happening");
                    fileList.setAdapter(new FileAdapter(this, 0, currentLocation));

                }
            } else {
                notBack = true;
                new loaderTask().execute();
//                Toast.makeText(this,""+baseFile.exists(),Toast.LENGTH_SHORT).show();
            }


        }
    }
    void DeleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                DeleteRecursive(child);

        fileOrDirectory.delete();
    }

    class FileAdapter extends ArrayAdapter<String> {
        View view;
        File files;

        public FileAdapter(Context context, int resource, File mainFile) {
            super(context, resource, mainFile.list());
            files = mainFile;
            listOfFiles = mainFile.listFiles();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){


            }
            view = getLayoutInflater().inflate(R.layout.file_list_item, null, false);
            ImageView fileIcon = (ImageView) view.findViewById(R.id.file_icon);
            TextView fileName = (TextView) view.findViewById(R.id.file_name);
            fileName.setText(getItem(position));
            Log.d("APPX", "" + getItem(position));
            if (files.listFiles()[position].isDirectory()) {
                fileIcon.setBackgroundResource(R.drawable.folder_icon_1);
            } else {
                if (getItem(position).endsWith(".xml")) {
                    fileIcon.setBackgroundResource(R.drawable.xml_file);
                } else if (getItem(position).endsWith(".png") || getItem(position).endsWith(".jpg")  ) {
                    fileIcon.setBackgroundResource(R.drawable.res_enabled);
                } else {
                    fileIcon.setBackgroundResource(R.drawable.res_disabled);
                }
            }

            return view;
        }
    }
    class loaderTask extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            if (notBack){

                try {
                    extractUtils.unzip(currentApp, fileDir.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("APPX", "" + fileDir.getAbsolutePath());
                allFiles = new File(Environment.getExternalStorageDirectory() + "/tmp/" + currentApp.substring(currentApp.lastIndexOf("/")));
            }else{
                DeleteRecursive(baseFile);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (notBack) {
                FileAdapter adapter = new FileAdapter(FileActivity.this, android.R.layout.simple_list_item_1, allFiles);
                listOfFiles = allFiles.listFiles();
                fileList.setAdapter(adapter);
                progressDialog.dismiss();
            }else{
                finish();

            }

        }
    }
}
