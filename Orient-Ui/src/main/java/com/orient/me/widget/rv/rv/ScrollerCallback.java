package com.orient.me.widget.rv.rv;

/**
 * 滑动回调
 */
public interface ScrollerCallback {
    /**
     * 是否可以纵向滑动
     */
    boolean canScrollVertical();

    /**
     * 是否可以横向滚动
     */
    boolean canScrollHorizontal();
}
