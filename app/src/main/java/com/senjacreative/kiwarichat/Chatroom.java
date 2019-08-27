package com.senjacreative.kiwarichat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.senjacreative.kiwarichat.Adapter.AdapterChat;
import com.senjacreative.kiwarichat.Utils.Private;
import com.senjacreative.kiwarichat.Utils.Session;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chatroom extends AppCompatActivity {
    Session session;
    String user_email,user_name,user_avatar;
    CircleImageView avatar;
    TextView name;
    Firebase chat1,chat2;
    ArrayList<String>list_message,list_email,list_avatar,list_time,list_seen;
    AdapterChat adapterChat;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView chat;
    EditText message;
    CircleImageView send;
    String chat_message,chat_time;
    ChildEventListener child1,child2;
    Boolean stateNotif=true,stateNotifAwal=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        session = new Session(Chatroom.this);
        user_email = Private.getPrivateWith();
        initLayout();
        initRecycler();
        getUser();
    }

    void initLayout(){
        avatar = (CircleImageView)findViewById(R.id.avatar);
        name = (TextView) findViewById(R.id.name);
        chat = (RecyclerView) findViewById(R.id.chat);
        message = (EditText) findViewById(R.id.message);
        send = (CircleImageView) findViewById(R.id.send);

        list_message = new ArrayList<String>();
        list_email = new ArrayList<String>();
        list_avatar = new ArrayList<String>();
        list_time = new ArrayList<String>();
        list_seen = new ArrayList<String>();

    }

    void initListenner(){
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat_message = message.getText().toString();
                SimpleDateFormat time = new SimpleDateFormat("HH:mm");
                chat_time = time.format(new Date());
                if (chat_message.length()>0){
                    SendMessage();
                }
            }
        });
    }

    void initRecycler(){
        chat.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        chat.setLayoutManager(layoutManager);
    }

    void getUser(){
        int userpos=0;
        boolean userselected=false;
        ArrayList<String> u_email = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.user_email)));
        ArrayList<String> u_name = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.user_name)));
        ArrayList<String> u_avatar = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.user_avatar)));

        for (int a=0;a<u_email.size();a++){
            if (user_email.equals(u_email.get(a))){
                userselected=true;
                userpos=a;
            }
        }

        if (userselected){
            user_name = u_name.get(userpos);
            user_avatar = u_avatar.get(userpos);
            setView();
            initChat();
            initListenner();
        }
    }

    void setView(){
        name.setText(user_name);
        Glide.with(this)
                .load(user_avatar)
                .placeholder(getResources().getDrawable(R.color.colorPrimary))
                .into(avatar);
    }

    void initChat(){
        Firebase.setAndroidContext(this);

        String from = session.getEmail();
        int f_indx = session.getEmail().indexOf('@');
        from = from.substring(0,f_indx);

        String to = Private.getPrivateWith();
        int t_indx = to.indexOf('@');
        to = to.substring(0,t_indx);

        chat1 = new Firebase(Private.getBaseUri()+"messages/" + from + "_" + to);
        chat2 = new Firebase(Private.getBaseUri()+"messages/" + to + "_" + from);
        chatlistener();

    }

    void chatlistener(){
        child1 = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try{
                    Map map = dataSnapshot.getValue(Map.class);
                    String message = map.get("message").toString();
                    String time = map.get("time").toString();
                    String email = map.get("user").toString();
                    String avatar = map.get("avatar").toString();
                    String seen = map.get("seen").toString();

                    list_email.add(email);
                    list_avatar.add(avatar);
                    list_time.add(time);
                    list_message.add(message);

                    if(!email.matches(session.getEmail())){
                        Log.d("SEEN","updated");
                        Map<String, Object> seenUpdate = new HashMap<>();
                        seenUpdate.put(dataSnapshot.getKey() + "/seen", "1");
                        chat1.updateChildren(seenUpdate);
                        setNotif();
                    }
                    showChat();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(Chatroom.this, ""+getString(R.string.app_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };

        child2 = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try{
                    Map map = dataSnapshot.getValue(Map.class);
                    String email = map.get("user").toString();

                    if(!email.matches(session.getEmail())){
                        Log.d("SEEN","updated");
                        Map<String, Object> seenUpdate = new HashMap<>();
                        seenUpdate.put(dataSnapshot.getKey() + "/seen", "1");
                        chat2.updateChildren(seenUpdate);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(Chatroom.this, ""+getString(R.string.app_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };

        chat1.addChildEventListener(child1);
        chat2.addChildEventListener(child2);
    }

    void showChat(){
        if (chat.getAdapter()!=null){
            chat.getAdapter().notifyItemInserted(list_avatar.size()-1);
        }else{
            adapterChat = new AdapterChat(Chatroom.this,list_message,list_email,list_avatar,list_time);
            chat.setAdapter(adapterChat);
        }
        chat.scrollToPosition(list_avatar.size()-1);
    }

    void setNotif(){
        if (stateNotifAwal){
            if (stateNotif){
                stateNotif=false;
                try {
                    MediaPlayer mp= MediaPlayer.create(Chatroom.this, R.raw.doubt);
                    mp.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    stateNotif = true;
                }
            },2000);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stateNotifAwal = true;
            }
        },2000);
    }

    void SendMessage(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("message", chat_message);
        map.put("user", session.getEmail());
        map.put("time", chat_time);
        map.put("avatar", session.getAvatar());
        map.put("seen", "0");
        chat1.push().setValue(map);
        chat2.push().setValue(map);
        message.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chat1.removeEventListener(child1);
        chat2.removeEventListener(child2);

    }
}
