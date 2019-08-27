package com.senjacreative.kiwarichat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.senjacreative.kiwarichat.Adapter.AdapterTabLayout;
import com.senjacreative.kiwarichat.Utils.Session;
import com.senjacreative.kiwarichat.Utils.ShowDialog;

public class MainActivity extends AppCompatActivity {
    TextView options;
    TabLayout tablayout;
    ViewPager content;
    AdapterTabLayout adapterTabLayout;
    Session session;
    ShowDialog dialog;
    Boolean stateExit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new Session(MainActivity.this);
        dialog = new ShowDialog();
        cekSession();
        initLayout();
        initTabLayout();
        initMenu();
    }

    void cekSession(){
        if (!session.getLoggedin()){
            startActivity(new Intent(MainActivity.this,Login.class));
            finish();
        }else {
            Toast.makeText(this, "Selamat Datang "+session.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    void initLayout(){
        tablayout = (TabLayout)findViewById(R.id.tablayout);
        content = (ViewPager)findViewById(R.id.content);
        options = (TextView)findViewById(R.id.options);
    }

    void initTabLayout(){
        adapterTabLayout = new AdapterTabLayout(getSupportFragmentManager());
        content.setAdapter(adapterTabLayout);
        tablayout.setupWithViewPager(content);
    }

    void initMenu(){
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this,options);
                popup.inflate(R.menu.option_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                dialog.show(MainActivity.this,session);
                                return true;
                            case R.id.menu2:
                                session.saveBooleanSession(Session.LOGGEDIN,false);
                                cekSession();
                                return true;
                            case R.id.menu3:
                                dialog.show(MainActivity.this);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (stateExit){
            super.onBackPressed();
            finish();
        }else {
            stateExit = true;
            Toast.makeText(this, ""+getString(R.string.app_exit), Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stateExit = false;
            }
        },2000);
    }
}
