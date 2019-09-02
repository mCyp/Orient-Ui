package com.orient.ui.ui.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());
        initWidget();
        initData();
    }

    protected abstract @LayoutRes int getLayoutId();

    protected void initWidget(){
        //unbinder = ButterKnife.bind(this);
        ButterKnife.bind(this);
    }

    protected void initData(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(unbinder != null && unbinder != Unbinder.EMPTY){
            unbinder.unbind();
            unbinder = null;
        }
    }
}
