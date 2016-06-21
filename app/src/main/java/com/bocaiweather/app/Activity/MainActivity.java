package com.bocaiweather.app.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bocaiweather.app.R;

import util.HttpUtil;
import util.ImageUtil;
import util.myApplication;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private ImageUtil imageUtil;
    private Toolbar toolbar;
    private ImageView image;
    private TextView toolBarTitle;
    private ImageView blurImage;
    private String cityid = "101010100";
    public View errorLayout;
    private NavigationView navigationView;
    private DBManager dbManager = new DBManager(this);
    public static HttpUtil httpUtil = new HttpUtil();
    public static WeatherAdapter adapter;
    public Handler mHandler = new Handler();
    public SwipeRefreshLayout refreshLayout;
    public static RecyclerView recyclerView;

    public String locationCity;

    //public  OtherUtil otherUtil = new OtherUtil();

    public void MainActivity (){}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏


        setContentView(R.layout.activity_main);
        initView();
        toolbar.setTitle("");
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
        adapter = new WeatherAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));//这里表明用线性显示
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new RecyclerViewListener());
       if(!checkNetwork()){ networkError();}
        else {
           new  Thread(new checkLocationInfo()).start();
       }
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                refreshLayout.setRefreshing(true);
                if(checkNetwork()){networkOK();httpUtil.getData(cityid);}
                else {networkError();}
                new  Thread(new myThread()).start();
            }
        });
        myApplication.baiduLocate.stop();
    }



    private void initView()
    {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        image = (ImageView)findViewById(R.id.testImage);
        blurImage = (ImageView)findViewById(R.id.blured_image);
        errorLayout = (View)findViewById(R.id.networkErrorLayout);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperfresh);
        toolBarTitle = (TextView)findViewById(R.id.toolbar_title);
        imageUtil = new ImageUtil();

    }

/**----------------------------我的名字是分割线------------------------------------------------*/
    //下面这个方法的作用是为了让menu在DrawerLayout显示出来
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);

        // 关联检索配置和SearchView   http://hukai.me/android-training-course-in-chinese/ux/search/setup.html
        final SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("请输城市名");


/**----------------------------我的名字是分割线------------------------------------------------*/
        //监听searchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){
               // Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                searchView.clearFocus();
                 cityid = dbManager.queryCityID(query); //查询数据库，获取城市代号
                 if (cityid!=null){
                     httpUtil.getData(cityid);
                     myApplication.otherUtil.saveCity("城市",cityid);
                   toolBarTitle.setText(query);
                     searchView.setIconifiedByDefault(true);
                 }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {return false;}
        });
        return true;
    }

/**----------------------------我的名字是分割线------------------------------------------------*/
    //与上面的 onCreateOptionsMenu（）区别请看：http://blog.csdn.net/qq_23547831/article/details/50483837
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {return true;}

/**----------------------------我的名字是分割线------------------------------------------------*/
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

           /* Intent intent = new Intent(MainActivity.this,NavSettingActivity.class);
            startActivity(intent);*/
        } else if (id == R.id.nav_share) {
            //.makeText(this,"you click nav_share",Toast.LENGTH_SHORT).show();
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
                toolbar.hideOverflowMenu();
            } else if (dy < 0) {
                alpha = 0.0f;
            }
            blurImage.setAlpha(alpha);
        }
    }


    /**作用：告诉程序当网络出现错误时，应该做的事情*/
    private void networkError(){
        recyclerView.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
    }

    /**作用：告诉程序有网络应该怎么做*/
    private void networkOK(){
        errorLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

/**----------------------------我的名字是分割线------------------------------------------------*/
    public class myThread  implements Runnable{
        @Override
        public void run(){
            mHandler.post(new Runnable() {
                @Override
                public void run()
                {
                    recyclerView.getAdapter().notifyDataSetChanged();
                    refreshLayout.setRefreshing(false);
                }
            });
        }
    }

    public class checkLocationInfo implements Runnable{
        @Override
        public void run(){
            while (myApplication.baiduLocate.cityName!=null){
                mHandler.post(new Runnable() {
                    @Override
                    public void run()
                    {
                        locationCity = dbManager.queryCityID(myApplication.baiduLocate.cityName);
                        myApplication.otherUtil.saveCity("城市",locationCity);
                        cityid = myApplication.otherUtil.getCity("城市",locationCity);//先看看有没有默认城市，如果没有就用定位到的城市
                        httpUtil.getData(cityid);
                        toolBarTitle.setText(dbManager.queryCityName(cityid));
                        imageUtil.applyBlur(image,blurImage);
                    }
                });
                    break;
            }
        }
}

    /**检测网络情况*/
    private boolean checkNetwork(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network[] network = cm.getAllNetworks();
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(network!=null&&networkInfo!=null)
            return true;
        return false;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

    }
}




