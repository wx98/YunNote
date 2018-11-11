package cn.wxxwwx98.mytestapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements TabHost.OnTabChangeListener {
    private Context context = MainActivity.this;
    public static List<Map<String, Object>> list;
    private long lastBack = 0;
    static public String uid;
    private String username;
    static String s1;
    static String s2;
    static String s3;
    static String s4;
    static String s5;
    private TabHost TB;
    private TextView TvAdd;
    private ListView listview;
    private TextView Tab1TvUid,Tab1TvUName,Tab1TvAge,Tab1TvSex,Tab1TvRdate;
    private Button Tab1BtnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getSupportActionBar().hide();
        TB = findViewById(R.id.tabhost);
        TB.setup();
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        layoutInflater.inflate(R.layout.layout_tab1, TB.getTabContentView());
        layoutInflater.inflate(R.layout.layout_tab2, TB.getTabContentView());
        TB.addTab(TB.newTabSpec("tab1").setIndicator("tab1").setContent(R.id.LinearLayout1));
        TB.addTab(TB.newTabSpec("tab2").setIndicator("tab2").setContent(R.id.LinearLayout2));

        Tab1TvUid = findViewById(R.id.UID);
        Tab1TvUName = findViewById(R.id.UserName);
        Tab1TvAge = findViewById(R.id.UAge);
        Tab1TvSex = findViewById(R.id.USex);
        Tab1TvRdate = findViewById(R.id.Rdate);
        Tab1BtnExit = findViewById(R.id.btnExit);
        TvAdd = findViewById(R.id.Add);
        listview = findViewById(R.id.list);
        if(GetUserSharedPreferences()){
            // GetNote();
            GetUserInformation(uid);
        }
    }

    /**
     *
     */
    protected void onResume(){
        super.onResume();
        MyListAdapter.GetNote(uid);
        TvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });
        Tab1BtnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clear();
            }
        });

        listview.setAdapter(new MyListAdapter(context));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MyListAdapter myListAdapter = new MyListAdapter(context);
                int x = Integer.parseInt(myListAdapter.getItem(i).toString().trim());
                SharedPreferences sharedPreferences = getSharedPreferences("nData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Ndata", list.get(i).get("Ndata").toString().trim());
                editor.putString("Title", list.get(i).get("Title").toString().trim());
                editor.putString("Data", list.get(i).get("Data").toString().trim());
                Toast.makeText(MainActivity.this,"点击pos:"+i+":"+myListAdapter.getItem(i),Toast.LENGTH_SHORT).show();
                editor.commit();
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this,"长按pos:"+i,Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        TB.setOnTabChangedListener(this);
        GetNote(uid);
    }

    /**
     * TabHost的监听事件
     * @param tabId
     */
    @Override
    public void onTabChanged(String tabId) {
        onResume();
    }
    /**
     *  取缓存中的Username和Uid
     * @return 如果不为空返回 true，为空返回 false
     */
    private boolean GetUserSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");//(key,若无数据需要赋的值);
        uid = sharedPreferences.getString("uid", "");//(key,若无数据需要赋的值);
        if (!uid.equals("") && !username.equals(""))
            return true;
        return false;
    }

    /**
     *  清除缓存并跳转
     * @return
     */
    private boolean Clear(){
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences1 = getSharedPreferences("Ruser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor.clear();
        editor1.putString("RName", username);
        editor1.commit();
        editor.commit();
        if (sharedPreferences.getString("uid", "").equals("")){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            Toast.makeText(MainActivity.this,username+"已经退出",Toast.LENGTH_LONG).show();
            finish();
            return true;
        }
        return false;
    }
    /**
     *  根据 uid 加载用户信息
     * @param Uid
     */
    public void GetUserInformation(final String Uid){
        final Handler handler = new Handler();
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        DBService dbService = new DBService();
                        try {
                            Connection conn = dbService.getConnection();
                            List<Map<String, Object>> list = dbService.execQuery("select * from user_information where UID = " + Uid , null);
                            s1 = list.get(0).get("UID").toString();
                            s2 = list.get(0).get("Name").toString();
                            s3 = list.get(0).get("Birthday").toString();
                            s4 = list.get(0).get("Sex").toString();
                            s5 = list.get(0).get("RDate").toString();
                            Calendar calendar = Calendar.getInstance();
                            int BYear = Integer.parseInt(s3.substring(0,4));
                            int NYear =  calendar.get(Calendar.YEAR);//年
                            final int i = (NYear - BYear);
                            handler.post(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            Tab1TvUid.setText(s1);
                                            Tab1TvUName.setText(s2);
                                            Tab1TvAge.setText(i+"");
                                            Tab1TvSex.setText(s4);
                                            Tab1TvRdate.setText(s5);
                                        }
                                    }
                            );
                            //关闭数据库对象
                            dbService.close(null, null, conn);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
    }

    public static void GetNote(final String Uid){
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        DBService dbService = new DBService();
                        try {
                            Connection conn = dbService.getConnection();
                            List<Map<String, Object>> list1 = dbService.execQuery("select * from ndata where UID = " + Uid , null);
                            list = list1;
                            //关闭数据库对象
                            dbService.close(null, null, conn);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
    }

    /**
     *  状态栏沉浸
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged (boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    /**
     * 再按一次退出程序
     */
    @Override
    public void onBackPressed(){
        if (lastBack == 0 || System.currentTimeMillis() - lastBack > 2000) {
            Toast.makeText(MainActivity.this, "再按一次返回退出程序", Toast.LENGTH_SHORT).show();
            lastBack = System.currentTimeMillis();
            return;
        }
        super.onBackPressed();
    }


}
