package cn.wxxwwx98.mytestapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

public class StartActivity extends AppCompatActivity {
    private Handler handler = new Handler();

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // 注意：此处将setContentView()方法注释掉
//        // setContentView(R.layout.activity_start);
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                gotoLogin();
//            }
//        }, 2000);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //隐藏状态栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        // 设置没有标题栏
        getSupportActionBar().hide();

        RelativeLayout layoutSplash=(RelativeLayout) findViewById(R.id.activity_splash);
        AlphaAnimation alphaAnimation=new AlphaAnimation(0.1f,1.0f);
        alphaAnimation.setDuration(1000);//设置动画播放时长1000毫秒（1秒）
        layoutSplash.startAnimation(alphaAnimation);
        //设置动画监听

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            //动画结束
            @Override
            public void onAnimationEnd(Animation animation) {
                //页面的跳转
//                Intent intent=new Intent(StartActivity.this,MainActivity.class);
//                startActivity(intent);
//                finish();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gotoLogin();
                    }
                }, 1000);
//
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
    /**
     * 前往注册、登录主页
     */
    private void gotoLogin() {
        Intent intent = new Intent(StartActivity.this, FirstActivity.class);
        startActivity(intent);
        finish();
        //取消界面跳转时的动画，使启动页的logo图片与注册、登录主页的logo图片完美衔接
        overridePendingTransition(0, 0);
    }

    /**
     * 屏蔽物理返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (handler != null) {
            //If token is null, all callbacks and messages will be removed.
            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
}
