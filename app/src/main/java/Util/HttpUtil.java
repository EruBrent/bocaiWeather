package Util;

import android.os.Handler;
import android.util.Log;

import com.bocaiweather.app.Activity.MainActivity;
import com.bocaiweather.app.Activity.WeatherTest;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *Created by oyj
 * 作用：此类主要是用来处理APP网络方面的事项
 */

public class HttpUtil
{

    private OkHttpClient client = new OkHttpClient();
    private String result;
    private Gson gson = new Gson();
    public WeatherTest weather = new WeatherTest();
    Handler m_Handler = new Handler();

    public HttpUtil(String cityID) {getData(cityID);}

    //使用OKHttp + GSON 获取数据
    public  void getData(String cityID)
    {
        final Request request = new Request.Builder()
                .url("https://api.heweather.com/x3/weather?cityid=CN" +cityID+
                        "&key=088b2162b2d44596b9f22a458a8e41e3 ")
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {}

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                result =response.body().string().replace("HeWeather data service 3.0", "WeatherData");//替换掉数据中有空格的部分，不然不能解析
                weather = gson.fromJson(result,WeatherTest.class);
                Log.d("love",result);

                m_Handler.post(new Runnable()
                {
                    @Override
                    public void run() { MainActivity.recyclerView.getAdapter().notifyDataSetChanged();}

                });

            }



        });


    }
}

