package com.orient.ui.ui.activity.placeholder;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.widget.Toolbar;

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
    @BindView(R.id.sv_content)
    StatusView mStatusView;

    public static void show(Context context){
        Intent intent = new Intent(context,PlaceHolderActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.place_holder_activity;
    }

    // initWidget方法 在 Activity#onCreate声明周期中
    @Override
    protected void initWidget() {
        super.initWidget();

        // 视图绑定
        mStatusView.bind(mContent);
        // 初始为 空数据 状态
        mStatusView.triggerEmpty();

        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        mToolbar.inflateMenu(R.menu.place_holder_menu);
        mToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
                case R.id.menu_loading:
                    // 切换为 加载 状态
                    mStatusView.triggerLoading();
                    return true;
                case R.id.menu_show_data:
                    // 切换为 显示数据 的状态
                    mStatusView.triggerOk();
                    return true;
                case R.id.menu_error:
                    // 切换为 显示错误 的状态
                    mStatusView.triggerNetError();
                    //mEmptyView.triggerError();
                    return true;
                case R.id.menu_null:
                    // 切换为错误状态
                    mStatusView.triggerEmpty();
                    // 需要条件的时候可以使用 mEmptyView.triggerOkOrEmpty(boolean isOk);
                    return true;
                default:
                    return false;
            }
        });

    }

}
