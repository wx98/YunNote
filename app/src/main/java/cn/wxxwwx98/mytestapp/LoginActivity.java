package cn.wxxwwx98.mytestapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private EditText EtUsername,EtPassword;
    private TextView Tv1;
    private SharedPreferences sharedPreferences = null;
    private long lastBack = 0;
    Context context = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //删除标题栏
        getSupportActionBar().hide();

        context = this.getApplicationContext();
        btnLogin = findViewById(R.id.Login);
        EtUsername = findViewById(R.id.Username);
        EtPassword = findViewById(R.id.Password);
        Tv1 = findViewById(R.id.tv1);
        Tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        //查找是否登录过如果登陆过则跳过登录界面
        if(GetUserSharedPreferences()){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        EtUsername.setText(GetUserName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if (!isNetworkAvailable(context)) {
                    Toast.makeText(LoginActivity.this,"你登录得连接网络啊orz",Toast.LENGTH_LONG).show();
                    return;
                }
                Login();
            }
        });
    }

    /**
     *  用于记录登录状态
     * @param username 用户名
     * @param password 密码
     */
    private void SetUserSharedPreferences(String Uid,String username,String password){
        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid",Uid);
        editor.putString("username", username);
        editor.putString("password", password);
        editor.commit();
    }

    /**
     *  如果已经登录过返回true
     *  未登陆过则返回false
     * @return
     */
    private boolean GetUserSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");//(key,若无数据需要赋的值)
        String password = sharedPreferences.getString("password", "");

        if(!username.equals("") && !password.equals(""))
            return true;
        return false;
    }

    private String GetUserName(){
        SharedPreferences sharedPreferences = getSharedPreferences("Ruser", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("RName", "");//(key,若无数据需要赋的值)
        return username;
    }
    /**
     * 使用用户输入的值对比从数据库中获取到的list
     *
     * @param list 查到的List<Map<String, Object>>
     * @param username 界面获取到的用户名
     * @param password 界面获取到的密码
     * @return  0:username未找到  1:登录成功，-1:密码错误
     */
    private int UserEquals(List<Map<String, Object>> list,String username,String password) {
        int i = 0;
        for (Map<String, Object> m : list) {
            //for (String k : m.keySet()){
            //  System.out.println(k + " : " + m.get(k)+"\t\t\t\t");
            //}
            //从当前List获取用户名和密码
            String Username = m.get("Name").toString();
            String Password = m.get("Password").toString();
            String Uid = m.get("UID").toString();
            //System.out.println("\n"+Username + ":" + Password);
            //对比用户名
            if (username.equals(Username)) {
                //对比密码是否正确
                if (username.equals(Username) && password.equals(Password)) {
                    //对比完全正确后调用下面的函数将username和password保存
                    SetUserSharedPreferences(Uid,username,password);
                    i = 1;
                    break;
                } else {
                    i = -1;
                    break;
                }
            } else {
                i = 0;
            }
        }
        return i;
    }

    /**
     *  登录的基本逻辑
     */
    private void Login() {
        //创建线程
        new Thread(
                new Runnable() {
                    //获取到用户输入的密码和用户名
                    String username = EtUsername.getText().toString();
                    String password = EtPassword.getText().toString();
                    @Override
                    public void run() {
                        //判断用户名和密码是否正确
                        if (username.equals("")){MyToast("请输入用户名！！！"); return;}
                        if (password.length() < 6){MyToast("密码小于6位"); return;}
                        //创建DBService对象查询表数据，调用UserEquals()是否可以登录，并执行结果动作
                        DBService dbService = new DBService();
                        try {
                            Connection conn = dbService.getConnection();
                            List<Map<String, Object>> list = dbService.execQuery("select * from user;", null);
                            int i = UserEquals(list, username, password);

                            if (i == 0) {
                                //System.out.println("用户名错误");
                                MyToast("'"+username + "'是错误用户名\n或者'"+username+"'户名不存在");
                            } else if (i == 1) {
                                Looper.prepare();
                                Toast.makeText(LoginActivity.this, username + ":登录成功", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                Looper.loop();
                            } else {
                                //System.out.println(username + "密码错误");
                                MyToast(username + ":的密码错误");
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
     *这个函数用于把Toast包含到线程里
     */
    private void MyToast(String text){
        Looper.prepare();
        Toast.makeText(LoginActivity.this,text,Toast.LENGTH_LONG).show();
        Looper.loop();
    }

    /**
     *  @Override
     *  再按一次退出程序
     */
    public void onBackPressed(){
        if (lastBack == 0 || System.currentTimeMillis() - lastBack > 2000) {
            Toast.makeText(LoginActivity.this, "不登录就退出嘛?", Toast.LENGTH_SHORT).show();
            lastBack = System.currentTimeMillis();
            return;
        }
        super.onBackPressed();
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
     *获得网络状态
     * @param context =  this.getApplicationContext()
     * @return turn有网络，flase:无网络
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
        } else {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null&&networkInfo.length>0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}