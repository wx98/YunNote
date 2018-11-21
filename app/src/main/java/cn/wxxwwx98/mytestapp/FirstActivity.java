package cn.wxxwwx98.mytestapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class FirstActivity extends AppCompatActivity {
    private long exitTime;
    private Button tvRegister;
    private Button tvLogin;
    private ImageView ivLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
        initViews();
        initAnims();
    }

    /**
     * 初始化View控件
     */
    private void initViews() {
        tvLogin = findViewById(R.id.tv_login);
        tvRegister = findViewById(R.id.tv_register);
        ivLogo = findViewById(R.id.imageView);
        //查找是否登录过如果登陆过则跳过登录界面
        if(GetUserSharedPreferences()){
            Intent intent = new Intent(FirstActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
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

    /**
     * 初始化logo图片以及底部注册、登录的按钮动画
     */
    private void initAnims() {
        //初始化底部注册、登录的按钮动画
        //以控件自身所在的位置为原点，从下方距离原点200像素的位置移动到原点
        ObjectAnimator tranLogin = ObjectAnimator.ofFloat(tvLogin, "translationY", 200, 0);
        ObjectAnimator tranRegister = ObjectAnimator.ofFloat(tvRegister, "translationY", 200, 0);
        //将注册、登录的控件alpha属性从0变到1
        ObjectAnimator alphaLogin = ObjectAnimator.ofFloat(tvLogin, "alpha", 0, 1);
        ObjectAnimator alphaRegister = ObjectAnimator.ofFloat(tvRegister, "alpha", 0, 1);
        final AnimatorSet bottomAnim = new AnimatorSet();
        bottomAnim.setDuration(1000);
        //同时执行控件平移和alpha渐变动画
        bottomAnim.play(tranLogin).with(tranRegister).with(alphaLogin).with(alphaRegister);

        //获取屏幕高度
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        int screenHeight = metrics.heightPixels;

        //通过测量，获取ivLogo的高度
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        ivLogo.measure(w, h);
        int logoHeight = ivLogo.getMeasuredHeight();

        //初始化ivLogo的移动和缩放动画
        float transY = (screenHeight - logoHeight) * 0.28f;
        //ivLogo向上移动 transY 的距离
        ObjectAnimator tranLogo = ObjectAnimator.ofFloat(ivLogo, "translationY", 0, -transY);
        //ivLogo在X轴和Y轴上都缩放0.75倍
        ObjectAnimator scaleXLogo = ObjectAnimator.ofFloat(ivLogo, "scaleX", 1f, 0.75f);
        ObjectAnimator scaleYLogo = ObjectAnimator.ofFloat(ivLogo, "scaleY", 1f, 0.75f);
        AnimatorSet logoAnim = new AnimatorSet();
        logoAnim.setDuration(3000);
        logoAnim.play(tranLogo).with(scaleXLogo).with(scaleYLogo);
        logoAnim.start();
        logoAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //待ivLogo的动画结束后,开始播放底部注册、登录按钮的动画
                bottomAnim.start();
            }
        });
    }


    /**
     * 重写返回键，实现双击退出效果
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(FirstActivity.this, " 不登录就退出嘛?", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                FirstActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
