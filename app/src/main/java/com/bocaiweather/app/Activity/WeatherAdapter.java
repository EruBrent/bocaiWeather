package com.bocaiweather.app.Activity;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bocaiweather.app.R;

/**
 * Created by oyj
 *
 * */


public class WeatherAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private String []data;
    private final int TYPE_ONE = 0;
    private final int TYPE_TWO = 1;
    private final int TYPE_THREE = 2;
    private final int TYPE_FORE = 3;
    private Context context;
    private Context mAContext;



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(viewType==TYPE_ONE)
            return new firstItem(LayoutInflater.from(context).inflate(R.layout.weather_first_item,parent,false));
        if(viewType==TYPE_TWO)
            return new secondItem(LayoutInflater.from(context).inflate(R.layout.weather_second_item,parent,false));
        if(viewType==TYPE_THREE)
            return new thirdItem(LayoutInflater.from(context).inflate(R.layout.weather_third_item,parent,false));
        return null;
    }




    //将数据与viewHolder绑定
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        if(holder instanceof firstItem){
            try{
                if(MainActivity.httpUtil.weather!=null)
                {
                    for(int i = 0;i<MainActivity.httpUtil.weather.weatherData.size();i++)
                        ((firstItem)holder).text.setText(MainActivity.httpUtil.weather.weatherData.get(i).now.tmp+"℃");
                }
            }catch (Exception e){e.printStackTrace();}


        }

        if (holder instanceof secondItem){
            ((secondItem)holder).text.setText("24小时天气预报");
        }

        if (holder instanceof thirdItem){
            ((thirdItem)holder).Bodily_sensation_value.setText("15");
        }
    }



    public WeatherAdapter(Context context) {this.context = context;}

    @Override
    public int getItemCount(){return 3;}

    @Override
    public int getItemViewType(int position) {
        if (position == TYPE_ONE) {
            return TYPE_ONE;
        }
        if (position == TYPE_TWO) {
            return TYPE_TWO;
        }
        if (position == TYPE_THREE) {
            return TYPE_THREE;
        }
        if (position == TYPE_FORE) {
            return TYPE_FORE;
        }
        return super.getItemViewType(position);
    }


    /**作用：在这里，我们通过自定义ViewHolder，来提前获得listView中每一个item中所用元素的引用
     这样可以加快listView的滑动速度。*/
    public static class  mHolder extends RecyclerView.ViewHolder
    {
        protected TextView txt;
        private mHolder(View v){
            super(v);
            txt =(TextView)v.findViewById(R.id.textView);
        }
    }

    //第一个Item的ViewHolder
    public static class firstItem extends RecyclerView.ViewHolder{
        private  TextView text;
        public   firstItem(View v){
            super(v);
            text = (TextView)v.findViewById(R.id.temperature);
        }
    }


    public static class secondItem extends RecyclerView.ViewHolder{
        private TextView text;
        private ImageView image;

        public  secondItem(View v){
            super(v);
            text = (TextView)v.findViewById(R.id.label);
            image = (ImageView)v.findViewById(R.id.divider);
        }
    }

    public static class thirdItem extends RecyclerView.ViewHolder{
        private ImageView image;
        private TextView Bodily_sensation_value;
        public  thirdItem(View v){
            super(v);
            image= (ImageView)v.findViewById(R.id.sun_location);
            Bodily_sensation_value = (TextView)v.findViewById(R.id.Bodily_sensation_value);
        }
    }

}
