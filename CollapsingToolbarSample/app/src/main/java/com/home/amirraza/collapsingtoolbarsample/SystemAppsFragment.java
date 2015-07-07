package com.home.amirraza.collapsingtoolbarsample;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by AmirRaza on 7/6/2015.
 */
public class SystemAppsFragment extends Fragment {
    private PackageManager packageManager = null;
    private List<ApplicationInfo> applist = null;
    private ApplicationInfo[] mAppInfo;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        packageManager = getActivity().getPackageManager();
        applist = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        mAppInfo = applist.toArray(new ApplicationInfo[applist.size()]);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_system_apps, container, false);
        setupRecyclerView(rv);
        return rv;

    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(),
                mAppInfo));
    }

    private List<String> getRandomSublist(String[] array, int amount) {
        ArrayList<String> list = new ArrayList<>(amount);
        Random random = new Random();
        while (list.size() < amount) {
            list.add(array[random.nextInt(array.length)]);
        }
        return list;
    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {
        private Context context;
        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<ApplicationInfo> mValues;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ApplicationInfo mBoundString;
            public String title;
            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar);
                mTextView = (TextView) view.findViewById(android.R.id.text1);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        public ApplicationInfo getValueAt(int position) {
            return mValues.get(position);
        }

        public SimpleStringRecyclerViewAdapter(Context context, ApplicationInfo[] items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = Arrays.asList(items);
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
//            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mBoundString = mValues.get(position);
            if(mValues.get(position).publicSourceDir.contains("/system/")){
                holder.title = (String) mValues.get(position).loadLabel(context.getPackageManager());
                holder.mTextView.setText(mValues.get(position).loadLabel(context.getPackageManager()));
                holder.mImageView.setBackgroundDrawable(mValues.get(position).loadIcon(context.getPackageManager()));
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        Context context = v.getContext();
//                        Intent intent = new Intent(context, CheeseDetailActivity.class);
//                        intent.putExtra(CheeseDetailActivity.EXTRA_NAME, holder.title);

//                        context.startActivity(intent);
                    }
                });
            }
            else{
                return;
            }


//            Glide.with(holder.mImageView.getContext())
//                    .load(Cheeses.getRandomCheeseDrawable())
//                    .fitCenter()
//                    .into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }

}
