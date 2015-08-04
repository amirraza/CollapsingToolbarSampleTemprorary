package com.world.extremeapkeditor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

/**
 * Created by AmirRaza on 7/5/2015.
 */
public class AllAppsFragment extends Fragment {
//    private PackageManager packageManager = null;
//    private List<ApplicationInfo> applist = null;
//    private ApplicationInfo[] mAppInfo;
//    private String[] names;
//    Drawable[] icons;
//    public MyAllAppsAdapter adapter;
    public ListView listView;
    ProgressBar progressBar;
//    private String[] packageName;
//    float[] appSize;
    public static AllAppsFragment allAppsFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ON", "onCreate");
//        new MyTask().execute();
    }

    public static AllAppsFragment getInstance(){
        if(allAppsFragment == null)
            allAppsFragment = new AllAppsFragment();

        return allAppsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_apps, container, false);
        progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        listView = (ListView) v.findViewById(R.id.myListView);
        new MyBackgroundTask(getActivity().getBaseContext(),progressBar,listView,1).execute();
        return v;

    }

//    class MyAllAppsAdapter extends BaseAdapter {
//
//        Context context;
//        ApplicationInfo applicationInfo[];
//        public MyAllAppsAdapter(Context context, ApplicationInfo[] applicationInfo) {
//            this.context = context;
//            this.applicationInfo = applicationInfo;
//        }
//
//        @Override
//        public int getCount() {
//            return applicationInfo.length;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return applicationInfo[position];
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            if (convertView == null) {
//                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                convertView = inflater.inflate(R.layout.single_item, null, false);
//            }
//
//            TextView appName = (TextView) convertView.findViewById(R.id.appName);
//            appName.setText(applicationInfo[position].loadLabel(getActivity().getPackageManager()));
//
//            final TextView packageName = (TextView) convertView.findViewById(R.id.packageName);
//            packageName.setText(applicationInfo[position].packageName);
//
//            TextView appSize = (TextView) convertView.findViewById(R.id.appSize);
////            appSize.setText(this.appSize[position] / (1024) + "Mb");
//
//            ImageView appImage = (ImageView) convertView.findViewById(R.id.appImage);
//            appImage.setImageDrawable(applicationInfo[position].loadIcon(getActivity().getPackageManager()));
//
//            ImageView popupImage = (ImageView) convertView.findViewById(R.id.myPopUpButton);
//            popupImage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(final View view) {
//                    PopupMenu popupMenu = new PopupMenu(getActivity(),view);
//                    popupMenu.getMenuInflater().inflate(R.menu.my_popup_menu, popupMenu.getMenu());
//                    popupMenu.show();
//                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(MenuItem menuItem) {
//                            switch (menuItem.getItemId()){
//                                case R.id.extract:
//                                    Toast.makeText(getActivity(),"Clicked "+menuItem.getTitle(),Toast.LENGTH_SHORT).show();
//                                    break;
//                            }
//                            return false;
//                        }
//                    });
//                }
//            });
//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(getActivity(), CheeseDetailActivity.class);
//                    intent.putExtra("Name",applicationInfo[position]);
//                    startActivity(intent);
//                }
//            });
//            return convertView;
//        }
//    }
//
//    class MyTask extends AsyncTask<String, String, String> {
//        @Override
//        protected String doInBackground(String... params) {
//            packageManager = getActivity().getPackageManager();
//            applist = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
//            mAppInfo = applist.toArray(new ApplicationInfo[applist.size()]);
//            int i = 0;
//            names = new String[mAppInfo.length];
//            icons = new Drawable[mAppInfo.length];
//            packageName = new String[mAppInfo.length];
//            appSize = new float[mAppInfo.length];
//
//
//            for (ApplicationInfo s : mAppInfo) {
//
//                try {
//                    names[i] = (String) s.loadLabel(getActivity().getPackageManager());
//                    icons[i] = s.loadIcon(getActivity().getPackageManager());
//                    packageName[i] = s.packageName;
//                    long fileSize = new FileInputStream(mAppInfo[i].sourceDir).getChannel().size();
//                    appSize[i] = fileSize;
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                i++;
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            progressBar.setVisibility(View.INVISIBLE);
//            adapter = new MyAllAppsAdapter(getActivity(),mAppInfo);
//
//            listView.setAdapter(adapter);
//        }
//
//    }
}
