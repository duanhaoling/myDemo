package com.example.mydemo.greendaotdemo;

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
    private List<VideoEntity> mDatas;
    private ActivityDbTestBinding binding;
    private VideoEntity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_db_test);

        dbHelper = new MyDataBaseHelper(this, "BookStore.db", null, 1);

        binding.setPresenter(new Presenter());
        binding.createDatabase.setOnClickListener(view -> {
            dbHelper.getWritableDatabase();
        });

        initData();
    }

    private void initData() {
        entity = new VideoEntity();
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
            Toast.makeText(DbTestActivity.this, "hello world", Toast.LENGTH_SHORT).show();
        }

        public void onClickBinding(VideoEntity entity) {
            Toast.makeText(DbTestActivity.this, "文件名：" + entity.getFileName(), Toast.LENGTH_SHORT).show();
        }
    }

}
