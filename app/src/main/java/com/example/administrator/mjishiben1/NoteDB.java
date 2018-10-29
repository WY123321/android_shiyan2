package com.example.administrator.mjishiben1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class NoteDB {
    private SQLiteDatabase db;
    private Context context;
    public NoteDB(Context context, SQLiteDatabase db) {
        this.db = db;
        this.context = context;
    }
    public void create_db(){//创建或打开数据库
        db = SQLiteDatabase.openOrCreateDatabase(context.getFilesDir().toString()+"/mynotes.db3", null);
        db.execSQL("DROP TABLE IF EXISTS studentScore");
        if(db == null){
            Toast.makeText(context,"数据库创建不成功",Toast.LENGTH_LONG).show();
        }
        db.execSQL("create table if not exists notes(_id integer primary key autoincrement," +
                "title text," +
                "context text," +
                "time varchar(20))");//建表
    }
    public Cursor query_db(int item_ID){
        Cursor cursor = db.rawQuery("select * from notes where _id='"+item_ID+"';",null);
        return cursor;//定义一个id方便后面查询删除
    }
    public void update_db(String title,String text,String time,int item_ID){//改
        if( text.isEmpty()){
            Toast.makeText(context, "各字段不能为空", Toast.LENGTH_LONG).show();
        }
        else{
            db.execSQL("update notes set context='"+text+ "',title='"+title+"',time='"+time+"'where _id='" + item_ID+"'");
        }
    }
    public void delete_db(int item_ID){
        db.execSQL("delete from notes where _id='" + item_ID+"'");
    }
    public Cursor query_db(){
        Cursor cursor = db.rawQuery("select * from notes order by time desc",null);
        return cursor;
    }

    public Cursor query_db(String SearchView){
        Cursor cursor = db.rawQuery("select * from notes where context like '%" + SearchView  + "%'", null);
        return cursor;
    }
    public void insert_db(String title,String text,String time){
        if(text.isEmpty()){
            Toast.makeText(context, "各字段不能为空", Toast.LENGTH_LONG).show();
        }
        else{
            db.execSQL("insert into notes(title,context,time) values('"+ title+"','"+ text+ "','"+time+"');");
        }
    }
    public void close_db(){
        db.close();
    } //关闭数据库
}
