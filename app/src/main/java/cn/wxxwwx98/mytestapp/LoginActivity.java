package cn.wxxwwx98.mytestapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private EditText EtUsername,EtPassword;
    private SharedPreferences sharedPreferences;

    Context context = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        context = this.getApplicationContext();
        btnLogin = findViewById(R.id.Login);
        EtUsername = findViewById(R.id.Username);
        EtPassword = findViewById(R.id.Password);



    }

    @Override
    protected void onResume() {
        super.onResume();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if (!isNetworkAvailable(context)) {
                    Toast.makeText(LoginActivity.this,"你登录得连接网络啊orz",Toast.LENGTH_LONG).show();
                    return;}
                Login();
            }
        });
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
            String Username = m.get("Name").toString();
            String Password = m.get("Password").toString();
            System.out.println("\n"+Username + ":" + Password);

            if (username.equals(Username)) {
                if (username.equals(Username) && password.equals(Password)) {
                    System.out.println(m.get("Name") + "登录成功");
                    i = 1;
                    break;
                } else {
                    System.out.println(m.get("Name") + "密码错误");
                    i = -1;
                    break;
                    //MyToast(username+"密码错误");
                }
            } else {
                System.out.println("用户名错误");
                i = 0;
            }
        }
        return i;
    }
    /**
     *  登录的基本逻辑
     */
    private void Login() {
        new Thread(
                new Runnable() {
                    String username = EtUsername.getText().toString();
                    String password = EtPassword.getText().toString();
                    @Override
                    public void run() {
                        if (username.equals("")){MyToast("请输入用户名！！！"); return;}
                        if (password.length() < 6){MyToast("密码小于6位"); return;}
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
     * 再按一次退出程序
     */
    private long lastBack = 0;
    @Override
    public void onBackPressed(){
        if (lastBack == 0 || System.currentTimeMillis() - lastBack > 2000) {
            Toast.makeText(LoginActivity.this, "不登录就退出嘛?", Toast.LENGTH_SHORT).show();
            lastBack = System.currentTimeMillis();
            return;
        }
        super.onBackPressed();
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