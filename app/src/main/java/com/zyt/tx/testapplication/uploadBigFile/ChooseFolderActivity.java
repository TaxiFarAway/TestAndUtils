package com.zyt.tx.testapplication.uploadBigFile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zyt.tx.testapplication.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseFolderActivity extends AppCompatActivity {
    public static final int RESULTCODE = 0x13;

    @BindView(R.id.tvPath)
    TextView tvPath;
    @BindView(R.id.listView)
    ListView listView;

    private String parentFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_folder);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            tvPath.setText("SDCard do not exits");
            return;
        }
        parentFile = Environment.getExternalStorageDirectory().getAbsolutePath();
        initListView(parentFile);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = new File(parentFile);
                File[] files = file.listFiles();
                File[] temp = files[position].listFiles();
                if (temp == null || temp.length == 0) {
                    Toast.makeText(ChooseFolderActivity.this, "该文件件下没有文件", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isFolder = hasFolder(temp);
                    if (isFolder) {
                        parentFile = files[position].getPath();
                        showPop();
                    } else {
                        Toast.makeText(ChooseFolderActivity.this, "该文件夹下没有文件夹", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("current_path", files[position].getPath());
                        intent.putExtras(bundle);
                        setResult(RESULTCODE, intent);
                        finish();
                    }
                }
            }
        });
    }

    private void showPop() {
        String[] strs = new String[]{"进入下一级界面", "返回当前界面"};
        AlertDialog builder = new AlertDialog.Builder(this)
                .setTitle("选择对话框")
                .setIcon(R.drawable.icon)
                .setItems(strs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            initListView(parentFile);
                        } else if (which == 1) {
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("current_path", parentFile);
                            intent.putExtra("bundle", bundle);
                            setResult(RESULTCODE, intent);
                            finish();
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        parentFile = getParentUrl(parentFile);
                        initListView(parentFile);
                    }
                }).setCancelable(false).create();
        builder.show();
    }

    /*返回上一层目录*/
    private String getParentUrl(String parentFile) {
        if (null == parentFile) {
            return "";
        }
        return parentFile.substring(0, parentFile.lastIndexOf("/"));
    }

    private boolean hasFolder(File[] temp) {
        if (temp == null || temp.length == 0) {
            return false;
        }

        for (File file : temp) {
            if (file.isDirectory()) {
                return true;
            }
        }
        return false;
    }

    private void initListView(String parentFile) {
        File file_p = new File(parentFile);
        if (!file_p.exists()) {
            return;
        }
        File[] files = file_p.listFiles();
        List<Map<String, Object>> listItems = new ArrayList<>();
        for (File file : files) {
            Map<String, Object> item = new HashMap<>();
            if (file.isDirectory()) {
                item.put("icon", R.drawable.folderke);
                item.put("fileName", file.getName());
            }
            listItems.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(ChooseFolderActivity.this, listItems
                , R.layout.list_item
                , new String[]{"icon", "fileName"}
                , new int[]{R.id.icon, R.id.file_name});
        listView.setAdapter(adapter);
        tvPath.setText(String.format("当前路径: %s", parentFile));
    }

    @OnClick(R.id.btnParent)
    public void onClick() {
        if (!parentFile.equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
            parentFile = getParentUrl(parentFile);
            initListView(parentFile);
        }
    }
}
