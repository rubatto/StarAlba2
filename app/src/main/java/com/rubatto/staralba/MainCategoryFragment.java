package com.rubatto.staralba;

import android.app.Dialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class MainCategoryFragment extends Fragment {
    private TabLayout mTabLayout;

    private ViewPager mViewPager;
    private TabPagerAdapter mTabPagerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_category, container, false);

        mTabLayout = (TabLayout) view.findViewById(R.id.main_category_fragment_tablayout);
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.frag_tab_label0));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.frag_tab_label1));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.frag_tab_label2));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.frag_tab_label3));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.frag_tab_label4));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.frag_tab_label5));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.frag_tab_label6));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.frag_tab_label7));
        //mTabLayout.setTabTextColors(getResources().getColor(android.R.color.background_dark), getResources().getColor(R.color.colorAccent));
        mViewPager = (ViewPager) view.findViewById(R.id.main_category_fragment_viewpager);
        mTabPagerAdapter = new TabPagerAdapter(getActivity().getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(mTabPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }
}
