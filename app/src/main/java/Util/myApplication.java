package util;

import android.app.Application;
import android.content.Context;

/**
 * Created by oyj
 */
public class myApplication extends Application
{
	public static Context BaseContext;
	public static OtherUtil otherUtil;
	public static BaiduLocate baiduLocate;

	@Override
	public void onCreate(){
		super.onCreate();
		BaseContext = getApplicationContext();
		otherUtil = new OtherUtil();
		baiduLocate = new BaiduLocate();
	}

}
