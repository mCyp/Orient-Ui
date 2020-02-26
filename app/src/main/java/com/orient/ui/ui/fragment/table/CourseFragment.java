package com.orient.ui.ui.fragment.table;


import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.orient.me.widget.rv.adapter.BaseAdapter;
import com.orient.me.widget.rv.adapter.TableAdapter;
import com.orient.me.widget.rv.adapter.TableView;
import com.orient.me.widget.rv.layoutmanager.table.TableLayoutManager;
import com.orient.ui.R;
import com.orient.ui.ui.activity.table.TableCell;
import com.orient.ui.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

/**
 * 课程表Fragment
 * 使用表格
 */
public class CourseFragment extends BaseFragment {

    String[] color = new String[]{"#E91E63","#673AB7","#3F51B5","#009688","#8BC34A","#FF9800"};


    @BindView(R.id.tb)
    TableView mTable;

    private TableAdapter<TableCell> mAdapter;

    public CourseFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_course;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);


        mTable.setModeAndValue(TableLayoutManager.MODE_A, 6, 8);
        mTable.setAdapter(mAdapter = new TableAdapter<TableCell>(new ArrayList<>()) {
            @Override
            public int getItemLayout(TableCell tableCell, int pos) {
                int row = tableCell.getRow();
                int col = tableCell.getCol();
                if (row == 0) {
                    return R.layout.course_cell_title_item;
                } else if (col == 0) {
                    return R.layout.course_cell_left_item;
                } else {
                    return R.layout.course_cell_content_item;
                }
            }

            @Override
            public BaseAdapter.ViewHolder<TableCell> onCreateViewHolder(View root, int itemType) {
                switch (itemType) {
                    case R.layout.course_cell_left_item:
                    case R.layout.course_cell_title_item:
                        return new TitleHolder(root);
                    default:
                        return new TableHolder(root);
                }
            }
        });

        mTable.post(() -> mTable.reMeasure());
    }

    @Override
    protected void initData() {
        super.initData();
        List<TableCell> cells = new LinkedList<>();

        cells.add(new TableCell("五月", "May", 1, 0, 0, 1, 1));
        cells.add(new TableCell("周一", "21", 1, 0, 1, 1, 1));
        cells.add(new TableCell("周二", "22", 1, 0, 2, 1, 1));
        cells.add(new TableCell("周三", "23", 1, 0, 3, 1, 1));
        cells.add(new TableCell("周四", "24", 1, 0, 4, 1, 1));
        cells.add(new TableCell("周五", "25", 1, 0, 5, 1, 1));
        cells.add(new TableCell("周六", "26", 1, 0, 6, 1, 1));

        cells.add(new TableCell("1", "8.30", 1, 1, 0, 1, 2));
        cells.add(new TableCell("高等数学", "1", 2, 1, 1, 1, 2));
        cells.add(new TableCell("计算机科学技术", "1", 2, 1, 2, 1, 2));
        cells.add(new TableCell("Java", "1", 6, 1, 3, 1, 2));
        cells.add(new TableCell("", "1", 1, 1, 4, 1, 2));
        cells.add(new TableCell("体育", "1", 3, 1, 5, 1, 2));
        cells.add(new TableCell("", "1", 1, 1, 6, 1, 2));

        cells.add(new TableCell("2", "9.20", 1, 2, 0, 1, 1));


        cells.add(new TableCell("3", "10.15", 1, 3, 0, 1, 1));
        cells.add(new TableCell("C++", "1", 4, 3, 1, 1, 2));
        cells.add(new TableCell("英语", "1", 4, 3, 2, 1, 2));
        cells.add(new TableCell("高度数学", "1", 4, 3, 3, 1, 2));
        cells.add(new TableCell("", "1", 1, 3, 4, 1, 1));
        cells.add(new TableCell("政治", "1", 1, 3, 5, 1, 2));
        cells.add(new TableCell("", "1", 1, 3, 6, 1, 2));

        cells.add(new TableCell("4", "11.05", 1, 4, 0, 1, 1));
        cells.add(new TableCell("开会", "1", 1, 3, 4, 1, 1));


        cells.add(new TableCell("5", "13.00", 1, 5, 0, 1, 1));
        cells.add(new TableCell("开会", "1", 5, 5, 1, 1, 1));
        cells.add(new TableCell("政治", "1", 5, 5, 2, 1, 2));
        cells.add(new TableCell("C++", "1", 5, 5, 3, 1, 2));
        cells.add(new TableCell("英语", "1", 5, 5, 4, 1, 2));
        cells.add(new TableCell("游戏", "1", 5, 5, 5, 1, 2));
        cells.add(new TableCell("", "1", 5, 5, 6, 1, 2));

        cells.add(new TableCell("6", "13.50", 1, 6, 0, 1, 1));
        cells.add(new TableCell("", "1", 1, 6, 1, 1, 1));

        cells.add(new TableCell("7", "14.45", 1, 7, 0, 1, 1));
        cells.add(new TableCell("", "1", 1, 7, 1, 1, 2));
        cells.add(new TableCell("C++", "1", 1, 7, 2, 1, 2));
        cells.add(new TableCell("Java", "1", 1, 7, 3, 1, 2));
        cells.add(new TableCell("", "1", 1, 7, 4, 1, 2));
        cells.add(new TableCell("", "1", 1, 7, 5, 1, 2));
        cells.add(new TableCell("", "1", 1, 7, 6, 1, 2));

        cells.add(new TableCell("8", "15.35", 1, 8, 0, 1, 1));

        mAdapter.addList(cells);

    }

    class TitleHolder extends BaseAdapter.ViewHolder<TableCell> {
        TextView mFirst;
        TextView mSecond;

        public TitleHolder(View itemView) {
            super(itemView);

            mFirst = itemView.findViewById(R.id.tv_name);
            mSecond = itemView.findViewById(R.id.tv_value);
        }

        @Override
        protected void onBind(TableCell tableCell) {
            mFirst.setText(tableCell.getName());
            mSecond.setText(tableCell.getValue());
        }
    }

    class TableHolder extends BaseAdapter.ViewHolder<TableCell> {

        TextView mNameTv;

        public TableHolder(View itemView) {
            super(itemView);
            mNameTv = itemView.findViewById(R.id.tv_name);
        }

        @Override
        protected void onBind(TableCell tableCell) {
            if (TextUtils.isEmpty(tableCell.getName())) {
                mNameTv.setVisibility(View.GONE);
            } else {
                mNameTv.setVisibility(View.VISIBLE);
                mNameTv.setText(tableCell.getName());
                GradientDrawable myGrad = (GradientDrawable)mNameTv.getBackground();
                myGrad.setColor(Color.parseColor(color[tableCell.getType()-1]));
            }
        }
    }
}
