package cn.wxxwwx98.mytestapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private long lastBack = 0;
    private TextView tv1;
    private SharedPreferences sharedPreferences = null;
    private String uid;
    private TabHost TB;

    private TextView Tab1TvUid,Tab1TvUName,Tab1TvAge,Tab1TvSex,Tab1TvRdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        GetUserInformation(uid);

    }
    private String GetUserSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");//(key,若无数据需要赋的值);
        uid = sharedPreferences.getString("uid", "");//(key,若无数据需要赋的值);
        return username;
    }

    public void GetUserInformation(final String Uid){
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        DBService dbService = new DBService();
                        try {
                            Connection conn = dbService.getConnection();
                            List<Map<String, Object>> list = dbService.execQuery("select * from user_information where UID = " + Uid , null);
                            System.out.println(list.size());
                            for (Map<String, Object> m : list) {
                                    Tab1TvUid.setText(m.get("UID").toString());
                                    Tab1TvUName.setText(m.get("Name").toString());
                                    Tab1TvAge.setText(m.get("Birthday").toString());
                                    Tab1TvSex.setText(m.get("Sex").toString());
                                    Tab1TvRdate.setText(m.get("RDate").toString());
                            }
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
