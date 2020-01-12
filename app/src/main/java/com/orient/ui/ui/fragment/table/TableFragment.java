package com.orient.ui.ui.fragment.table;


import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
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
 * A simple {@link Fragment} subclass.
 */
public class TableFragment extends BaseFragment {

    int[] drawable = new int[]{
            R.drawable.ball_pf,
            R.drawable.ball_sg,
            R.drawable.ball_sf,
            R.drawable.ball_pg,
            R.drawable.ball_c
    };


    @BindView(R.id.tb)
    TableView mTable;

    private TableAdapter<TableCell> mAdapter;


    public TableFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_table;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);


        mTable.setModeAndValue(TableLayoutManager.MODE_A, 6, 8);
        mTable.setAdapter(mAdapter = new TableAdapter<TableCell>(new ArrayList<>()) {
            @Override
            public int getItemLayout(TableCell tableCell, int pos) {
                int row = tableCell.getRow();
                if (row == 0) {
                    return R.layout.table_cell_title_item;
                }
                switch (tableCell.getType()) {
                    case 2:
                        return R.layout.table_cell_img_item;
                    case 3:
                        return R.layout.table_cell_date_item;
                    case 4:
                        return R.layout.table_cell_select_item;
                    case 5:
                        return R.layout.table_cell_check_item;
                    default:
                        return R.layout.table_cell_content_item;
                }
            }

            @Override
            public BaseAdapter.ViewHolder<TableCell> onCreateViewHolder(View root, int itemType) {
                switch (itemType) {
                    case R.layout.table_cell_title_item:
                        return new TitleHolder(root);
                    case R.layout.table_cell_img_item:
                        return new ImageHolder(root);
                    case R.layout.table_cell_check_item:
                        return new CheckHolder(root);
                    case R.layout.table_cell_date_item:
                        return new DateHolder(root);
                    case R.layout.table_cell_select_item:
                        return new SelectHolder(root);
                    default:
                        return new ContentHolder(root);
                }
            }
        });

        mTable.post(() -> mTable.reMeasure());
    }

    @Override
    protected void initData() {
        super.initData();

        List<TableCell> cells = new LinkedList<>();

        cells.add(new TableCell("信息", "1", 1, 0, 0, 1, 1));
        cells.add(new TableCell("照片", "1", 1, 0, 1, 1, 1));
        cells.add(new TableCell("照片", "1", 1, 0, 2, 1, 1));
        cells.add(new TableCell("姓名/生日", "1", 1, 0, 3, 1, 1));
        cells.add(new TableCell("身高/位置", "1", 1, 0, 4, 1, 1));
        cells.add(new TableCell("运球", "1", 1, 0, 5, 1, 1));
        cells.add(new TableCell("投射", "1", 1, 0, 6, 1, 1));
        cells.add(new TableCell("灌篮", "1", 1, 0, 7, 1, 1));
        cells.add(new TableCell("篮板", "1", 1, 0, 8, 1, 1));
        cells.add(new TableCell("号码", "1", 1, 0, 9, 1, 1));
        cells.add(new TableCell("说明三", "1", 1, 0, 10, 1, 1));


        int row = 1;
        for (int i = 0; i < 1; i++) {
            row = i * 10;
            row++;
            // 1-文本 2-照片 3-日期 4-选择项 5-检查项
            cells.add(new TableCell("1", Integer.toString(row), 1, row, 0, 1, 1));
            cells.add(new TableCell("照片", "0", 2, row, 1, 2, 2));
            cells.add(new TableCell("姓名", "樱木花道", 1, row, 3, 1, 1));
            cells.add(new TableCell("身高", "189", 1, row, 4, 1, 1));
            cells.add(new TableCell("运球", "false", 5, row, 5, 1, 2));
            cells.add(new TableCell("投射", "30", 1, row, 6, 1, 2));
            cells.add(new TableCell("灌篮", "99", 1, row, 7, 1, 2));
            cells.add(new TableCell("篮板", "90", 1, row, 8, 1, 2));
            cells.add(new TableCell("号码", "10", 1, row, 9, 1, 2));
            cells.add(new TableCell("说明", "这是说明文本", 1, row, 10, 1, 2));

            row++;
            cells.add(new TableCell("1", Integer.toString(row), 1, row, 0, 1, 1));
            cells.add(new TableCell("生日", "2020.1.3", 3, row, 3, 1, 1));
            cells.add(new TableCell("位置", "大前锋", 4, row, 4, 1, 1));


            row++;
            // 1-文本 2-照片 3-日期 4-选择项 5-检查项
            cells.add(new TableCell("1", Integer.toString(row), 1, row, 0, 1, 1));
            cells.add(new TableCell("照片", "1", 2, row, 1, 2, 2));
            cells.add(new TableCell("姓名", "三井寿", 1, row, 3, 1, 1));
            cells.add(new TableCell("身高", "184", 1, row, 4, 1, 1));
            cells.add(new TableCell("运球", "false", 5, row, 5, 1, 2));
            cells.add(new TableCell("投射", "90", 1, row, 6, 1, 2));
            cells.add(new TableCell("灌篮", "65", 1, row, 7, 1, 2));
            cells.add(new TableCell("篮板", "60", 1, row, 8, 1, 2));
            cells.add(new TableCell("号码", "10", 1, row, 9, 1, 2));
            cells.add(new TableCell("说明", "这是说明文本", 1, row, 10, 1, 2));

            row++;
            cells.add(new TableCell( "1",Integer.toString(row), 1, row, 0, 1, 1));
            cells.add(new TableCell("生日", "2020.1.3", 3, row, 3, 1, 1));
            cells.add(new TableCell("位置", "分位", 4, row, 4, 1, 1));


            row++;
            // 1-文本 2-照片 3-日期 4-选择项 5-检查项
            cells.add(new TableCell("1", Integer.toString(row), 1, row, 0, 1, 1));
            cells.add(new TableCell("照片", "2", 2, row, 1, 2, 2));
            cells.add(new TableCell("姓名", "流川枫", 1, row, 3, 1, 1));
            cells.add(new TableCell("身高", "187", 1, row, 4, 1, 1));
            cells.add(new TableCell("运球", "true", 5, row, 5, 1, 2));
            cells.add(new TableCell("投射", "85", 1, row, 6, 1, 2));
            cells.add(new TableCell("灌篮", "90", 1, row, 7, 1, 2));
            cells.add(new TableCell("篮板", "80", 1, row, 8, 1, 2));
            cells.add(new TableCell("号码", "11", 1, row, 9, 1, 2));
            cells.add(new TableCell("说明", "这是说明文本", 1, row, 10, 1, 2));

            row++;
            cells.add(new TableCell( "1",Integer.toString(row), 1, row, 0, 1, 1));
            cells.add(new TableCell("生日", "2020.1.3", 3, row, 3, 1, 1));
            cells.add(new TableCell("位置", "小前锋", 4, row, 4, 1, 1));


            row++;
            // 1-文本 2-照片 3-日期 4-选择项 5-检查项
            cells.add(new TableCell("1", Integer.toString(row), 1, row, 0, 1, 1));
            cells.add(new TableCell("照片", "3", 2, row, 1, 2, 2));
            cells.add(new TableCell("姓名", "宫城良田", 1, row, 3, 1, 1));
            cells.add(new TableCell("身高", "169", 1, row, 4, 1, 1));
            cells.add(new TableCell("运球", "true", 5, row, 5, 1, 2));
            cells.add(new TableCell("投射", "80", 1, row, 6, 1, 2));
            cells.add(new TableCell("灌篮", "50", 1, row, 7, 1, 2));
            cells.add(new TableCell("篮板", "50", 1, row, 8, 1, 2));
            cells.add(new TableCell("号码", "7", 1, row, 9, 1, 2));
            cells.add(new TableCell("说明", "这是说明文本", 1, row, 10, 1, 2));

            row++;
            cells.add(new TableCell( "1",Integer.toString(row), 1, row, 0, 1, 1));
            cells.add(new TableCell("生日", "2020.1.3", 3, row, 3, 1, 1));
            cells.add(new TableCell("位置", "控卫", 4, row, 4, 1, 1));


            row++;
            // 1-文本 2-照片 3-日期 4-选择项 5-检查项
            cells.add(new TableCell("1", Integer.toString(row), 1, row, 0, 1, 1));
            cells.add(new TableCell("照片", "4", 2, row, 1, 2, 2));
            cells.add(new TableCell("姓名", "赤木刚宪", 1, row, 3, 1, 1));
            cells.add(new TableCell("身高", "197", 1, row, 4, 1, 1));
            cells.add(new TableCell("运球", "false", 5, row, 5, 1, 2));
            cells.add(new TableCell("投射", "40", 1, row, 6, 1, 2));
            cells.add(new TableCell("灌篮", "90", 1, row, 7, 1, 2));
            cells.add(new TableCell("篮板", "90", 1, row, 8, 1, 2));
            cells.add(new TableCell("号码", "4", 1, row, 9, 1, 2));
            cells.add(new TableCell("说明", "这是说明文本", 1, row, 10, 1, 2));

            row++;
            cells.add(new TableCell("1",Integer.toString(row),  1, row, 0, 1, 1));
            cells.add(new TableCell("生日", "2020.1.3", 3, row, 3, 1, 1));
            cells.add(new TableCell("位置", "中锋", 4, row, 4, 1, 1));
        }


        mAdapter.addList(cells);
    }


    /**
     * 标题的Holder
     */
    class TitleHolder extends BaseAdapter.ViewHolder<TableCell> {

        TextView mTitle;

        public TitleHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.tv_name);
        }

        @Override
        protected void onBind(TableCell tableCell) {
            mTitle.setText(tableCell.getName());
        }
    }

    /**
     * 内容的Holder
     */
    class ContentHolder extends BaseAdapter.ViewHolder<TableCell>{

        TextView mContent;

        public ContentHolder(View itemView) {
            super(itemView);
            mContent = itemView.findViewById(R.id.tv_name);
        }

        @Override
        protected void onBind(TableCell tableCell) {
            mContent.setText(tableCell.getValue());
        }
    }

    /**
     * 图片的Holder
     */
    class ImageHolder extends BaseAdapter.ViewHolder<TableCell> {

        private ImageView mContent;

        public ImageHolder(View itemView) {
            super(itemView);
            mContent = itemView.findViewById(R.id.iv_content);
        }

        @Override
        protected void onBind(TableCell tableCell) {
            mContent.setImageResource(drawable[Integer.valueOf(tableCell.getValue())]);
        }
    }

    class CheckHolder extends BaseAdapter.ViewHolder<TableCell>{

        CheckBox mBox;

        public CheckHolder(View itemView) {
            super(itemView);
            mBox = itemView.findViewById(R.id.cb_name);
        }

        @Override
        protected void onBind(TableCell tableCell) {
            boolean result = Boolean.valueOf(tableCell.getValue());
            mBox.setChecked(result);
        }
    }

    /**
     * 日期Holder
     */
    class DateHolder extends BaseAdapter.ViewHolder<TableCell> {

        TextView mDate;

        public DateHolder(View itemView) {
            super(itemView);
            mDate = itemView.findViewById(R.id.tv_date);
        }

        @Override
        protected void onBind(TableCell tableCell) {
            mDate.setText(tableCell.getValue());
        }
    }

    /**
     * 选择的Holder
     */
    class SelectHolder extends BaseAdapter.ViewHolder<TableCell> {

        TextView mSelect;

        public SelectHolder(View itemView) {
            super(itemView);
            mSelect = itemView.findViewById(R.id.tv_select);
        }

        @Override
        protected void onBind(TableCell tableCell) {
            mSelect.setText(tableCell.getValue());
        }
    }
}
