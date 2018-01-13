package com.bysj.eyeapp.util;

/**
 * Created by lcplcp on 2018/1/12.
 */

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库操作工具类
 */

public class DbHelper extends SQLiteOpenHelper{
    private Context context;

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //初始化表结果
        String eyedataCreateSql = SqlConst.EYEDATA_CREATE;
        db.execSQL(eyedataCreateSql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }





}

