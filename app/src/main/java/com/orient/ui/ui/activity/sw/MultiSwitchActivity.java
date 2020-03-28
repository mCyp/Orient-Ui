package com.orient.ui.ui.activity.sw;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.orient.me.widget.sw.MultiSwitch;
import com.orient.me.widget.sw.MultiSwitchListener;
import com.orient.ui.R;
import com.orient.ui.ui.activity.BaseActivity;

import butterknife.BindView;

public class MultiSwitchActivity extends BaseActivity {

    public static final String TAG = "MultiSwitchActivity";

    @BindView(R.id.ms_head)
    MultiSwitch mHead;

    @BindView(R.id.ms_content)
    MultiSwitch mSwitch;

    @BindView(R.id.ms_icon)
    MultiSwitch mIconSwitch;

    @BindView(R.id.ms_weak)
    MultiSwitch mWeekSwitch;

    @BindView(R.id.ms_check)
    MultiSwitch mCheckSwitch;

    @BindView(R.id.fl_head)
    FrameLayout mHeadFl;

    @BindView(R.id.ll_content)
    LinearLayout mContentLL;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_multi_switch;
    }

    public static void show(Context context) {
        Intent intent = new Intent(context, MultiSwitchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        //mSwitch.setCanSelect(false);

        mHead.setItemsArray(new String[]{"Dark", "Light"});
        mHead.setMultiSwitchListener(new MultiSwitchListener() {
            @Override
            public void onPositionSelected(int pos) {
                if (pos == 0) {
                    setBgAlpha(0, 0);
                } else {
                    setBgAlpha(1, 0);
                }
            }

            @Override
            public void onPositionOffsetPercent(int pos, float percent) {
                setBgAlpha(pos, percent);
            }
        });

        mSwitch.setItemsArray(new String[]{"Android", "Ios", "Java"});
        mSwitch.setCurrentItem(2);
        mSwitch.setMultiSwitchListener(new MultiSwitchListener() {
            @Override
            public void onPositionSelected(int pos) {
                Log.e(TAG, "位置:" + pos);
            }

            @Override
            public void onPositionOffsetPercent(int pos, float percent) {
                Log.e(TAG, "位置:" + pos + ",百分比：" + percent);
            }
        });


        mIconSwitch.setIconArray(new int[]{R.drawable.grid_ic_play, R.drawable.ic_camera, R.drawable.common_ic_back});

        mWeekSwitch.setItemsArray(new String[]{"一", "二", "三", "四", "五", "六", "日"});

        mCheckSwitch.setItemsArray(new String[]{"是", "否"});
    }

    public void setBgAlpha(int pos, float percent) {
        if (pos == 0) {
            float alpha = 255 * (1 - percent);
            Drawable drawable = mContentLL.getBackground();
            drawable.setAlpha((int) alpha);
            mContentLL.setBackground(drawable);
        } else if (pos == 1) {
            float alpha = 255 * Math.abs(percent);
            Drawable drawable = mContentLL.getBackground();
            drawable.setAlpha((int) alpha);
            mContentLL.setBackground(drawable);
        }
    }
}
