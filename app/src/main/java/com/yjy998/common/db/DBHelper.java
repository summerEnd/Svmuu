package com.yjy998.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Province(id int,name varchar(50),primary key (id))");
        db.execSQL("create table City(cid int not null,city varchar(50) primary key,pid int foreign key references Provincial(pid))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE Province;");
        db.execSQL("DROP TABLE City;");
        onCreate(db);
    }
}
