package com.czy.zhiliao.Listener;


import com.czy.zhiliao.Bean.ArticleTheme;

/**
 * Created by ZY on 2016/7/31.
 * 加载特定主题下文章列表事件监听
 */
public interface OnLoadThemeContentListener {

    void onSuccess(ArticleTheme articleTheme);

    void onFailure();
}
