package com.orient.ui.ui.activity;

import android.content.Context;
import android.content.Intent;
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
import com.orient.me.widget.rv.itemdocration.GridItemDecoration;
import com.orient.ui.R;
import com.orient.ui.data.GridItem;
import com.orient.ui.ui.adapter.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class GridPageActivity extends AppCompatActivity {

    @BindView(R.id.rv_content)
    RecyclerView mRecyclerView;

    private List<GridItem> values;
    private GridItemDecoration itemDecoration;
    private RecyclerAdapter<GridItem> mAdapter;

    public static void show(Context context){
        Intent intent = new Intent(context, GridPageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gird_page_activity);

        initWidget();
    }

    private void initWidget() {
        mRecyclerView = findViewById(R.id.rv_content);

        GridLayoutManager gll = new GridLayoutManager(this, 3);
        gll.setSpanSizeLookup(new SpecialSpanSizeLookup());
        mRecyclerView.setLayoutManager(gll);
        values = initData();
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<GridItem>(values,null) {
            @Override
            public ViewHolder<GridItem> onCreateViewHolder(View root, int viewType) {
                switch (viewType) {
                    case R.layout.grid_small_recycle_item:
                        return new SmallHolder(root);
                    case R.layout.grid_normal_recycle_item:
                        return new NormalHolder(root);
                    case R.layout.grid_special_recycle_item:
                        return new SpecialHolder(root);
                    default:
                        return null;
                }

            }

            @Override
            public int getItemLayout(GridItem gridItem, int position) {
                switch (gridItem.getType()) {
                    case GridItem.TYPE_SMALL:
                        return R.layout.grid_small_recycle_item;
                    case GridItem.TYPE_NORMAL:
                        return R.layout.grid_normal_recycle_item;
                    case GridItem.TYPE_SPECIAL:
                        return R.layout.grid_special_recycle_item;
                }
                return 0;
            }
        });

        mAdapter.setAdapterListener(new RecyclerAdapter.AdapterListener<GridItem>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder<GridItem> holder, GridItem gridItem) {
                // 测试的代码
                int pos = holder.getAdapterPosition();
                if(holder.getAdapterPosition() == mAdapter.getItemCount()-1){
                    List<GridItem> items = initData();
                    items.get(items.size() - 1).setName("hhhahhahaa");
                    mAdapter.replace(items);
                    itemDecoration.replace(items,pos);
                    values = items;
                }
            }

            @Override
            public void onItemLongClick(RecyclerAdapter.ViewHolder<GridItem> holder, GridItem gridItem) {

            }
        });

        itemDecoration = new GridItemDecoration.Builder(this,values, 3)
                .setTitleTextColor(Color.parseColor("#4e5864"))
                //.setTitleBgColor(Color.parseColor("#008577"))
                .setTitleFontSize(22)
                .setTitleHeight(52)
                .build();
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    private List<GridItem> initData() {
        List<GridItem> values = new ArrayList<>();
        values.add(new GridItem("我很忙", "", R.drawable.grid_head_1,"最近常听",1,GridItem.TYPE_SMALL));
        values.add(new GridItem("治愈：有些歌比闺蜜更懂你", "", R.drawable.grid_head_2,"最近常听",1,GridItem.TYPE_SMALL));
        values.add(new GridItem("「华语」90后的青春纪念手册", "", R.drawable.grid_head_3,"最近常听",1,GridItem.TYPE_SMALL));

        values.add(new GridItem("流行创作女神你霉，泰勒斯威夫特的创作历程", "", R.drawable.grid_special_2
                ,"更多为你推荐",3,GridItem.TYPE_SPECIAL));
        values.add(new GridItem("行走的CD写给别人的歌", "给「跟我走吧」几分，试试这些", R.drawable.grid_normal_1
                ,"更多为你推荐",3,GridItem.TYPE_NORMAL));
        values.add(new GridItem("爱情里的酸甜苦辣，让人捉摸不透", "听完「靠近一点点」，他们等你翻牌", R.drawable.grid_normal_2
                ,"更多为你推荐",3,GridItem.TYPE_NORMAL));
        values.add(new GridItem("关于喜欢你这件事，我都写在了歌里", "「好想你」听罢，听它们吧", R.drawable.grid_normal_3
                ,"更多为你推荐",3,GridItem.TYPE_NORMAL));
        values.add(new GridItem("周杰伦暖心混剪，短短几分钟是多少人的青春", "", R.drawable.grid_special_1
                ,"更多为你推荐",3,GridItem.TYPE_SPECIAL));
        values.add(new GridItem("我好想和你一起听雨滴", "给「发如雪」几分，那这些呢", R.drawable.grid_normal_4
                ,"更多为你推荐",3,GridItem.TYPE_NORMAL));
        values.add(new GridItem("油管周杰伦热门单曲Top20", "「周杰伦」的这些哥，你听了吗", R.drawable.grid_normal_5
                ,"更多为你推荐",3,GridItem.TYPE_NORMAL));

        return values;

    }

    class SpecialSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

        @Override
        public int getSpanSize(int i) {
            GridItem gridItem = values.get(i);
            return gridItem.getSpanSize();
        }
    }

    class SmallHolder extends RecyclerAdapter.ViewHolder<GridItem> {

        @BindView(R.id.iv_head)
        ImageView mHead;
        @BindView(R.id.tv_content)
        TextView mName;

        SmallHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(GridItem gridItem) {
            Glide.with(GridPageActivity.this).load(gridItem.getSource())
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

        NormalHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(GridItem gridItem) {
            Glide.with(GridPageActivity.this).load(gridItem.getSource())
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

        SpecialHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(GridItem gridItem) {
            Glide.with(GridPageActivity.this).load(gridItem.getSource())
                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(20, 0)))
                    .into(mHead);
            mTitle.setText(gridItem.getName());
        }
    }


}
