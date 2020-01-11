package com.orient.ui.ui.fragment.table;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.orient.me.widget.rv.adapter.BaseAdapter;
import com.orient.me.widget.rv.adapter.TableAdapter;
import com.orient.me.widget.rv.adapter.TableView;
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


        mTable.setAdapter(mAdapter = new TableAdapter<TableCell>(new ArrayList<>()) {
            @Override
            public int getItemLayout(TableCell tableCell, int pos) {
                switch (tableCell.getType()) {
                    case 2:
                        return R.layout.table_cell_img_item;
                    case 3:
                        return R.layout.table_cell_title_item;
                    case 4:
                        return R.layout.table_cell_edit_item;
                    case 5:
                        return R.layout.table_cell_check_item;
                    case 6:
                        return R.layout.table_cell_camera_item;
                    default:
                        return R.layout.table_cell_content_item;
                }
            }

            @Override
            public BaseAdapter.ViewHolder<TableCell> onCreateViewHolder(View root, int itemType) {
                switch (itemType) {
                    case R.layout.table_cell_img_item:
                        return new ImgHolder(root);
                    case R.layout.table_cell_edit_item:
                        return new EditHolder(root);
                    case R.layout.table_cell_check_item:
                        return new CheckHolder(root);
                    case R.layout.table_cell_camera_item:
                        return new CameraHolder(root);
                    default:
                        return new ViewHolder(root);
                }
            }
        });

        mTable.post(() -> mTable.reMeasure());
    }

    @Override
    protected void initData() {
        super.initData();

        List<TableCell> cells = new LinkedList<>();

        cells.add(new TableCell("表格", "1", 3, 0, 0, 1, 1));
        cells.add(new TableCell("姓名", "1", 3, 0, 1, 1, 1));
        cells.add(new TableCell("编辑项", "1", 3, 0, 2, 1, 1));
        cells.add(new TableCell("照片项", "1", 3, 0, 3, 1, 1));
        cells.add(new TableCell("日期项", "1", 3, 0, 4, 1, 1));
        cells.add(new TableCell("选择项", "1", 3, 0, 5, 1, 1));
        cells.add(new TableCell("检查项", "1", 3, 0, 6, 1, 1));
        cells.add(new TableCell("说明项一", "1", 3, 0, 7, 1, 1));
        cells.add(new TableCell("说明项二", "1", 3, 0, 8, 1, 1));
        cells.add(new TableCell("说明项三", "1", 3, 0, 9, 1, 1));
        cells.add(new TableCell("说明项四", "1", 3, 0, 10, 1, 1));

        cells.add(new TableCell("P1", "1", 1, 1, 0, 1, 1));
        cells.add(new TableCell("1", "1", 2, 1, 1, 2, 2));
        cells.add(new TableCell("项目一", "1", 1, 1, 3, 1, 1));
        cells.add(new TableCell("项目一", "1", 1, 1, 4, 1, 2));
        cells.add(new TableCell("项目一", "1", 1, 1, 5, 1, 1));
        cells.add(new TableCell("项目一", "1", 1, 1, 6, 1, 1));

        cells.add(new TableCell("P2", "1", 1, 2, 0, 1, 1));
        cells.add(new TableCell("9", "项目二", 6, 2, 3, 1, 1));
        cells.add(new TableCell("11", "项目二", 1, 2, 5, 1, 1));
        cells.add(new TableCell("12", "项目三", 1, 2, 6, 1, 1));

        mAdapter.addList(cells);
    }

    class ViewHolder extends BaseAdapter.ViewHolder<TableCell> {

        //@BindView(R.id.tv_name)
        TextView mNameTv;

        public ViewHolder(View itemView) {
            super(itemView);
            mNameTv = itemView.findViewById(R.id.tv_name);
        }

        @Override
        protected void onBind(TableCell tableCell) {
            mNameTv.setText(tableCell.getName());
        }
    }

    class ImgHolder extends BaseAdapter.ViewHolder<TableCell> {
        ImageView mContent;

        public ImgHolder(View itemView) {
            super(itemView);

            mContent = itemView.findViewById(R.id.iv_content);
        }

        @Override
        protected void onBind(TableCell tableCell) {
            mContent.setImageResource(R.drawable.grid_head_1);
        }
    }

    class EditHolder extends BaseAdapter.ViewHolder<TableCell> {
        EditText mContent;

        public EditHolder(View itemView) {
            super(itemView);

            mContent = itemView.findViewById(R.id.et_content);
        }

        @Override
        protected void onBind(TableCell tableCell) {

        }
    }

    class CheckHolder extends BaseAdapter.ViewHolder<TableCell> {
        CheckBox mCheckBox;

        public CheckHolder(View itemView) {
            super(itemView);

            mCheckBox = itemView.findViewById(R.id.eb_content);
        }


        @Override
        protected void onBind(TableCell tableCell) {

        }
    }

    class CameraHolder extends BaseAdapter.ViewHolder<TableCell> {
        ImageView mCamera;

        public CameraHolder(View itemView) {
            super(itemView);

            mCamera = itemView.findViewById(R.id.iv_camera);
        }


        @Override
        protected void onBind(TableCell tableCell) {

        }
    }
}
