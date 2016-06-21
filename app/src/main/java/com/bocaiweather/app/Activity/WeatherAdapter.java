package com.bocaiweather.app.Activity;


import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bocaiweather.app.R;
import com.bumptech.glide.Glide;

import util.myApplication;

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

    private Animation animation;
    private  int int_wind=1;


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
            return new fourthItem(LayoutInflater.from(context).inflate(R.layout.weather_fourth_item, parent, false));
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


/**----------------------------我的名字是分割线------------------------------------------------*/
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
                                        setText(myApplication.otherUtil.dayForWeek(temp.dailyForecast.get(i).date));

                        for (WeatherTest.WeatherData temp : MainActivity.httpUtil.weather.weatherData)
                        {
                            ((secondItem) holder).forecastMin[i].
                                    setText(temp.dailyForecast.get(i).tmp.min);
                            ((secondItem) holder).forecastMax[i].
                                    setText(temp.dailyForecast.get(i).tmp.max);
                            //用Glideg更新图片
                            Glide.with(context)
                                 .load(myApplication.otherUtil.
                                            getIconData(temp.dailyForecast.get(i).cond.txtD, R.drawable.sadcloud))
                                 .into(((secondItem)holder).forecastImage[i]);
                        }

                    }
                }
            } catch (Exception e) {e.printStackTrace();}
        }


/**----------------------------我的名字是分割线------------------------------------------------*/
        if (holder instanceof thirdItem)
        {
            try
            { if (MainActivity.httpUtil.weather != null)
                {
                    for (WeatherTest.WeatherData temp : MainActivity.httpUtil.weather.weatherData)
                    {
                        ((thirdItem) holder).bodily_sensation_value.setText(temp.now.fl+"℃" );
                        ((thirdItem) holder).humidity_value.setText(temp.now.hum + "%");
                        ((thirdItem) holder).barometricPressure_value.setText(temp.now.pres + "mbar");
                        ((thirdItem) holder).visibility_value.setText(temp.now.vis + "km");
                    }
                 }
            }catch (Exception e){e.printStackTrace();}
        }

/**----------------------------我的名字是分割线------------------------------------------------*/
        if (holder instanceof fourthItem)
        {
            try
            {
                if (MainActivity.httpUtil.weather != null)
                {
                    for (WeatherTest.WeatherData temp:MainActivity.httpUtil.weather.weatherData)
                    {
                        ((fourthItem) holder).wind_velocity.setText("风速: " + temp.now.wind.spd+"km/h" );
                        int_wind = Integer.parseInt(temp.now.wind.spd);
                    }
                    int temp_value=int_wind/10; //如果直接给animation使用，那么将会使得出来的值不是359的倍数，从而导致出现卡顿效果，所以在这里先把它转化为整数
                    animation = new RotateAnimation(0f,359f*temp_value,
                            Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                    animation.setDuration(3000); //转动持续时间
                    animation.setRepeatCount(ValueAnimator.INFINITE); //设置为永不停止模式
                    animation.setInterpolator(new LinearInterpolator()); //设置为匀速转动
                    ((fourthItem)holder).Blade.startAnimation(animation);
                }
            }catch (Exception e){e.printStackTrace();}
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
        private TextView[] forecastMin = new TextView[7]; //未来几天的最低温度
        private TextView[] forecastMax = new TextView[7];
        private TextView textView;
        private ImageView[] forecastImage = new ImageView[7];

        public secondItem(View v)
        {
            super(v);
            forecastLayout = (LinearLayout) v.findViewById(R.id.day1);

            for (int i = 0; i < 7; i++)
            {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View view = layoutInflater.inflate(R.layout.forecast, null);
                forecastWeek[i] = (TextView) view.findViewById(R.id.forecast_week);
                forecastMin[i] = (TextView)view.findViewById(R.id.forecast_mintemperature);
                forecastMax[i] = (TextView)view.findViewById(R.id.forecast_maxtemperature);
                forecastImage[i] = (ImageView) view.findViewById(R.id.forecast_image);
                forecastLayout.addView(view);
            }
            forecastLayout.setVisibility(View.VISIBLE);

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
        private ImageView Blade;

        public fourthItem(View v)
        {
            super(v);
            wind_velocity = (TextView) v.findViewById(R.id.Wind_velocity);
           Blade = (ImageView)v.findViewById(R.id.blade);
        }
    }

}
