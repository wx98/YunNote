package cn.wxxwwx98.mytestapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {
    static String uid;

    private TextView BtnSave,BtnBack;
    private EditText EtTitle,EtNote;
    public String _NTitle;
    public String _Note = null;
    public String _NDate = null;
    String _NData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        this.getSupportActionBar().hide();
        BtnSave = findViewById(R.id.Save);
        BtnBack = findViewById(R.id.Back);
        EtNote = findViewById(R.id.Note);
        EtTitle = findViewById(R.id.Title);
        GetUserUid();
        GetNote();
        EtNote.setText(_Note);
        EtTitle.setText(_NTitle);
        BtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (GetNote() && validate()){
                UpdataNote();
            }else if(validate()){
                PushaNote();
            }
            }
        });
        BtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNoteActivity.this, MainActivity.class);
                startActivity(intent);
                MyClear();
                finish();
            }
        });
    }
    private void getValues(){
        _NTitle = EtTitle.getText().toString().trim();
        _Note = EtNote.getText().toString().trim();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        _NDate = simpleDateFormat.format(date);
    }
    private boolean GetUserUid(){
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "");//(key,若无数据需要赋的值);
        if (!uid.equals(""))
            return true;
        return false;
    }
    public boolean validate(){
        getValues();
        if(_NTitle.equals("")){
            Toast.makeText(AddNoteActivity.this,"标题不能为空！",Toast.LENGTH_LONG).show();
            return false;
        }
        if(_Note.equals("")){
            Toast.makeText(AddNoteActivity.this,"内容不能为空！",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    private void PushaNote() {
        final Handler handler = new Handler();
        getValues();
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        //创建DBService对象查询表数据，调用UserEquals()是否可以登录，并执行结果动作
                        DBService dbService = new DBService();
                        try {
                            Connection conn = dbService.getConnection();
                            String sql1 = "INSERT INTO `ndata` (`UID`,`Title`,`Data`,`DateTime`) values ("+uid+",'"+_NTitle+"','"+_Note+"','"+_NDate+"');";
                            if(dbService.execUpdate(sql1,null) > 0) {
                                PushSuccess();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        MyClear();
                                    }
                                });
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
    private void PushSuccess(){
        Looper.prepare();
        Intent intent = new Intent(AddNoteActivity.this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(AddNoteActivity.this,"保存成功",Toast.LENGTH_LONG).show();
        finish();
        Looper.loop();
    }
    private void UpdataNote(){
        final Handler handler = new Handler();
        getValues();
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        //创建DBService对象查询表数据，调用UserEquals()是否可以登录，并执行结果动作
                        DBService dbService = new DBService();
                        try {
                            Connection conn = dbService.getConnection();
                            //String sql1 = "INSERT INTO `ndata` (`UID`,`Title`,`Data`,`DateTime`) values ("+uid+",'"+_NTitle+"','"+_Note+"','"+_NDate+"');";
                            String sql1 = "update `test`.`ndata` set `Title`='"+_NTitle+"',`Data`='"+_Note+"',`DateTime`='"+_NDate+"' where `Ndata`="+_NData+";";
                            if(dbService.execUpdate(sql1,null) > 0) {
                                UpdataSuccess();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        MyClear();
                                    }
                                });
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
    private void UpdataSuccess(){
        Looper.prepare();
        Intent intent = new Intent(AddNoteActivity.this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(AddNoteActivity.this,"更新成功",Toast.LENGTH_LONG).show();
        finish();
        Looper.loop();
    }
    private boolean GetNote(){
        SharedPreferences sharedPreferences = getSharedPreferences("nData", Context.MODE_PRIVATE);
        _NTitle = sharedPreferences.getString("Title", "");//(key,若无数据需要赋的值);
        _Note = sharedPreferences.getString("Data", "");
        _NData = sharedPreferences.getString("Ndata", "");
        if (_NTitle.equals("")&&_Note.equals("")&&_NData.equals("")){return true;}
        return false;
    }
    public void MyClear(){
        SharedPreferences sharedPreferences = getSharedPreferences("nData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

    }
}
