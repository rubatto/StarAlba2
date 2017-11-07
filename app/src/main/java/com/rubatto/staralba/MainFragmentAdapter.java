package com.rubatto.staralba;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017-09-12.
 */

public class MainFragmentAdapter extends RecyclerView.Adapter<MainFragmentAdapter.ViewHolder> {

    private ArrayList<DataBusiness> mDataList;
    private Activity mRootActivity;

    /*public MainSurroundAdapter(ArrayList<String> arrayList, Activity activity) {
        this.rootActivity = activity;
        dataList = new ArrayList<>();
        this.dataList = arrayList;
    }*/

    public MainFragmentAdapter(ArrayList<DataBusiness> dataList, Activity activity) {
        this.mDataList = dataList;
        this.mRootActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_surround_recyclerview, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //holder.mProfileImg.setImageResource(R.drawable.female);
        holder.mProfileTv.setText(mDataList.get(position).BUSINESS_NAME);
        holder.mTitleTv.setText(mDataList.get(position).TITLE);
        String theme_List = "";
        for (int i = 0; i < mDataList.get(position).THEME_LIST.size(); i++) {
            if (i < 3) {
                if (theme_List.equals("")) {
                    theme_List = mDataList.get(position).THEME_LIST.get(i);
                } else {
                    theme_List = theme_List + " · " + mDataList.get(position).THEME_LIST.get(i);
                }
            }
        }
        holder.mThemeTv.setText(theme_List);
        holder.mTypeTv.setText(mDataList.get(position).CATEGORY_SE_NM);
        holder.mPayTv.setText(mDataList.get(position).PAY_SE_CD);
        if (mDataList.get(position).PAY_SE_CD.equals("0")) {
            holder.mPayTv.setText("협의");
            holder.mWageTv.setText("");
        } else if (mDataList.get(position).PAY_SE_CD.equals("1")) {
            holder.mPayTv.setText("시급");
            holder.mWageTv.setText(String.valueOf(mDataList.get(position).PAY));
        }
        holder.mDistanceTv.setText(GetPointAfterSecond(mDataList.get(position).DISTANCE));
        holder.mChatImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mRootActivity, position +"] chat click", Toast.LENGTH_SHORT).show();
            }
        });
        holder.mStarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mRootActivity, position +"] star click", Toast.LENGTH_SHORT).show();
            }
        });
        holder.mItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mRootActivity, DetailActivity.class);
                i.putExtra("POS", String.valueOf(position));
                mRootActivity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout mItemLayout;
        public ImageView mProfileImg;
        public TextView mProfileTv;
        public TextView mTitleTv;
        public TextView mThemeTv;
        public TextView mTypeTv;
        public TextView mPayTv;
        public TextView mWageTv;
        public TextView mDistanceTv;
        public ImageView mChatImg;
        public ImageView mStarImg;

        public ViewHolder(View view) {
            super(view);
            mItemLayout = (RelativeLayout) view.findViewById(R.id.surround_relativelayout);
            mProfileImg = (ImageView) view.findViewById(R.id.surround_profile_img);
            mProfileTv = (TextView) view.findViewById(R.id.surround_profile_tv);
            mTitleTv = (TextView) view.findViewById(R.id.surround_title_tv);
            mThemeTv = (TextView) view.findViewById(R.id.surround_theme_tv);
            mTypeTv = (TextView) view.findViewById(R.id.surround_type_tv);
            mPayTv = (TextView) view.findViewById(R.id.surround_pay_tv);
            mWageTv = (TextView) view.findViewById(R.id.surround_wage_tv);
            mDistanceTv = (TextView) view.findViewById(R.id.surround_distance_tv);
            mChatImg = (ImageView) view.findViewById(R.id.surround_chat_img);
            mStarImg = (ImageView) view.findViewById(R.id.surround_star_img);
        }
    }

    private String GetPointAfterSecond(double b) {
        DecimalFormat format = new DecimalFormat(".##");
        String str = format.format(b);

        return str +"Km";
    }
}