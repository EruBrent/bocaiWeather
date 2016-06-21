package com.bocaiweather.app.Activity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.bocaiweather.app.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * 数据库管理类
 */
public class DBManager
{
    private static String TAG = DBManager.class.getSimpleName();
    private final int BUFFER_SIZE = 400000;
    public final String DB_NAME = "cityname.db"; //数据库名字
    public static final String PACKAGE_NAME = "com.bocaiweather.app";
    public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" +
            PACKAGE_NAME;
    public String cityID;
    public String cityName;
    private SQLiteDatabase database;
    private Context context;


    public DBManager(Context context) {
        this.context = context;
    }


    public SQLiteDatabase getDatabase() {
        return database;
    }


    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }


    public SQLiteDatabase openDatabase() {
        Log.e(TAG, DB_PATH + "/" + DB_NAME);
        this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
        return database;
    }


    //判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
    private SQLiteDatabase openDatabase(String dbfile) {

        try {
            if (!(new File(dbfile).exists())) {

                InputStream is = this.context.getResources().openRawResource(R.raw.citychina); //欲导入的数据库
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }

            return SQLiteDatabase.openOrCreateDatabase(dbfile, null);
        } catch (FileNotFoundException e) {
            Log.e("Database", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Database", "IO exception");
            e.printStackTrace();
        }
        return null;
    }


    public void closeDatabase() {
        this.database.close();
    }



    //获得对应城市的城市编号
    public String queryCityID(String cityName)
    {
        String sql = "select * from city_table where CITY =" + "'"
                + cityName + "'" + ";";
        SQLiteDatabase db = this.openDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        if (cursor != null) {
            cursor.moveToFirst();
            cityID = cursor.getString(cursor
                    .getColumnIndex("WEATHER_ID"));
          //  Toast.makeText(context,cityID , Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        return  cityID;
    }

    //获得对应编号的城市名
    public String queryCityName(String cityID)
    {
        String sql = "select * from city_table where WEATHER_ID =" + "'"
                + cityID + "'" + ";";
        SQLiteDatabase db = this.openDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        if (cursor != null) {
            cursor.moveToFirst();
            cityName = cursor.getString(cursor
                    .getColumnIndex("CITY"));
           // Toast.makeText(context,cityName , Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        return  cityName;
    }

}


