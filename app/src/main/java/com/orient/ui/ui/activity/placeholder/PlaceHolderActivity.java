package com.orient.ui.ui.activity.placeholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.orient.me.widget.placeholder.StatusView;
import com.orient.ui.R;
import com.orient.ui.ui.activity.BaseActivity;

import butterknife.BindView;

public class PlaceHolderActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_name)
    TextView mContent;
    @BindView(R.id.et_content)
    StatusView mStatusView;

    public static void show(Context context){
        Intent intent = new Intent(context,PlaceHolderActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.place_holder_activity;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        mStatusView.triggerEmpty();
        mToolbar.inflateMenu(R.menu.place_holder_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_loading:
                        mStatusView.triggerLoading();
                        return true;
                    case R.id.menu_show_data:
                        mStatusView.triggerOk();
                        return true;
                    case R.id.menu_error:
                        mStatusView.triggerNetError();
                        //mEmptyView.triggerError();
                        return true;
                    case R.id.menu_null:
                        mStatusView.triggerEmpty();
                        // 需要条件的时候可以使用 mEmptyView.triggerOkOrEmpty(boolean isOk);
                        return true;
                    default:
                        return false;
                }
            }
        });

        mStatusView.bind(mContent);
    }

}
