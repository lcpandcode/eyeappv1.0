package com.bysj.eyeapp.util;

/**
 * Created by lcplcp on 2018/1/12.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 读取sql目录下的sql文件的工具类
 */
public class ReadSqlFile {
    //这个map用来存储sql语句，key是文件名字，value是sql文件的sql语句
    private static Map<String,String> sqlMap = new HashMap<>();
    static {
        init();
    }


    /**
     * 根据文件名字获取sql文件中的sql语句
     * @param fileName
     * @return
     */
    public static String getSqlByFileName(String fileName){
        return sqlMap.get(fileName);
    }

    /**
     * 加入sql语句
     */
    public static void addSql(String fileName,String value){
        sqlMap.put(fileName,value);
    }



    private static void init(){
        File sql = new File("app\\src\\main\\java\\com\\bysj\\eyeapp\\util\\sql");
        //读取文件夹下所有文件
        File [] sqlFiles = sql.listFiles();
        //读取数据
        for(File f : sqlFiles){
            if(f.isDirectory()){
                continue;
            }
            String key = f.getName();
            BufferedReader reader = null;
            StringBuffer value = new StringBuffer();
            try {
                reader = new BufferedReader(new FileReader(f));
                while(true){
                    String strTem = reader.readLine();
                    if(strTem==null){
                        break;
                    }
                    value.append(strTem);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(reader!=null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            sqlMap.put(key,value.toString());
        }
    }
}
