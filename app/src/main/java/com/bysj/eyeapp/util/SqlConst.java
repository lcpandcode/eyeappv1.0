package com.bysj.eyeapp.util;

/**
 * Created by lcplcp on 2018/1/12.
 */

public class SqlConst {
    public static final String EYEDATA_CREATE = "CREATE TABLE eyedata (" +
            "  id Integer PRIMARY KEY AUTOINCREMENT," +
            "  open_screen_time int(11) ," +
            "  open_screen_count int(11) ," +
            "  indoor_time int(11) ," +
            "  outdoor_time int(11) ," +
            "  date date " +
            ")";
    public static final String EYEDATA_SELECT_BY_DATE = "select t.* from eyedata t where t.date=?";
}
