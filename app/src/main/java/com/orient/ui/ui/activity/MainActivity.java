package com.orient.ui.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.orient.ui.R;
import com.orient.ui.ui.adapter.RecyclerAdapter;
import com.orient.ui.widget.GridDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    /*@BindView(R.id.rv_main)
    RecyclerView mRecyclerView;*/

    private RecyclerView mRecyclerView;

    private RecyclerAdapter<MainItem> mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.main_activity;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mRecyclerView =findViewById(R.id.rv_main);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<MainItem>(createItem(), null) {
            @Override
            public ViewHolder<MainItem> onCreateViewHolder(View root, int viewType) {
                return new MainActivity.ViewHolder(root);
            }

            @Override
            public int getItemLayout(MainItem mainItem, int position) {
                return R.layout.main_recycle_item;
            }
        });

        mRecyclerView.addItemDecoration(new GridDividerItemDecoration(this,3));

        mAdapter.setAdapterListener(new RecyclerAdapter.AdapterListenerImpl<MainItem>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder<MainItem> holder, MainItem mainItem) {
                super.onItemClick(holder, mainItem);

                switch (mainItem.name) {
                    case "两侧布局":
                        TimeLineActivity.show(MainActivity.this);
                        break;
                    case "网格首页":
                        GridPageActivity.show(MainActivity.this);
                        break;
                }
            }
        });
    }

    private List<MainItem> createItem() {
        List<MainItem> items = new ArrayList<>();
        items.add(new MainItem("两侧布局", -1));
        items.add(new MainItem("网格首页", -1));
        return items;
    }

    class MainItem {
        String name;
        int source = -1;

        public MainItem(String name, int source) {
            this.name = name;
            this.source = source;
        }
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<MainItem> {

        @BindView(R.id.tv_content)
        TextView mContent;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(MainItem mainItem) {
            mContent.setText(mainItem.name);
        }
    }


}
