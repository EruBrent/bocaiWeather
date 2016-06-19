package Util;

import android.app.Application;
import android.content.Context;

/**
 * Created by oyj
 */
public class myApplication extends Application
{
	public static Context BaseContext;
	public static OtherUtil otherUtil;
	@Override
	public void onCreate(){
		super.onCreate();
		BaseContext = getApplicationContext();
		otherUtil = new OtherUtil();
	}

}
