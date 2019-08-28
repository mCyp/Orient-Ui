package com.orient.ui.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.orient.ui.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 基础的RecyclerAdapter
 *
 * Author WangJie
 * Created on 2018/8/27.
 */
@SuppressWarnings("ALL")
public abstract class RecyclerAdapter<Data> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>>
    implements View.OnClickListener,View.OnLongClickListener{

    // 数据集合
    private List<Data> mDataList;
    // 监听器
    private AdapterListener<Data> adapterListener;

    public RecyclerAdapter() {
        this(null);
    }

    public RecyclerAdapter(AdapterListener<Data> adapterListener) {
        this(new ArrayList<Data>(),adapterListener);
    }

    public RecyclerAdapter(List<Data> mDataList, AdapterListener<Data> adapterListener) {
        this.mDataList = mDataList;
        this.adapterListener = adapterListener;
    }

    @Override
    public ViewHolder<Data> onCreateViewHolder(ViewGroup parent, int viewType) {
        // 创建ViewHolder
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(viewType,parent,false);
        ViewHolder<Data> viewHolder = onCreateViewHolder(root,viewType);

        // 基础的操作
        root.setTag(R.id.recycler_view_holder,viewHolder);
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);
        viewHolder.unbinder = ButterKnife.bind(viewHolder,root);

        return viewHolder;
    }

    /**
     *  实际的创建ViewHolder的方法
     */
    public abstract ViewHolder<Data> onCreateViewHolder(View root, int viewType);

    @Override
    public void onBindViewHolder(ViewHolder<Data> holder, int position) {
        // 设置不能进行重复绘制
        //holder.setIsRecyclable(false);
        // TODO 对多数据进行测试，查看哪里出了问题
        // 绑定数据
        Data data = mDataList.get(position);
        holder.bind(data);
    }

    @Override
    public int getItemViewType(int position) {
        Data data = mDataList.get(position);
        return getItemLayout(data,position);
    }

    /**
     * 得到子布局的ID 适合多种子布局的情况下使用
     */
    public abstract int getItemLayout(Data data,int position);

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onClick(View v) {
        ViewHolder<Data> holder = (ViewHolder<Data>) v.getTag(R.id.recycler_view_holder);
        if(holder != null){
            if(adapterListener == null)
                return;
            int pos = holder.getAdapterPosition();
            adapterListener.onItemClick(holder,mDataList.get(pos));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        ViewHolder<Data> holder = (ViewHolder<Data>) v.getTag(R.id.recycler_view_holder);
        if(holder != null){
            if(adapterListener != null){
                int pos = holder.getAdapterPosition();
                adapterListener.onItemLongClick(holder,mDataList.get(pos));
                return true;
            }
        }
        return false;
    }

    /**
     *  得到数据
     */
    public List<Data> getItems() {
        return mDataList;
    }

    /**
     * 新增一个数据
     */
    public void add(Data data){
        mDataList.add(data);
        notifyItemInserted(mDataList.size() -1 );
    }

    /**
     * 新增所有的数据
     */
    public void addAllData(Collection<Data> datas){
        int start = mDataList.size();
        mDataList.addAll(datas);
        notifyItemRangeChanged(start,mDataList.size()-1);
    }

    /**
     *  新增所有的数组数据
     */
    public void addAllData(Data... datas){
        int start = mDataList.size();
        mDataList.addAll(Arrays.asList(datas));
        notifyItemRangeChanged(start,mDataList.size()-1);
    }

    /**
     * 删除所有的数据
     */
    public void remove(){
        mDataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 替换数据
     */
    public void replace(Collection<Data> datas){
        mDataList.clear();
        mDataList.addAll(datas);
        notifyDataSetChanged();
    }

    public void setAdapterListener(AdapterListener<Data> listener){
        this.adapterListener = listener;
    }


    /*
     * 适配器的监听器
     */
    public interface AdapterListener<Data>{
        // 单击的时候
        void onItemClick(ViewHolder<Data> holder, Data data);
        // 长按的时候
        void onItemLongClick(ViewHolder<Data> holder, Data data);
    }

    /**
     *  自定义的ViewHolder
     */
    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder{
        public Unbinder unbinder;
        protected Data mData;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(Data data){
            mData = data;
            onBind(data);
        }

        /**
         * 实现数据的绑定
         */
        protected abstract void onBind(Data data);
    }

    public abstract static class AdapterListenerImpl<Data> implements AdapterListener<Data>{
        @Override
        public void onItemClick(ViewHolder<Data> holder,Data data) {

        }

        @Override
        public void onItemLongClick(ViewHolder<Data> holder,Data data) {

        }
    }
}
