package com.orient.ui.ui.activity.doubleside;

import android.content.Context;
import android.content.Intent;

import com.orient.ui.R;
import com.orient.ui.ui.activity.BaseActivity;

/**
 * 两侧布局的最佳实战
 * LayoutManager -> DoubleSideLayoutManager
 */
public class DoubleSideLayoutActivity extends BaseActivity {


    public static void show(Context context) {
        Intent intent = new Intent(context, DoubleSideLayoutActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.two_side_layout_activity;
    }

}
