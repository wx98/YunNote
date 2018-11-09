package cn.wxxwwx98.mytestapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    //声明控件
    private Button btnRegister;
    private EditText UserName,Password1,Password2;
    private RadioGroup RG;
    private TextView tv;
    //声明全局变量
    private SharedPreferences sharedPreferences = null;
    String _UserName = null;
    String _Sex = null;
    String _Password1 = null;
    String _Password2 = null;
    String _NowDate = null;
    String _Birthday = null;
    boolean validateUserName = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //删除标题栏
        getSupportActionBar().hide();
        //隐藏状态栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        tv = findViewById(R.id.txtBitthday);
        RG = findViewById(R.id.RG1);
        btnRegister = findViewById(R.id.Register);
        UserName = findViewById(R.id.txtUsername);
        Password1 = findViewById(R.id.txtPassword1);
        Password2 = findViewById(R.id.txtPassword2);
        UserName.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    getValues();
                    validateUserName(_UserName);
                } else {
                    // 此处为失去焦点时的处理内容
                    getValues();
                    validateUserName(_UserName);
                }
            }
        });
        UserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getValues();
                validateUserName(_UserName);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    protected void onResume() {
        super.onResume();
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                View view = LayoutInflater.from(RegisterActivity.this).inflate(R.layout.layout_datetime,null);

                Button btnLoglin = view.findViewById(R.id.btnGetDt);
                final DatePicker dt = view.findViewById(R.id.dt);

                btnLoglin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(MainActivity.this, "Test",Toast.LENGTH_LONG).show();

                        _Birthday = dt.getYear()+"-"+dt.getMonth()+"-"+dt.getDayOfMonth();
                        tv.setText(_Birthday);
                    }
                });
                builder.setTitle("选择日期")
                        .setView(view)
                        .show();
            }
        });
        //获取性别
        RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = group.findViewById(checkedId);
                _Sex = radioButton.getText().toString().trim();
                Toast.makeText(RegisterActivity.this, _Sex,Toast.LENGTH_LONG).show();
            }
        });
        //注册的点击事件
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValues();
                //for (int i = 0;i < 2;i++)
                validateUserName(_UserName);
                if(validate()){
                    Register();
                    SetUserName(_UserName);
                }

            }
        });
    }
    /**
     *  获得表单的值
     */
    public void getValues(){
        //获取用户名
        _UserName = UserName.getText().toString().trim();


        //密码获取
        _Password1 = Password1.getText().toString().trim();
        _Password2 = Password2.getText().toString().trim();

        //获取当前系统时间系统日期
        Calendar calendar = Calendar.getInstance();
        int NYear = calendar.get(Calendar.YEAR);//年
        int NMonth = calendar.get(Calendar.MONTH)+1;//月
        int NDay = calendar.get(Calendar.DAY_OF_MONTH);//日
        _NowDate = NYear+"-"+NMonth+"-"+NDay;
    }
    /**
     *   验证表单内的数据是否合法
     */
    public boolean validate() {
        getValues();
        //此处调用函数验证数据库用户名不能重复
        if (!validateUserName){
            MyToast("用户名已经有");
            //Toast.makeText(RegisterActivity.this,"用户名已经有！",Toast.LENGTH_LONG).show();
            return false;
        }

        //验证密码合法性
        if (_Password1.length() < 6){
            MyToast("密码不能小于6位");
            //Toast.makeText(RegisterActivity.this,"密码不能小于6位！",Toast.LENGTH_LONG).show();
            return false;
        }
        if (!_Password1.equals(_Password2)){
            MyToast("两次密码输入不同");
            //Toast.makeText(RegisterActivity.this,"两次密码输入不同！",Toast.LENGTH_LONG).show();
            return false;
        }

        //验证生日合法性
        if(_Birthday == null){
            MyToast("您没有选择您的生日");
            //Toast.makeText(RegisterActivity.this,"您没有选择您的生日！",Toast.LENGTH_LONG).show();
            return false;
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if(dateFormat.parse(_Birthday).getTime() > dateFormat.parse(_NowDate).getTime()){
                MyToast("生日选择错误");
               // Toast.makeText(RegisterActivity.this,"生日选择错误",Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 查询数据库中的用户名并设置到UserList上
     */
    private void validateUserName(final String UserName) {
        //创建线程
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        DBService dbService = new DBService();
                        try {
                            Connection conn = dbService.getConnection();
                            List<Map<String, Object>> list = dbService.execQuery("select Name from user where Name = \"" + UserName + "\"", null);
                            if (list.size() == 0){
                                validateUserName = true;
                            }else {
                                validateUserName = false;
                            }
                            System.out.println(list.size());
                            System.out.println(validateUserName+"");
                            for (Map<String, Object> m : list) {
                                for (String k : m.keySet()){
                                  System.out.println(k + " : " + m.get(k)+"\t\t\t\t");
                                }
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
     * 注册逻辑
     */
    private void Register(){
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {

                        //创建DBService对象查询表数据，调用UserEquals()是否可以登录，并执行结果动作
                        DBService dbService = new DBService();
                        try {
                            Connection conn = dbService.getConnection();
                            String sql1 = "INSERT INTO `user`(`Name`,`Password`) values(\""+_UserName+"\",\""+_Password1+"\");";
                            String sql2 = "INSERT INTO `udata` (`UID`,`Sex`,`Birthday`,`RDate`) values ((select UID from user where Name = '"+_UserName+"'),'"+_Sex+"','"+_Birthday+"','"+_NowDate+"');";
                            if(dbService.execUpdate(sql1,null) > 0 && dbService.execUpdate(sql2,null) > 0){
                                MyToast(_UserName+":注册成功");
                            }else  MyToast(_UserName+":注册失败");

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
     *  用于记录注册的用户名
     * @param username 用户名
     */
    private void SetUserName(String username){
        sharedPreferences = getSharedPreferences("Ruser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("RName", username);
        editor.commit();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     *这个函数用于把Toast包含到线程里
     */
    private void MyToast(String text){
        Looper.prepare();
        Toast.makeText(RegisterActivity.this,text,Toast.LENGTH_LONG).show();
        Looper.loop();
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
}
