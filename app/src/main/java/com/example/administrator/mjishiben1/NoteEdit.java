package com.example.administrator.mjishiben1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Intent;
import android.database.Cursor;

public class NoteEdit extends AppCompatActivity implements View.OnClickListener{
    private TextView time;
    private NoteDB dop;
    private EditText et_Notes;
    private Button btn_ok;
    Intent intent;
    String editModel = null;
    int item_Id;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        InitView();
    }
    private void InitView() {
        time = (TextView) findViewById(R.id.tv_note_time);
        et_Notes = (EditText) findViewById(R.id.et_content);
        btn_ok = (Button) findViewById(R.id.ok);
        dop = new NoteDB(this,db);
        intent = getIntent();
        editModel = intent.getStringExtra("editModel");
        item_Id = intent.getIntExtra("noteId", 0);
        loadData();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");//时间
        String dateString = sdf.format(date);
        time.setText(dateString);
        btn_ok.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok: //取EditText中的内容
                String context = et_Notes.getText().toString();
                //截取EditText中的前10个作为title
                String title ;
                if(context.length()<10){
                    title = context;
                }
                else{
                    title = context.substring(0,10);
                }
                if(context.isEmpty()){
                    Toast.makeText(NoteEdit.this, "记事不能为空!", Toast.LENGTH_LONG).show();
                }
                else{
                    SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy-MM-dd HH:mm");
                    Date   curDate   =   new   Date(System.currentTimeMillis());//时间
                    String   time   =   formatter.format(curDate);
                    dop.create_db(); //打开数据库
                    // 判断是更新还是新增
                    if(editModel.equals("newAdd")){
                        //新增的话将title,context,time放进数据库
                        dop.insert_db(title,context,time);
                    }
                    //更新的话将title,context,time,放入数据库，并加入item_Id
                    else if(editModel.equals("update")){
                        dop.update_db(title,context,time,item_Id);
                    }
                    dop.close_db();//关闭数据库
                    NoteEdit.this.finish();//结束当前活动
                }
                break;
        }
    }
    private void loadData(){
        if(editModel.equals("newAdd")){//新建的话，将editText清空
            et_Notes.setText("");
        }
        else if(editModel.equals("update")){//如果已存在的话，将数据从数据库中取出，放在editText中
            dop.create_db();
            Cursor cursor = dop.query_db(item_Id);
            cursor.moveToFirst();
            String context = cursor.getString(cursor.getColumnIndex("context")); //从数据库中取出
            et_Notes.append(context);
            dop.close_db();
        }
    }

}

