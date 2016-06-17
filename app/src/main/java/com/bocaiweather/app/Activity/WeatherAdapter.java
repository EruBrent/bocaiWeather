package com.bocaiweather.app.Activity;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bocaiweather.app.R;

import Util.OtherUtil;

/**
 * Created by oyj
 * 作用：这个类主要是用来初始化RecyclerView的
 */


public class WeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private String[] data;
    private final int TYPE_ONE = 0;
    private final int TYPE_TWO = 1;
    private final int TYPE_THREE = 2;
    private final int TYPE_FORE = 3;
    private static Context context;
    private OtherUtil otherUtil = new OtherUtil();


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == TYPE_ONE)
            return new firstItem(LayoutInflater.from(context).inflate(R.layout.weather_first_item, parent, false));
        if (viewType == TYPE_TWO)
            return new secondItem(LayoutInflater.from(context).inflate(R.layout.weather_second_item, parent, false));
        if (viewType == TYPE_THREE)
            return new thirdItem(LayoutInflater.from(context).inflate(R.layout.weather_third_item, parent, false));
        if (viewType == TYPE_FORE)
            return new thirdItem(LayoutInflater.from(context).inflate(R.layout.weather_fourth_item, parent, false));
        return null;
    }


    //将数据与viewHolder绑定
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if (holder instanceof firstItem)
        {
            try
            {
                if (MainActivity.httpUtil.weather != null)
                {
                    for (int i = 0; i < MainActivity.httpUtil.weather.weatherData.size(); i++)
                    {
                        ((firstItem) holder).Temperature.setText(MainActivity.
                                httpUtil.weather.weatherData.get(i).now.tmp + "℃");
                        ((firstItem) holder).temp_low_value.setText(MainActivity.
                                httpUtil.weather.weatherData.get(i).dailyForecast.get(i).tmp.min);
                        ((firstItem) holder).temp_high_value.setText(MainActivity.
                                httpUtil.weather.weatherData.get(i).dailyForecast.get(i).tmp.max);
                    }

                }
            } catch (Exception e) {e.printStackTrace();}


        }

        if (holder instanceof secondItem)
        {
            try
            {
                if (MainActivity.httpUtil.weather != null)
                {
                    ((secondItem) holder).forecastWeek[0].setText("今日");
                    ((secondItem) holder).forecastWeek[1].setText("明日");
                    for (int i = 0; i < 7; i++)
                    {
                        if (i > 1)

                            for (WeatherTest.WeatherData temp : MainActivity.httpUtil.weather.weatherData)
                                ((secondItem) holder).forecastWeek[i].
                                        setText(otherUtil.dayForWeek(temp.dailyForecast.get(i).date));

                    }

                }

            } catch (Exception e) {e.printStackTrace();}

        }

        if (holder instanceof thirdItem)
        {
            // ((thirdItem)holder).bodily_sensation_value.setText("15");
        }

        if (holder instanceof fourthItem)
        {
            if (MainActivity.httpUtil.weather != null)
            {
                for (int i = 0; i < MainActivity.httpUtil.weather.weatherData.size(); i++)
                {
                    ((fourthItem) holder).wind_velocity.setText(MainActivity.
                            httpUtil.weather.weatherData.get(i).now.wind.spd + "km/h");
                }
            }
        }
    }


    public WeatherAdapter(Context context) {this.context = context;}

    @Override
    public int getItemCount() {return 4;}

    @Override
    public int getItemViewType(int position)
    {
        if (position == TYPE_ONE)
        {
            return TYPE_ONE;
        }
        if (position == TYPE_TWO)
        {
            return TYPE_TWO;
        }
        if (position == TYPE_THREE)
        {
            return TYPE_THREE;
        }
        if (position == TYPE_FORE)
        {
            return TYPE_FORE;
        }
        return super.getItemViewType(position);
    }


    /**
     * 作用：在这里，我们通过自定义ViewHolder，来提前获得listView中每一个item中所用元素的引用
     * 这样可以加快listView的滑动速度。
     */

    //第一个Item的ViewHolder
    public static class firstItem extends RecyclerView.ViewHolder
    {
        private TextView Temperature;
        private TextView temp_low_value;
        private TextView temp_high_value;

        public firstItem(View v)
        {
            super(v);
            Temperature = (TextView) v.findViewById(R.id.temperature);
            temp_low_value = (TextView) v.findViewById(R.id.temp_low);
            temp_high_value = (TextView) v.findViewById(R.id.temp_high);
        }
    }


    public static class secondItem extends RecyclerView.ViewHolder
    {
        private LinearLayout forecastLayout;
        private TextView[] forecastWeek = new TextView[7];
        private TextView textView;
        private ImageView image;

        public secondItem(View v)
        {
            super(v);
            forecastLayout = (LinearLayout) v.findViewById(R.id.day1);

            for (int i = 0; i < 7; i++)
            {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View view = layoutInflater.inflate(R.layout.forecast, null);
                forecastWeek[i] = (TextView) view.findViewById(R.id.forecast_week);
                forecastLayout.addView(view);
            }
            forecastLayout.setVisibility(View.VISIBLE);
            image = (ImageView) v.findViewById(R.id.divider);
        }
    }

    public static class thirdItem extends RecyclerView.ViewHolder
    {
        private ImageView image;
        private TextView bodily_sensation_value;
        private TextView humidity_value;
        private TextView barometricPressure_value;
        private TextView visibility_value;

        public thirdItem(View v)
        {
            super(v);
            image = (ImageView) v.findViewById(R.id.sun_location);
            bodily_sensation_value = (TextView) v.findViewById(R.id.Bodily_sensation_value);
            humidity_value = (TextView) v.findViewById(R.id.Humidity_value);
            barometricPressure_value = (TextView) v.findViewById(R.id.BarometricPressure_value);
            visibility_value = (TextView) v.findViewById(R.id.Visibility_value);
        }
    }

    public static class fourthItem extends RecyclerView.ViewHolder
    {
        private TextView wind_velocity;

        public fourthItem(View v)
        {
            super(v);
            wind_velocity = (TextView) v.findViewById(R.id.Wind_velocity);
        }
    }

}
