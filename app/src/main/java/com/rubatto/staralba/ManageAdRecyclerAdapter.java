package com.rubatto.staralba;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 2017-09-12.
 */

public class ManageAdRecyclerAdapter extends RecyclerView.Adapter<ManageAdRecyclerAdapter.ViewHolder> {

    private ArrayList<DataMyBusinessInfo> mDataList;
    private Activity mRootActivity;
    private DeleteDataTask mDataTask;

    /*public MainSurroundAdapter(ArrayList<String> arrayList, Activity activity) {
        this.rootActivity = activity;
        dataList = new ArrayList<>();
        this.dataList = arrayList;
    }*/

    public ManageAdRecyclerAdapter(ArrayList<DataMyBusinessInfo> dataList, Activity activity) {
        this.mDataList = dataList;
        this.mRootActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_ad_recyclerview, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //holder.mProfileImg.setImageResource(R.drawable.female);
        holder.mProfileTv.setText(mDataList.get(position).BUSINESS_NAME);
        holder.mBusinessNameTv.setText(mDataList.get(position).BUSINESS_NAME);
        holder.mTitleTv.setText(mDataList.get(position).TITLE);

        if (mDataList.get(position).USE_YN.equals("N")) {
            holder.mStatusTv.setText("광고");
            holder.mRemainTv.setText("대기");
            holder.mStartBt.setText("광고 시작");
            holder.mStartBt.setBackground(mRootActivity.getResources().getDrawable(R.color.colorPrimary));
            holder.mStartBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mRootActivity, "인앱 결제", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (mDataList.get(position).USE_YN.equals("Y")) {
            String start_dt = mDataList.get(position).START_DT;
            String end_dt = mDataList.get(position).END_DT;
            Log.e("manageAdadapter", end_dt);
            String remain_date = "";

            if ("null".equals(end_dt) || null == end_dt || "".equals(end_dt)) {
                remain_date = "-1";
            } else {
                try {
                    remain_date = diffOfDate(end_dt.substring(0, 8));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (Integer.parseInt(remain_date) > 0) {
                holder.mStatusTv.setText("광고중");
                holder.mRemainTv.setText("D-" + remain_date);
                holder.mStartBt.setText("광고 중지");
                holder.mStartBt.setTextColor(mRootActivity.getColor(R.color.colorPrimary));
                holder.mStartBt.setBackground(mRootActivity.getResources().getDrawable(R.drawable.red_rectangle_stroke_border));
                holder.mStartBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mRootActivity, "Update USE_YN -> N", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                holder.mStatusTv.setText("광고");
                holder.mRemainTv.setText("종료");
                holder.mStartBt.setText("광고 연장");
                holder.mStartBt.setTextColor(mRootActivity.getColor(R.color.white));
                holder.mStartBt.setBackgroundColor(mRootActivity.getColor(R.color.colorPrimary));
                holder.mStartBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mRootActivity, "인앱 결제", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        holder.mModifyBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mRootActivity, ModifyActivity.class);
                i.putExtra("POS", String.valueOf(position));
                mRootActivity.startActivity(i);
            }
        });
        holder.mDeleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mRootActivity);
                // 제목셋팅
                alertDialogBuilder.setTitle("광고 삭제");
                // AlertDialog 셋팅
                alertDialogBuilder
                        .setMessage("광고를 삭제할 것입니까?")
                        .setCancelable(false)
                        .setPositiveButton("삭제",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        // 삭제 쿼리를 날림
                                        String business_seq = String.valueOf(mDataList.get(position).BUSINESS_SEQ);
                                        postData(business_seq);
                                    }
                                })
                        .setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        // 다이얼로그를 취소한다
                                        dialog.cancel();
                                    }
                                });
                // 다이얼로그 생성
                AlertDialog alertDialog = alertDialogBuilder.create();
                // 다이얼로그 보여주기
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mItemLayout;
        public ImageView mProfileImg;
        public TextView mProfileTv;
        public TextView mBusinessNameTv;
        public TextView mTitleTv;
        public TextView mStatusTv;
        public TextView mRemainTv;
        public Button mStartBt;
        public Button mModifyBt;
        public Button mDeleteBt;

        public ViewHolder(View view) {
            super(view);
            mItemLayout = (LinearLayout) view.findViewById(R.id.manage_ad_recyclerview_layout);
            mProfileImg = (ImageView) view.findViewById(R.id.manage_ad_profile_img);
            mProfileTv = (TextView) view.findViewById(R.id.manage_ad_profile_tv);
            mBusinessNameTv = (TextView) view.findViewById(R.id.manage_ad_business_name_tv);
            mTitleTv = (TextView) view.findViewById(R.id.manage_ad_title_tv);
            mStatusTv = (TextView) view.findViewById(R.id.manage_ad_status_tv);
            mRemainTv = (TextView) view.findViewById(R.id.manage_ad_remain_tv);
            mStartBt = (Button) view.findViewById(R.id.manage_ad_start_bt);
            mModifyBt = (Button) view.findViewById(R.id.manage_ad_modify_bt);
            mDeleteBt = (Button) view.findViewById(R.id.manage_ad_delete_bt);
        }
    }

    private String diffOfDate(String end) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

        Date currentDate = new Date();
        Date endDate = formatter.parse(end);

        Log.e("current", currentDate.toString());
        Log.e("end", endDate.toString());

        long diff = endDate.getTime() - currentDate.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);

        return String.valueOf(diffDays);
    }

    private void postData(String businessSeq) {
        String result = "";
        String insertOk = "";
        mDataTask = new DeleteDataTask();
        try {
            result = mDataTask.execute(businessSeq).get();

            if (!("".equals(result) || null == result)) {
                JSONObject jsonObj = new JSONObject(result);
                insertOk = jsonObj.getString("data");

                if (insertOk.equals("1")) {
                    Toast.makeText(mRootActivity, "삭제되었습니다!", Toast.LENGTH_SHORT).show();
                    mRootActivity.finish();
                    // TODO: 2017-09-21 intent 작성
                } else {
                    Toast.makeText(mRootActivity, "관리자에게 문의해 주세요", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mRootActivity, "잘못된 형식입니다.\n다시 작성해 주세요", Toast.LENGTH_SHORT).show();
                mRootActivity.finish();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class DeleteDataTask extends AsyncTask<String, Void, String> {
        private String sendMsg;
        private String receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                String urlString = "http://staralba3336.cafe24.com/star/mobile/deleteBusinessInfo.do";
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                String business_seq = strings[0];
                sendMsg = "BUSINESS_SEQ=" + business_seq;
                Log.e("delete]sendmsg", sendMsg);
                osw.write(sendMsg);
                osw.flush();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder buffer = new StringBuilder();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                        receiveMsg = buffer.toString();
                    }
                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onCancelled() {
            mDataTask = null;
        }
    }
}