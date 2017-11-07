package com.rubatto.staralba;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class BusinessWebviewActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private WebView mWebview;
    private Dialog mDialog;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_webview);

        mToolbar = (Toolbar) findViewById(R.id.webview_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mWebview = (WebView) findViewById(R.id.business_webview_webview);
        mProgressBar = (ProgressBar) findViewById(R.id.business_webview_progress);

        mWebview.setWebViewClient(new WebClient());//정의한 클래스 사용
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setSupportZoom(true);
        mWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
            }
        });

        //웹뷰 다이얼로그
        mDialog = new Dialog(this, R.style.prograssdialog);
        mDialog.addContentView(
                new ProgressBar(this),
                new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT));

        Intent intent = getIntent();
        String lat = intent.getExtras().getString("LAT");
        String lng = intent.getExtras().getString("LNT");

        mWebview.loadUrl("http://staralba3336.cafe24.com/star/mobile/map2.do?LAT=" + lat + "&LNG=" + lng);
    }

    class WebClient extends WebViewClient {
        /*
        * 웹뷰 내 링크 터치 시 새로운 창이 뜨지 않고
        * 해당 웹뷰 안에서 새로운 페이지가 로딩되도록 함
           */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgressBar.setVisibility(View.VISIBLE);
        };

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mProgressBar.setVisibility(View.INVISIBLE);
        };

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Toast.makeText(BusinessWebviewActivity.this, "로딩오류" + description, Toast.LENGTH_SHORT).show();
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 백버튼
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return true;
    }
}
