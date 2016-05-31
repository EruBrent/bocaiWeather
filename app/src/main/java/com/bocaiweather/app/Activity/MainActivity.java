package com.bocaiweather.app.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bocaiweather.app.R;

import Util.HttpUtil;
import Util.ImageUtil;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private DBManager dbManager = new DBManager(this);
    private ImageUtil imageUtil;
    private RecyclerView recyclerView;
    private ImageView image;
    private ImageView blurImage;
    private Toolbar toolbar;
    private String cityid;
    private NavigationView navigationView;
    private HttpUtil httpUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

        setContentView(R.layout.activity_main);

        image = (ImageView)findViewById(R.id.testImage);
        blurImage = (ImageView)findViewById(R.id.blured_image);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        /*ToolBar与DrawerLayout绑定*/
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {super.onDrawerOpened(drawerView);}
            @Override
            public void onDrawerClosed(View drawerView) {super.onDrawerClosed(drawerView);}
        };
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        drawer.setDrawerListener(toggle);
        WeatherAdapter adapter = new WeatherAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));//这里表明用线性显示
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new RecyclerViewListener());

        imageUtil = new ImageUtil(image,recyclerView,blurImage);
        httpUtil = new HttpUtil("101010100");
    }



    //下面这个方法的作用是为了让menu在DrawerLayout显示出来
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);

        // 关联检索配置和SearchView   http://hukai.me/android-training-course-in-chinese/ux/search/setup.html
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("请输城市名");

        //监听searchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){
                Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                searchView.clearFocus();
                 cityid = dbManager.queryCityID(query); //查询数据库，获取城市代号
                 if (cityid!=null){
                     httpUtil.getData(cityid);
                 }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {return false;}
        });
        return true;
    }


    //与上面的 onCreateOptionsMenu（）区别请看：http://blog.csdn.net/qq_23547831/article/details/50483837
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {return true;}


    //onBackPressed()作用为：当我们按下返回键时，如果DrawerLayout是打开的，则关闭它
    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean  onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_setting) {

            Intent intent = new Intent(MainActivity.this,NavSettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Toast.makeText(this,"you click nav_share",Toast.LENGTH_SHORT).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**监听RecyclerView上下滑动，从而改变图片透明值，从而实现虚化效果*/
    public class RecyclerViewListener extends RecyclerView.OnScrollListener
    {
        private float alpha;
        @Override
        public void  onScrolled(RecyclerView recyclerView, int dx, int dy)
        {
            super.onScrolled(recyclerView, dx, dy);

            //大于0表示在上滑，小于0表示在下滑
            if (dy>0) {
                alpha = 1.0f;
            } else if (dy < 0) {
                alpha = 0.0f;
            }
            blurImage.setAlpha(alpha);
        }
    }
}




