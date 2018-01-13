package com.bysj.eyeapp.dao;

/**
 * Created by lcplcp on 2018/1/12.
 */

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bysj.eyeapp.dto.EyedataDTO;
import com.bysj.eyeapp.util.DbHelper;
import com.bysj.eyeapp.util.GlobalConst;

import java.util.Date;

/**
 * 用眼数据dao
 */
public class EyedataDAO {


    /**
     * 根据日期查看用眼数据
     * @param date 日期
     * @param context 上下文
     * @return EyedataDTO对象
     */
    public EyedataDTO findEyedataByDate( Context context,String date){
        DbHelper dbHelper = new DbHelper(context, GlobalConst.DATABASE_NAME,null,GlobalConst.DATABASE_VERSION);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String sql = "select t.* from eyedata t where t.date=? limit 1";
        Cursor cursor = database.rawQuery(sql,new String[]{date});
        EyedataDTO eyedataDTO = new EyedataDTO();
        if(cursor.moveToNext()){
            int id = cursor.getInt(1);
            eyedataDTO.setId(getIntByColumnName(cursor,"id"));
            eyedataDTO.setOpenScreenTime(getIntByColumnName(cursor,"open_screen_time"));
            eyedataDTO.setOpenScreenCount(getIntByColumnName(cursor,"open_screen_count"));
            eyedataDTO.setIndoorTime(getIntByColumnName(cursor,"indoor_time"));
            eyedataDTO.setOutdoorTime(cursor.getColumnIndex("outdoor_time"));
            eyedataDTO.setDate(getStringByColumnName(cursor,"date"));
        }
        return eyedataDTO;
    }
    private int getIntByColumnName(Cursor cursor,String columnName){
        //获取列号
        int columnIndex = cursor.getColumnIndex(columnName);
        return cursor.getInt(columnIndex);
    }

    private String getStringByColumnName(Cursor cursor,String columnName){
        //获取列号
        int columnIndex = cursor.getColumnIndex(columnName);
        return cursor.getString(columnIndex);
    }


    public void updateOpenScreenCount(Context context,String date){
        String sql = "update eyedata set open_screen_count=open_screen_count+1 where date=?";
        DbHelper dbHelper = new DbHelper(context, GlobalConst.DATABASE_NAME,null,GlobalConst.DATABASE_VERSION);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.execSQL(sql,new String[]{date});
    }

    /**
     * 根据传进来的dto，更新eyedata表数据
     * @param context 上下文
     * @param dto 需要更新的数据
     * @param date 需要更新的用眼数据的日期
     */
    public void updateEyedata(Context context,EyedataDTO dto,String date){
        StringBuffer sqlTem = new StringBuffer("update eyedata set ");
        if(dto.getOpenScreenTime()>=0){
            sqlTem.append(String.format(" open_screen_time=%s ,",dto.getOpenScreenTime()));
        }
        if(dto.getOpenScreenCount()>=0){//注意，当dto作为传进参数时，OpenScreenCount仅仅是标记值，标记是否是执行开屏次数加1操作
            sqlTem.append(String.format(" open_screen_count=%s, ",dto.getOpenScreenCount()));
        }
        if(dto.getIndoorTime()>=0){
            sqlTem.append(String.format(" indoor_time=%s ,",dto.getIndoorTime()));
        }
        if(dto.getOutdoorTime()>=0){
            sqlTem.append(String.format(" outdoor_time=%s ",dto.getOutdoorTime()));
        }
        sqlTem.append(String.format(" where date='%s' ",dto.getDate()));
        DbHelper dbHelper = new DbHelper(context, GlobalConst.DATABASE_NAME,null,GlobalConst.DATABASE_VERSION);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.execSQL(sqlTem.toString());
    }

    public void insertEyedata(Context context,EyedataDTO dto){
        String sql = "INSERT INTO `eyedata` (open_screen_time,open_screen_count,indoor_time,outdoor_time,date) VALUES (?,?,?,?,?)";
        DbHelper dbHelper = new DbHelper(context, GlobalConst.DATABASE_NAME,null,GlobalConst.DATABASE_VERSION);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.execSQL(sql,new String []{dto.getOpenScreenTime() + "" ,dto.getOpenScreenCount() + "",
                                            dto.getIndoorTime() + "",dto.getOutdoorTime() + "",dto.getDate()});

    }

    public boolean judgeEyedataExistByDate(Context context,String date){
        String sql = "select t.id from eyedata t where t.date=? limit 1";
        DbHelper dbHelper = new DbHelper(context, GlobalConst.DATABASE_NAME,null,GlobalConst.DATABASE_VERSION);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(sql,new String[]{date});
        return cursor.moveToNext();
    }


}
