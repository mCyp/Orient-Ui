package com.orient.me.widget.rv.rv;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollRecyclerView extends RecyclerView {
    public NoScrollRecyclerView(@NonNull Context context) {
        super(context);
    }

    public NoScrollRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        switch (e.getAction()){
            case MotionEvent.ACTION_MOVE:{
                return false;
            }
        }
        return super.onInterceptTouchEvent(e);
    }
}
