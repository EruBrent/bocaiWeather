package oyj.com.bocaibar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by oyj on 2016/4/27.
 */
public class NavSettingActivity extends AppCompatActivity
{
    @Override
    public void onCreate (Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.nav_setting);
        getSupportFragmentManager().beginTransaction().replace(R.id.setFragment,new SettingFragment()).commit();
    }
}


