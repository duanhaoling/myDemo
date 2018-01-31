package com.example.mydemo.view.recycledemo;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mydemo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecyclerDemoActivity extends AppCompatActivity {

    @Bind(R.id.rv_recycler_demo)
    RecyclerView rv;
    private List<Integer> datas;
    private RecyclerView.ItemDecoration itemDecoration;
    private RvAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_demo);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        datas = new ArrayList<>();
        for (int i = 0; i < 38; i++) {
            Resources res = getResources();
            int j = i % 5;
            datas.add(res.getIdentifier("a" + j, "miapmap", getPackageName()));
        }

        /**
         *用来确定每一个item如何进行排列摆放
         * LinearLayoutManager 相当于ListView的效果
         GridLayoutManager相当于GridView的效果
         StaggeredGridLayoutManager 瀑布流
         */
        /**第一步：设置布局管理器**/
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        /**第二步：添加分割线**/
        itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(itemDecoration);
        /**第三步：设置适配器**/
        rvAdapter = new RvAdapter(this, datas);
        rv.setAdapter(rvAdapter);
        rvAdapter.setOnItemClickListener(new RvAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position, Integer data) {
                Toast.makeText(RecyclerDemoActivity.this, "点击了" + position, Toast.LENGTH_SHORT).show();
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recycle_demo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /**竖向的ListView**/
            case R.id.id_action_listview:
                rv.setBackgroundColor(Color.TRANSPARENT);
                rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                rvAdapter.setType(0);
                rv.removeItemDecoration(itemDecoration);
                rv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
                rv.addItemDecoration(itemDecoration);
                break;
            /**横向的ListView**/
            case R.id.id_action_horizontalListView:
                rvAdapter.setType(1);
                rv.removeItemDecoration(itemDecoration);
                rv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
                rv.addItemDecoration(itemDecoration);
                break;

            /**竖向的GridView**/
            case R.id.id_action_gridview:
                rvAdapter.setType(1);
                rv.setBackgroundColor(Color.TRANSPARENT);
                rv.removeItemDecoration(itemDecoration);
                rv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                rv.setLayoutManager(new GridLayoutManager(this, 5));
                itemDecoration = new DividerGridItemDecoration(this);
                rv.addItemDecoration(itemDecoration);
                break;
            /**横向的GridView**/
            case R.id.id_action_horizontalGridView:
                rvAdapter.setType(1);
                rv.setBackgroundColor(Color.TRANSPARENT);
                rv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                rv.removeItemDecoration(itemDecoration);
                rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
                itemDecoration = new DividerGridItemDecoration(this);
                rv.addItemDecoration(itemDecoration);
                break;
            /**竖向的瀑布流**/
            case R.id.id_action_staggeredgridview:
                rvAdapter.setType(3);
                rv.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                rv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                rv.removeItemDecoration(itemDecoration);
                rv.setLayoutManager(new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL));
                break;
            /**添加一个数据**/
            case R.id.id_action_add:
                rvAdapter.notifyItemInserted(1);
                break;
            /**删除一个数据**/
            case R.id.id_action_delete:
                rvAdapter.notifyItemRemoved(1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
