package com.czy.zhiliao.Listener;


import com.czy.zhiliao.Bean.ArticleContent;

/**
 * Created by ZY on 2016/7/26.
 * 加载文章内容事件监听
 */
public interface OnLoadArticleContentListener {

    void onSuccess(ArticleContent content);

    void onFailure();

}
