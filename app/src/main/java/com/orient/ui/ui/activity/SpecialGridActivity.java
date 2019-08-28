package com.orient.ui.ui.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.orient.me.rv.itemdocration.GridItemDecoration;
import com.orient.ui.R;
import com.orient.ui.data.GridItem;
import com.orient.ui.ui.adapter.GridAdapter;
import com.orient.ui.ui.adapter.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class SpecialGridActivity extends AppCompatActivity {

    private List<GridItem> values;
    private RecyclerView mRecyclerView;
    private GridItemDecoration itemDecoration;
    private RecyclerAdapter<GridItem> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_grid);

        initWidget();
    }

    private void initWidget() {
        mRecyclerView = findViewById(R.id.rv_content);

        GridLayoutManager gll = new GridLayoutManager(this, 3);
        gll.setSpanSizeLookup(new SpecialSpanSizeLookup());
        mRecyclerView.setLayoutManager(gll);
        initData();
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<GridItem>(values,null) {
            @Override
            public ViewHolder<GridItem> onCreateViewHolder(View root, int viewType) {
                switch (viewType) {
                    case R.layout.small_grid_recycle_item:
                        return new SmallHolder(root);
                    case R.layout.normal_grid_recycle_item:
                        return new NormalHolder(root);
                    case R.layout.special_grid_recycle_item:
                        return new SpecialHolder(root);
                    default:
                        return null;
                }

            }

            @Override
            public int getItemLayout(GridItem gridItem, int position) {
                switch (gridItem.getType()) {
                    case GridItem.TYPE_SMALL:
                        return R.layout.small_grid_recycle_item;
                    case GridItem.TYPE_NORMAL:
                        return R.layout.normal_grid_recycle_item;
                    case GridItem.TYPE_SPECIAL:
                        return R.layout.special_grid_recycle_item;
                }
                return 0;
            }
        });
        itemDecoration = new GridItemDecoration.Builder(values, 3)
                .setTitleTextColor(Color.parseColor("#4e5864"))
                .setTitleFontSize(24)
                .setTitleHeight(52)
                .build();
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    private void initData() {
        values = new ArrayList<>();
        values.add(new GridItem("我很忙", "", R.drawable.jay,"最近常听",1,GridItem.TYPE_SMALL));
        values.add(new GridItem("华语治愈：有些歌比闺蜜更懂你", "", R.drawable.head_2,"最近常听",1,GridItem.TYPE_SMALL));
        values.add(new GridItem("我很忙", "", R.drawable.jay,"最近常听",1,GridItem.TYPE_SMALL));
        values.add(new GridItem("校园：那些年，我爱过的那个少年", "听完[彩虹]，他们等你翻牌", R.drawable.normal_1
                ,"更多为你推荐",3,GridItem.TYPE_NORMAL));
        values.add(new GridItem("校园：那些年，我爱过的那个少年", "听完[彩虹]，他们等你翻牌", R.drawable.normal_1
                ,"更多为你推荐",3,GridItem.TYPE_NORMAL));
        values.add(new GridItem("taylor swift音乐历程", "", R.drawable.special_1
                ,"更多为你推荐",3,GridItem.TYPE_SPECIAL));
        values.add(new GridItem("校园：那些年，我爱过的那个少年", "听完[彩虹]，他们等你翻牌", R.drawable.normal_1
                ,"更多为你推荐",3,GridItem.TYPE_NORMAL));
        values.add(new GridItem("校园：那些年，我爱过的那个少年", "听完[彩虹]，他们等你翻牌", R.drawable.normal_1
                ,"更多为你推荐",3,GridItem.TYPE_NORMAL));
        values.add(new GridItem("taylor swift音乐历程", "", R.drawable.special_1
                ,"更多为你推荐",3,GridItem.TYPE_SPECIAL));
        values.add(new GridItem("校园：那些年，我爱过的那个少年", "听完[彩虹]，他们等你翻牌", R.drawable.normal_1
                ,"更多为你推荐",3,GridItem.TYPE_NORMAL));
        values.add(new GridItem("校园：那些年，我爱过的那个少年", "听完[彩虹]，他们等你翻牌", R.drawable.normal_1
                ,"更多为你推荐",3,GridItem.TYPE_NORMAL));
        values.add(new GridItem("taylor swift音乐历程", "", R.drawable.special_1
                ,"更多为你推荐",3,GridItem.TYPE_SPECIAL));


    }

    class SpecialSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

        @Override
        public int getSpanSize(int i) {
            // TODO 做自己处理的逻辑
            GridItem gridItem = values.get(i);
            return gridItem.getSpanSize();
        }
    }

    class SmallHolder extends RecyclerAdapter.ViewHolder<GridItem> {

        @BindView(R.id.iv_head)
        ImageView mHead;
        @BindView(R.id.tv_content)
        TextView mName;

        public SmallHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(GridItem gridItem) {
            Glide.with(SpecialGridActivity.this).load(gridItem.getSource())
                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(20, 0)))
                    .into(mHead);
            mName.setText(gridItem.getName());
        }
    }

    class NormalHolder extends RecyclerAdapter.ViewHolder<GridItem> {

        @BindView(R.id.iv_head)
        ImageView mHead;
        @BindView(R.id.tv_title)
        TextView mTitle;
        @BindView(R.id.tv_desc)
        TextView mDesc;

        public NormalHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(GridItem gridItem) {
            Glide.with(SpecialGridActivity.this).load(gridItem.getSource())
                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(20, 0)))
                    .into(mHead);
            mTitle.setText(gridItem.getName());
            mDesc.setText(gridItem.getOther());
        }
    }

    class SpecialHolder extends RecyclerAdapter.ViewHolder<GridItem> {

        @BindView(R.id.iv_head)
        ImageView mHead;
        @BindView(R.id.tv_title)
        TextView mTitle;

        public SpecialHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(GridItem gridItem) {
            Glide.with(SpecialGridActivity.this).load(gridItem.getSource())
                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(20, 0)))
                    .into(mHead);
            mTitle.setText(gridItem.getName());
        }
    }


}
