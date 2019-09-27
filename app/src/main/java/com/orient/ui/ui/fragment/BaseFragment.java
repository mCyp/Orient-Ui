package com.orient.ui.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author WangJie
 * Created on 2019/7/25.
 */
public abstract class BaseFragment extends Fragment {

    private Unbinder unbinder;
    private View mRoot;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null) {
            View root = inflater.inflate(getLayoutId(), container, false);
            initWidget(root);
            mRoot = root;
        } else {
            if (mRoot.getParent() != null) {
                ((ViewGroup) (mRoot.getParent())).removeView(mRoot);
            }
        }
        return mRoot;
    }

    /**
     * 得到资源文件
     */
    protected abstract int getLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget(View root) {
        unbinder = ButterKnife.bind(this, root);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
    }

    protected void showToast(String str) {
        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
    }

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unbinder.unbind();
    }
}
