/*
package Util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

*/
/**
 *1
 *//*

public class HttpUtil
{

    public static void sendHttpRequest(final String address,final HttpCallBackListener listener)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                HttpURLConnection connection = null;
                try
                {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();//获取服务器返回的数据
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder resopnse = new StringBuilder();
                    String line;
                    while ((line = reader.readLine())!= null)
                        resopnse.append(line);
                    if(listener != null)
                        listener.onFinish(resopnse.toString());
                }catch (Exception e){
                    if (listener != null)
                        listener.onError(e);
                }finally {
                    if (connection != null)
                        connection.disconnect();
                }

            }

        }).start();
    }
}
*/
