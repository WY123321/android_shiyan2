package com.example.administrator.mjishiben1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private ListView listview;
    View view;
    private List<Map<String, Object>> dataList;//内容，时间
    private Button addNote;//按钮新增
    private TextView tv_note_id ;
    private NoteDB dop;
    private SQLiteDatabase db;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitView();
    }

    private void InitView() {
        listview = (ListView) findViewById(R.id.listview);
        dataList = new ArrayList<Map<String, Object>>();
        addNote = (Button) findViewById(R.id.editnote);
        mSearchView = (SearchView) findViewById(R.id.search);// 给搜索加一个监听器
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 搜索时触发的方法
            @Override

            public boolean onQueryTextSubmit(String query) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                return false;

            }

            // 搜索内容改变时触发的方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null) {
                    showNotesList(newText);
                }
                else{
                    showNotesList(null);
                }
                return false;
            }
        });

        dop = new NoteDB(this, db);

        //设置监听点击时间
        listview.setOnItemClickListener(this);
        //长按
        listview.setOnItemLongClickListener(this);


        addNote.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                //单击新建触发的页面跳转
                Intent intent = new Intent(MainActivity.this, NoteEdit.class);
                intent.putExtra("editModel", "newAdd");
                startActivity(intent);
            }
        });
    }


    //在activity显示的时候更新listview
    @Override
    protected void onStart() {

        super.onStart();
        showNotesList(null);
    }
    private void showNotesList(String mSearchView){
        dop.create_db();
        Cursor cursor=null;
        if(mSearchView!=null){
            cursor = dop.query_db(mSearchView);
        }
        else {
            cursor=dop.query_db();
        }
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.item,
                cursor,
                new String[]{"_id", "title", "time"}, new int[]{R.id.tv_note_id, R.id.tv_note_title, R.id.tv_note_time});
        listview.setAdapter(adapter);
        dop.close_db();
    }
    @Override//长按事件
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        tv_note_id = (TextView)view.findViewById(R.id.tv_note_id);
        final int item_id = Integer.parseInt(tv_note_id.getText().toString());
        Builder builder = new Builder(this);
        builder.setTitle("删除便签");
        builder.setMessage("确认删除吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dop.create_db();
                dop.delete_db(item_id);
                dop.close_db();
                listview.invalidate();
                showNotesList(null);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create();
        builder.show();
        return true;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        tv_note_id = (TextView)view.findViewById(R.id.tv_note_id);
        int item_id = Integer.parseInt(tv_note_id.getText().toString());
        Intent intent = new Intent(MainActivity.this,NoteEdit.class);
        intent.putExtra("editModel", "update");
        intent.putExtra("noteId", item_id);
        startActivity(intent);
    }
}
