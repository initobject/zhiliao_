package com.czy.zhiliao.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.czy.zhiliao.Adapter.ArticleThemeContentAdapter;
import com.czy.zhiliao.Bean.ArticleTheme;
import com.czy.zhiliao.Listener.OnLoadThemeContentListener;
import com.czy.zhiliao.Net.HttpUtil;
import com.czy.zhiliao.R;

/**
 * Created by ZY on 2016/7/31.
 * 主题文章
 */
public class ThemeFragment extends BaseFragment {

    //文章列表
    private RecyclerView recyclerView;

    private ArticleThemeContentAdapter adapter;

    private OnLoadThemeContentListener listener;

    private int id;

    private boolean flag;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_article_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.articleList);
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        return view;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        String title = "享受阅读的乐趣";
        if (bundle != null) {
            id = bundle.getInt("ID", 1);
            title = bundle.getString("Title");
        }
        if (getRootActivity().getSupportActionBar() != null) {
            getRootActivity().getSupportActionBar().setTitle(title);
        }
        adapter = new ArticleThemeContentAdapter(mActivity);
        recyclerView.setAdapter(adapter);
        listener = new OnLoadThemeContentListener() {
            @Override
            public void onSuccess(ArticleTheme articleTheme) {
                adapter.setData(articleTheme);
                adapter.notifyDataSetChanged();
                stopRefresh();
                if (!flag) {
                    flag = true;
                } else {
                    hint(recyclerView, "已经是最新文章啦", Color.parseColor("#0099CC"));
                }
            }

            @Override
            public void onFailure() {
                if (mActivity != null) {
                    hint(recyclerView, "好奇怪，文章加载不来", Color.parseColor("#0099CC"));
                }
                stopRefresh();
            }
        };
        refreshData();
    }

    public void stopRefresh() {
        if (getRootActivity() != null) {
            getRootActivity().setRefresh(false);
        }
    }

    public void refreshData() {
        if (!HttpUtil.isNetworkConnected(mActivity)) {
            stopRefresh();
            hint(recyclerView, "似乎没有连接网络？", Color.parseColor("#0099CC"));
            return;
        }
        HttpUtil.getArticleListByTheme(id, listener);
    }
}
