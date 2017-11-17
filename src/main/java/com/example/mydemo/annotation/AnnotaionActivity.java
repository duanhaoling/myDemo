package com.example.mydemo.annotation;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.mydemo.R;
import com.ldh.android.javaprocessor.CustomAnnotation;

import java.lang.reflect.Field;

public class AnnotaionActivity extends AppCompatActivity {

    @GetViewTo(R.id.toolbar)
    private Toolbar toolbar;
    @GetViewTo(R.id.fab)
    private FloatingActionButton fab;

    @CustomAnnotation
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotaion);
        //通过注解生成View
        getAllAnnotationView();
        initView();
    }

    /**
     * 解析注解，获取控件
     */
    private void getAllAnnotationView() {
        //获得成员变量
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                //判断注解
                if (field.getAnnotations() != null) {
                    //确定注解类型
                    if (field.isAnnotationPresent(GetViewTo.class)) {
                        //允许修改反射属性
                        field.setAccessible(true);
                        GetViewTo getViewTo = field.getAnnotation(GetViewTo.class);
                        //findViewById将注解的id，找到View注入成员变量中
                        field.set(this, findViewById(getViewTo.value()));
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void initView() {
        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
