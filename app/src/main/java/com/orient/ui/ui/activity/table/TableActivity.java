package com.orient.ui.ui.activity.table;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.orient.me.widget.rv.adapter.GridAdapter;
import com.orient.me.widget.rv.layoutmanager.table.TableLayoutManager;
import com.orient.me.widget.rv.rv.TableRecyclerView;
import com.orient.ui.R;
import com.orient.ui.data.table.TableCell;
import com.orient.ui.ui.activity.BaseActivity;
import com.orient.ui.ui.adapter.CommonGridAdapter;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

public class TableActivity extends BaseActivity {

    @BindView(R.id.rv_table)
    TableRecyclerView tableRv;

    private GridAdapter<TableCell> mAdapter;

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

        TableLayoutManager tll = new TableLayoutManager(TableLayoutManager.MODE_B,120,200);
        tableRv.setLayoutManager(tll);
        mAdapter = new CommonGridAdapter<TableCell>() {
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
        cells.add(new TableCell("1","1",1,0,0,2,2));
        //cells.add(new TableCell("2","1",1,0,1,1,1));
        cells.add(new TableCell("3","1",1,0,2,1,1));
        cells.add(new TableCell("4","1",1,0,3,1,2));
        cells.add(new TableCell("5","1",1,0,4,1,1));
        cells.add(new TableCell("6","1",1,0,5,1,1));

        //cells.add(new TableCell("7","1",1,1,0,1,1));
        //cells.add(new TableCell("8","1",1,1,1,1,1));
        cells.add(new TableCell("9","1",1,1,2,1,1));
        cells.add(new TableCell("11","1",1,1,4,1,1));
        cells.add(new TableCell("12","1",1,1,5,1,1));

        cells.add(new TableCell("13","1",1,2,0,4,1));
        cells.add(new TableCell("14","1",1,2,4,1,1));
        cells.add(new TableCell("15","1",1,2,5,1,1));

        cells.add(new TableCell("19","1",1,3,0,1,1));
        cells.add(new TableCell("20","1",1,3,1,1,1));
        cells.add(new TableCell("21","1",1,3,2,1,1));
        cells.add(new TableCell("22","1",1,3,3,1,1));
        cells.add(new TableCell("23","1",1,3,4,1,1));
        cells.add(new TableCell("24","1",1,3,5,1,1));

        cells.add(new TableCell("25","1",1,4,0,1,1));
        cells.add(new TableCell("26","1",1,4,1,1,1));
        cells.add(new TableCell("27","1",1,4,2,1,1));
        cells.add(new TableCell("28","1",1,4,3,1,1));
        cells.add(new TableCell("29","1",1,4,4,1,1));
        cells.add(new TableCell("30","1",1,4,5,1,1));

        cells.add(new TableCell("31","1",1,5,0,1,1));
        cells.add(new TableCell("32","1",1,5,1,1,1));
        cells.add(new TableCell("33","1",1,5,2,1,1));
        cells.add(new TableCell("34","1",1,5,3,1,1));
        cells.add(new TableCell("35","1",1,5,4,1,1));
        cells.add(new TableCell("36","1",1,5,5,1,1));

        cells.add(new TableCell("37","1",1,6,0,1,2));
        cells.add(new TableCell("38","1",1,6,1,1,2));
        cells.add(new TableCell("39","1",1,6,2,1,2));
        cells.add(new TableCell("40","1",1,6,3,1,2));
        cells.add(new TableCell("41","1",1,6,4,1,2));
        cells.add(new TableCell("42","1",1,6,5,1,2));

        cells.add(new TableCell("43","1",1,8,0,1,1));
        cells.add(new TableCell("44","1",1,8,1,1,1));
        cells.add(new TableCell("45","1",1,8,2,1,1));
        cells.add(new TableCell("46","1",1,8,3,1,1));
        cells.add(new TableCell("47","1",1,8,4,1,1));
        cells.add(new TableCell("48","1",1,8,5,1,1));

        cells.add(new TableCell("1","1",1,9,0,2,2));
        cells.add(new TableCell("3","1",1,9,2,1,1));
        cells.add(new TableCell("4","1",1,9,3,1,2));
        cells.add(new TableCell("5","1",1,9,4,1,1));
        cells.add(new TableCell("6","1",1,9,5,1,1));

        cells.add(new TableCell("9","1",1,10,2,1,1));
        cells.add(new TableCell("11","1",1,10,4,1,1));
        cells.add(new TableCell("12","1",1,10,5,1,1));

        cells.add(new TableCell("13","1",1,11,0,4,1));
        cells.add(new TableCell("14","1",1,11,4,1,1));
        cells.add(new TableCell("15","1",1,11,5,1,1));



        /*cells.add(new TableCell("a","1",1,0,0,1,4));
        cells.add(new TableCell("e","1",1,0,1,2,2));
        cells.add(new TableCell("g","1",1,2,1,1,1));
        cells.add(new TableCell("h","1",1,3,1,3,1));

        cells.add(new TableCell("l","1",1,2,2,1,1));

        cells.add(new TableCell("n","1",1,0,3,1,1));
        cells.add(new TableCell("o","1",1,1,3,1,1));
        cells.add(new TableCell("p","1",1,2,3,1,1));

        cells.add(new TableCell("r","1",1,0,4,2,4));

        cells.add(new TableCell("z","1",1,0,6,4,1));
        cells.add(new TableCell("a","1",1,1,6,4,1));
        cells.add(new TableCell("b","1",1,2,6,4,1));
        cells.add(new TableCell("c","1",1,3,6,4,1));*/



        /*cells.add(new TableCell("a","1",1,0,0,4,1));
        cells.add(new TableCell("e","1",1,1,0,2,2));
        cells.add(new TableCell("g","1",1,1,2,1,1));
        cells.add(new TableCell("h","1",1,1,3,1,3));

        cells.add(new TableCell("l","1",1,2,2,1,1));

        cells.add(new TableCell("n","1",1,3,0,1,1));
        cells.add(new TableCell("o","1",1,3,1,1,1));
        cells.add(new TableCell("p","1",1,3,2,1,1));

        cells.add(new TableCell("r","1",1,4,0,4,2));

        cells.add(new TableCell("z","1",1,6,0,1,4));
        cells.add(new TableCell("a","1",1,6,1,1,4));
        cells.add(new TableCell("b","1",1,6,2,1,4));
        cells.add(new TableCell("c","1",1,6,3,1,4));

        cells.add(new TableCell("d","1",1,10,0,4,1));
        cells.add(new TableCell("e","1",1,11,0,4,1));
        cells.add(new TableCell("f","1",1,12,0,4,1));
        cells.add(new TableCell("g","1",1,13,0,4,1));
        cells.add(new TableCell("h","1",1,14,0,4,1));

        cells.add(new TableCell("a","1",1,15,0,4,1));

        cells.add(new TableCell("e","1",1,16,0,2,2));
        cells.add(new TableCell("g","1",1,16,2,1,1));
        cells.add(new TableCell("h","1",1,16,3,1,3));

        cells.add(new TableCell("l","1",1,17,2,1,1));

        cells.add(new TableCell("n","1",1,18,0,1,1));
        cells.add(new TableCell("o","1",1,18,1,1,1));
        cells.add(new TableCell("p","1",1,18,2,1,1));
        cells.add(new TableCell("f","1",1,19,0,4,1));
        cells.add(new TableCell("g","1",1,20,0,4,1));
        cells.add(new TableCell("h","1",1,21,0,4,1));
        cells.add(new TableCell("h","1",1,22,0,4,1));
        cells.add(new TableCell("1","1",1,23,0,4,1));
        cells.add(new TableCell("2","1",1,24,0,4,1));
        cells.add(new TableCell("11","1",1,25,0,4,1));
        cells.add(new TableCell("21","1",1,26,0,4,1));*/
        mAdapter.addAllData(cells);

    }

    class ViewHOlder extends CommonGridAdapter.ViewHolder<TableCell>{

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
