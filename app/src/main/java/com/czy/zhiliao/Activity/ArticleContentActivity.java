package com.czy.zhiliao.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.czy.zhiliao.Bean.ArticleContent;
import com.czy.zhiliao.Listener.OnLoadArticleContentListener;
import com.czy.zhiliao.Net.HttpUtil;
import com.czy.zhiliao.R;
import com.czy.zhiliao.Utility.Constant;

/**
 * Created by ZY on 2016/7/26.
 * 利用WebView加载文章内容，CSS文件已存在本地
 */

public class ArticleContentActivity extends AppCompatActivity {

    private OnLoadArticleContentListener listener;

    private WebView webView;

    private ImageView hintImage;

    private TextView hintText;

    private enum STATUS {ALREADY_LOAD, WAIT_RETRY, IN_LOAD}

    private STATUS status;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_content);
        init();
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            id = bundle.getInt("ID");
            HttpUtil.getArticleContent(id, listener);
            status = STATUS.IN_LOAD;
        }
    }

    private void init() {
        hintImage = (ImageView) findViewById(R.id.hintImage);
        hintText = (TextView) findViewById(R.id.hintText);
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        webView.getSettings().setDatabaseEnabled(true);
        // 开启Application Cache功能
        webView.getSettings().setAppCacheEnabled(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("享受阅读的乐趣");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        listener = new OnLoadArticleContentListener() {
            @Override
            public void onSuccess(ArticleContent content) {
                CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
                mCollapsingToolbarLayout.setTitle(content.getTitle());
                webView.loadDataWithBaseURL("x-data://base", content.getBody(), "text/html", "UTF-8", null);
                ImageView imageView = (ImageView) findViewById(R.id.headImage);
                Constant.getImageLoader().displayImage(content.getImage(), imageView, Constant.getDisplayImageOptions());

                stopAnimation(hintImage);
                hintImage.setVisibility(View.GONE);
                hintText.setVisibility(View.GONE);
                status = STATUS.ALREADY_LOAD;
            }

            @Override
            public void onFailure() {
                stopAnimation(hintImage);
                hintImage.setImageResource(R.drawable.retry);
                hintText.setText("点击重试");
                status = STATUS.WAIT_RETRY;
                Snackbar snackbar;
                if (!HttpUtil.isNetworkConnected(ArticleContentActivity.this)) {
                    snackbar = Snackbar.make(webView, "似乎没有连接网络?", Snackbar.LENGTH_SHORT);
                } else {
                    snackbar = Snackbar.make(webView, "好奇怪，文章加载不出来", Snackbar.LENGTH_SHORT);
                }
                snackbar.getView().setBackgroundColor(Color.parseColor("#0099CC"));
                snackbar.show();
            }
        };
        startAnimation(hintImage);
    }

    @Override
    public void onBackPressed() {
        HttpUtil.client.cancelAllRequests(true);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void hint(View view) {
        if (status == STATUS.WAIT_RETRY) {
            hintImage.setImageResource(R.drawable.load);
            hintText.setText("正在加载");
            startAnimation(hintImage);
            status = STATUS.IN_LOAD;
            HttpUtil.getArticleContent(id, listener);
        }
    }

    public void startAnimation(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        LinearInterpolator lin = new LinearInterpolator();
        animation.setInterpolator(lin);
        view.startAnimation(animation);
    }

    public void stopAnimation(View view) {
        view.clearAnimation();
    }

}
