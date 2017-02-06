package com.zyt.tx.testapplication.uploadBigFile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zyt.tx.testapplication.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseFileActivity extends AppCompatActivity {

    public static final int RESULTCODE = 0x12;

    @BindView(R.id.tvPath)
    TextView tvPath;
    @BindView(R.id.listView)
    ListView listView;

    File parentFile;
    File[] files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_file);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            parentFile = Environment.getExternalStorageDirectory();
            files = parentFile.listFiles();
            inflateListView(files);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (files[position].isFile()) {
                        //TODO
                        String path = files[position].getPath();
                        Log.i("taxi", "当前的file path ： " + path);
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("testStr", "第一个activity的内容");
                        bundle.putString("current_path", path);
                        intent.putExtras(bundle);
                        setResult(RESULTCODE, intent);
                        finish();
                    } else {
                        //获取点击文件夹下面的所有的文件
                        File[] tep = files[position].listFiles();
                        if (tep == null || tep.length == 0) {
                            Log.e("taxi", "当前文件夹下没有文件！");
                            Toast.makeText(ChooseFileActivity.this, "该文件夹下没有文件", Toast.LENGTH_SHORT).show();
                        } else {
                            //获取单击的列表的列表项对应的文件夹，设为当前的父类文件夹
                            parentFile = files[position];
                            //保存当前的父文件夹内全部的文件和文件夹
                            files = tep;
                            //再次更新ListView
                            inflateListView(files);
                        }
                    }
                }
            });
        }
    }

    private void inflateListView(File[] files) {
        List<HashMap<String, Object>> listItems = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            HashMap<String, Object> listItem = new HashMap<>();
            if (files[i].isDirectory()) {
                listItem.put("icon", R.drawable.folderke);
            } else {
                listItem.put("icon", R.drawable.fileke);
            }
            listItem.put("fileName", files[i].getName());
            listItems.add(listItem);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.list_item
                , new String[]{"icon", "fileName"}
                , new int[]{R.id.icon, R.id.file_name});

        listView.setAdapter(simpleAdapter);

        try {
            tvPath.setText(String.format("当前路径为：%s", parentFile.getCanonicalPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btnParent)
    public void onClick() {
        if (parentFile != Environment.getExternalStorageDirectory()) {
            parentFile = parentFile.getParentFile();
            files = parentFile.listFiles();
            inflateListView(files);
        }
    }
}
