package com.orient.ui.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.orient.ui.R;
import com.orient.ui.data.GridItem;

import java.util.List;

/**
 * Author WangJie
 * Created on 2019/1/14.
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    private List<GridItem> values;
    private Context mContext;

    public GridAdapter(List<GridItem> values, Context context) {
        this.values = values;
        this.mContext = context;
    }

    public List<GridItem> getValues() {
        return values;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View root = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.small_grid_recycle_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final String s = values.get(i).getName();
        viewHolder.content = viewHolder.itemView.findViewById(R.id.txt_content);
        viewHolder.content.setText(s);
        viewHolder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
