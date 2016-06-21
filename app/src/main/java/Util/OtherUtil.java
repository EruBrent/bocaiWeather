package util;

import android.content.Context;
import android.content.SharedPreferences;

import com.bocaiweather.app.Activity.DBManager;
import com.bocaiweather.app.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by oyj
 *
 */

public class OtherUtil
{
    private SharedPreferences sp ;
    private String ico_cityData = "ico_city.xml";

    public OtherUtil(){
        if (!new File(DBManager.DB_PATH +ico_cityData).exists()) {
            sp = myApplication.BaseContext.getSharedPreferences("ico_city",Context.MODE_PRIVATE);
            initIco_data();
        }
    }




    /**
     * 判断当前日期是星期几
     *
     * @param pTime 要判断的时间日期
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    public static String dayForWeek(String pTime) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));
        int dayForWeek = 0;
        String week = "";
        dayForWeek = c.get(Calendar.DAY_OF_WEEK);
        switch (dayForWeek) {
            case 1:
                week = "星期天";
                break;
            case 2:
                week = "星期一";
                break;
            case 3:
                week = "星期二";
                break;
            case 4:
                week = "星期三";
                break;
            case 5:
                week = "星期四";
                break;
            case 6:
                week = "星期五";
                break;
            case 7:
                week = "星期六";
                break;
        }
        return week;
    }

    private void initIco_data()
    {
      putIcoData("晴", R.mipmap.ds_clear_day);
      putIcoData("阴", R.mipmap.ds_cloudy_day_night);
      putIcoData("多云", R.mipmap.ds_fair_day);
      putIcoData("小雨", R.mipmap.ds_light_rain_day_night);
      putIcoData("中雨", R.mipmap.ds_freezing_rain_day_night);
      putIcoData("大雨", R.mipmap.ds_heavy_rain_day_night);
      putIcoData("雷阵雨", R.mipmap.ds_thundershowers_day_night);
      putIcoData("霾", R.mipmap.ds_haze_day_night);
      putIcoData("雾", R.mipmap.ds_fog_sun_day);
    }
    public void  putIcoData(String key, int value){
        sp.edit().putInt(key,value).apply();
    }

    public int getIconData(String key, int value){
        return sp.getInt(key, value);
    }

    public void saveCity(String key, String value) {
       sp.edit().putString(key, value).apply();

    }

    public String getCity(String key, String defValue) {
        return sp.getString(key, defValue);
    }
}
