package com.senjacreative.kiwarichat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.senjacreative.kiwarichat.Utils.Session;

import java.util.ArrayList;
import java.util.Arrays;

public class Login extends AppCompatActivity {
    EditText et_email,et_password;
    CardView btn_login;
    String email,password;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new Session(Login.this);
        cekSession();
        initLayout();
        initListener();
    }

    void cekSession(){
        if (session.getLoggedin()){
            startActivity(new Intent(Login.this,MainActivity.class));
            finish();
        }
    }

    void initLayout(){
        et_email = (EditText)findViewById(R.id.et_email);
        et_password = (EditText)findViewById(R.id.et_password);
        btn_login = (CardView)findViewById(R.id.btn_login);
    }

    void initListener(){
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputData();
                if (email.length()>0){
                    if (password.length()>0){
                        cekLogin();
                    }else {
                        et_password.requestFocus();
                        et_password.setError(getString(R.string.login_no_password));
                    }
                }else {
                    et_email.requestFocus();
                    et_email.setError(getString(R.string.login_no_username));
                }
            }
        });
    }

    void getInputData(){
        email = et_email.getText().toString();
        password = et_password.getText().toString();
    }

    void cekLogin(){
        int userpos=0;
        boolean userselected=false;
        ArrayList<String> list_email = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.user_email)));
        ArrayList<String> list_password = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.user_password)));
        ArrayList<String> list_name = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.user_name)));
        ArrayList<String> list_avatar = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.user_avatar)));

        for (int a=0;a<list_email.size();a++){
            if (email.equals(list_email.get(a))){
                userselected=true;
                userpos=a;
            }
        }

        if (userselected){
            userselected=false;
            for (int a=0;a<list_password.size();a++){
                if (password.equals(list_password.get(a))){
                    userselected=true;
                }
            }

            if (userselected){
                session.saveStringSession(Session.EMAIL,list_email.get(userpos));
                session.saveStringSession(Session.PASSWORD,list_password.get(userpos));
                session.saveStringSession(Session.NAME,list_name.get(userpos));
                session.saveStringSession(Session.AVATAR,list_avatar.get(userpos));
                session.saveBooleanSession(Session.LOGGEDIN,true);
                startActivity(new Intent(Login.this,MainActivity.class));
                finish();
            }else {
                et_password.requestFocus();
                et_password.setError(getString(R.string.login_error_password));
            }

        }else {
            et_email.requestFocus();
            et_email.setError(getString(R.string.login_error_username));
        }
    }
}
