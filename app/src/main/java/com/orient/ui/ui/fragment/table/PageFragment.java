package com.orient.ui.ui.fragment.table;


import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.orient.me.widget.rv.adapter.GridAdapter;
import com.orient.me.widget.rv.layoutmanager.table.TableLayoutManager;
import com.orient.me.widget.rv.rv.TableRecyclerView;
import com.orient.ui.R;
import com.orient.ui.ui.activity.table.TableCell;
import com.orient.ui.ui.fragment.BaseFragment;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends BaseFragment {

    int[] drawable = new int[]{
            R.drawable.grid_head_1,
            R.drawable.grid_head_2,
            R.drawable.grid_head_3,
            R.drawable.grid_normal_1,
            R.drawable.grid_normal_2,
            R.drawable.grid_normal_3,
            R.drawable.grid_normal_4,
            R.drawable.grid_normal_5,
            R.drawable.grid_special_1,
            R.drawable.grid_special_2,
    };

    @BindView(R.id.rv_table)
    TableRecyclerView mTableView;

    private GridAdapter<TableCell> mAdapter;


    public PageFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_page;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        mTableView.setLayoutManager(new TableLayoutManager(TableLayoutManager.MODE_A,3,6));
        mTableView.setAdapter(mAdapter = new GridAdapter<TableCell>() {
            @Override
            public ViewHolder<TableCell> onCreateViewHolder(View root, int viewType) {
                return new ImageHolder(root);
            }

            @Override
            public int getItemLayout(TableCell tableCell, int position) {
                return R.layout.table_cell_img_item;
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();

        List<TableCell> cells = new LinkedList<>();

        cells.add(new TableCell("1", "1", 1, 1, 1, 2, 2));
        cells.add(new TableCell("1", "2", 1, 1, 3, 1, 1));

        cells.add(new TableCell("1", "3", 1, 2, 3, 1, 1));

        cells.add(new TableCell("1", "4", 1, 3, 1, 1, 1));
        cells.add(new TableCell("1", "5", 1, 3, 2, 1, 1));
        cells.add(new TableCell("1", "6", 1, 3, 3, 1, 1));

        cells.add(new TableCell("1", "7", 1, 4, 1, 3, 1));

        cells.add(new TableCell("1", "8", 1, 5, 1, 1, 1));
        cells.add(new TableCell("1", "9", 1, 5, 2, 1, 1));
        cells.add(new TableCell("1", "10", 1, 5, 3, 1, 2));

        cells.add(new TableCell("1", "11", 1, 6, 1, 1, 1));
        cells.add(new TableCell("1", "12", 1, 6, 2, 1, 1));

        mAdapter.addAllData(cells);
    }

    /**
     * 图片的Holder
     */
    class ImageHolder extends GridAdapter.ViewHolder<TableCell> {

        //@BindView(R.id.iv_content)
        ImageView mContent;

        public ImageHolder(View itemView) {
            super(itemView);
            mContent = itemView.findViewById(R.id.iv_content);
        }

        @Override
        protected void onBind(TableCell tableCell) {
            int i = Integer.valueOf(tableCell.getValue());
            i = i % drawable.length;
            mContent.setImageResource(drawable[i]);
        }
    }
}
