package com.orient.ui.ui.activity.table;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.orient.me.widget.rv.adapter.TableAdapter;
import com.orient.me.widget.rv.layoutmanager.table.TableLayoutManager;
import com.orient.me.widget.rv.rv.TableRecyclerView;
import com.orient.ui.R;
import com.orient.ui.data.table.TableCell;
import com.orient.ui.ui.activity.BaseActivity;
import com.orient.ui.ui.adapter.CommonTableAdapter;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

public class TableActivity extends BaseActivity {

    @BindView(R.id.rv_table)
    TableRecyclerView tableRv;

    private TableAdapter<TableCell> mAdapter;

    public static void show(Context context){
        Intent intent = new Intent(context,TableActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.table_activity;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        TableLayoutManager tll = new TableLayoutManager(TableLayoutManager.MODE_A,4,8);
        tableRv.setLayoutManager(tll);
        mAdapter = new CommonTableAdapter<TableCell>() {
            @Override
            public ViewHolder<TableCell> onCreateViewHolder(View root, int viewType) {
                return new ViewHOlder(root);
            }

            @Override
            public int getItemLayout(TableCell tableCell, int position) {
                return R.layout.table_cell_recycler_item;
            }
        };
        tableRv.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        super.initData();

        List<TableCell> cells = new LinkedList<>();
        cells.add(new TableCell("a","1",1,0,0,4,1));
        /*cells.add(new TableCell("b","1",1,0,1,1,1));
        cells.add(new TableCell("c","1",1,0,2,1,1));
        cells.add(new TableCell("d","1",1,0,3,1,1));*/
        cells.add(new TableCell("e","1",1,1,0,2,2));
        //cells.add(new TableCell("f","1",1,1,1,1,1));
        cells.add(new TableCell("g","1",1,1,2,1,1));
        cells.add(new TableCell("h","1",1,1,3,1,3));
        //cells.add(new TableCell("j","1",1,2,0,1,1));
        //cells.add(new TableCell("k","1",1,2,1,1,1));
        cells.add(new TableCell("l","1",1,2,2,1,1));
        //cells.add(new TableCell("m","1",1,2,3,1,1));
        cells.add(new TableCell("n","1",1,3,0,1,1));
        cells.add(new TableCell("o","1",1,3,1,1,1));
        cells.add(new TableCell("p","1",1,3,2,1,1));
        //cells.add(new TableCell("q","1",1,3,3,1,1));
        cells.add(new TableCell("r","1",1,4,0,4,2));
       /* cells.add(new TableCell("s","1",1,4,1,1,1));
        cells.add(new TableCell("t","1",1,4,2,1,1));
        cells.add(new TableCell("u","1",1,4,3,1,1));
        cells.add(new TableCell("v","1",1,5,0,1,1));
        cells.add(new TableCell("w","1",1,5,1,1,1));
        cells.add(new TableCell("x","1",1,5,2,1,1));
        cells.add(new TableCell("y","1",1,5,3,1,1));*/
        cells.add(new TableCell("z","1",1,6,0,4,1));
        cells.add(new TableCell("a","1",1,7,0,4,1));
        cells.add(new TableCell("b","1",1,8,0,4,1));
        cells.add(new TableCell("c","1",1,9,0,4,1));
        cells.add(new TableCell("d","1",1,10,0,4,1));
        mAdapter.addAllData(cells);

    }

    class ViewHOlder extends CommonTableAdapter.ViewHolder<TableCell>{

        @BindView(R.id.tv_name)
        TextView mNameTv;

        public ViewHOlder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(TableCell tableCell) {
            mNameTv.setText(tableCell.getName());
        }
    }
}
