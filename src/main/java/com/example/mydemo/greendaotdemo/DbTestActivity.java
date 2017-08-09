package com.example.mydemo.greendaotdemo;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.mydemo.R;
import com.example.mydemo.databinding.ActivityDbTestBinding;

import java.util.List;


public class DbTestActivity extends AppCompatActivity {

    private MyDataBaseHelper dbHelper;
    private List<LocalVideo> mDatas;
    private ActivityDbTestBinding binding;
    private LocalVideo entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_db_test);

        dbHelper = new MyDataBaseHelper(this, "BookStore.db", null, 2);

        binding.setPresenter(new Presenter());
        binding.createDatabase.setOnClickListener(view -> {
            dbHelper.getWritableDatabase();
        });

        initData();
    }

    private void initData() {
        entity = new LocalVideo();
        entity.setFileName("videofile");
        entity.setLatitude("80.88 ");
        entity.setLongitude("110.90");
        binding.setVideo(entity);
    }

    public class Presenter {

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            entity.setFileName(s.toString());
            binding.setVideo(entity);
        }

        public void onClick(View view) {
            if (binding.viewstub.getViewStub() != null) {
                //viewstub在以下方法调用的时候inflate，且只可以inflate一次
                binding.viewstub.getViewStub().inflate();
            }
            Toast.makeText(DbTestActivity.this, "hello viewstub ,only inflate once", Toast.LENGTH_SHORT).show();
        }

        public void onClickBinding(LocalVideo entity) {
            Toast.makeText(DbTestActivity.this, "文件名：" + entity.getFileName(), Toast.LENGTH_SHORT).show();
        }

        public void insertData() {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            // 开始组装第一条数据
            values.put("name", "The Da Vinci Code");
            values.put("author", "Dan Brown");
            values.put("pages", 454);
            values.put("price", 16.96);
            db.insert("Book", null, values); // 插入第一条数据 values.clear();
            // 开始组装第二条数据
            values.put("name", "The Lost Symbol");
            values.put("author", "Dan Brown");
            values.put("pages", 510);
            values.put("price", 19.95);
            db.insert("Book", null, values); // 插入第二条数据
        }

        public void query() {
            Toast.makeText(DbTestActivity.this, "数据查询", Toast.LENGTH_SHORT).show();
        }

        public void update() {

        }
    }

}
