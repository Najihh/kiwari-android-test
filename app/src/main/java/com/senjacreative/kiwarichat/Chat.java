package com.senjacreative.kiwarichat;


import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.senjacreative.kiwarichat.Adapter.AdapterConversation;
import com.senjacreative.kiwarichat.Utils.Private;
import com.senjacreative.kiwarichat.Utils.Session;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class Chat extends Fragment implements AdapterConversation.EventListener{

    TextView no_chat;
    RecyclerView chat;
    AdapterConversation adapterConversation;
    RecyclerView.LayoutManager layoutManager;
    Firebase listchat;
    ArrayList<String>list_email,list_avatar,list_time,list_message,list_name,list_seen;
    Session session;
    ValueEventListener chat_listener;
    Boolean stateNotif=true;
    MediaPlayer mp;

    public Chat() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout(view);
        initRecycler();
        showNoChat(true);
        if (getContext()!=null){
            session = new Session(getContext());
            initConversation(getContext());
        }
    }

    void initLayout(View v){
        no_chat = (TextView)v.findViewById(R.id.no_chat);
        chat = (RecyclerView)v.findViewById(R.id.chat);

        list_email = new ArrayList<String>();
        list_avatar = new ArrayList<String>();
        list_time = new ArrayList<String>();
        list_message = new ArrayList<String>();
        list_name = new ArrayList<String>();
        list_seen = new ArrayList<String>();
    }

    void initRecycler(){
        chat.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        chat.setLayoutManager(layoutManager);
    }

    void initConversation(Context context){
        initListener();
        Firebase.setAndroidContext(context);
        listchat = new Firebase(Private.getBaseUri()+"messages/");
        listchat.addValueEventListener(chat_listener);
    }

    void initListener(){
        chat_listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    parstChat(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
    }

    void parstChat(DataSnapshot data){
        final ArrayList<String> u_email = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.user_email)));
        final ArrayList<String> u_name = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.user_name)));
        final ArrayList<String> u_avatar = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.user_avatar)));
        String message="",time="",seen="",email="";

        int pos=0;
        list_email.clear();
        list_avatar.clear();
        list_time.clear();
        list_message.clear();
        list_name.clear();
        list_seen.clear();
        for(DataSnapshot chat_list : data.getChildren()){
            Log.d("CHAT LIST",""+chat_list.getKey());

            String key = chat_list.getKey();
            int key_sparrate = key.indexOf('_');
            key = key.substring(0,key_sparrate);
            Log.d("CHAT FROM",key);

            if (session.getEmail().contains(key)){
                String t_key = chat_list.getKey();
                int t_key_sparrate = chat_list.getKey().indexOf('_');
                t_key = t_key.substring(t_key_sparrate+1);
                Log.d("CHAT TO",t_key);

                for (int a=0;a<u_email.size();a++){
                    if (u_email.get(a).contains(t_key)){
                        Log.d("CHAT WITH",u_name.get(a));
                        list_email.add(u_email.get(a));
                        list_avatar.add(u_avatar.get(a));
                        list_name.add(u_name.get(a));
                    }
                }
                int total_message=0;
                int m_seen=0;
                for (DataSnapshot chat_detail : chat_list.getChildren()){
                    Map map = chat_detail.getValue(Map.class);
                    message = map.get("message").toString();
                    time = map.get("time").toString();
                    seen = map.get("seen").toString();
                    email = map.get("user").toString();
                    if (seen.equals("0")&&!email.equals(session.getEmail())){
                        m_seen++;
                    }
                    total_message++;
                }

                list_message.add(message);
                list_time.add(time);
                list_seen.add(""+m_seen);

                if (m_seen>0){
                    setNotif();
                }

                initViews();
            }
            pos++;
        }

    }

    void showNoChat(Boolean a){
        if (a){
            no_chat.setVisibility(View.VISIBLE);
            chat.setVisibility(View.GONE);
        }else {
            no_chat.setVisibility(View.GONE);
            chat.setVisibility(View.VISIBLE);
        }
    }

    void initViews(){
        if (chat.getAdapter()!=null){
            chat.getAdapter().notifyDataSetChanged();
        }else{
            adapterConversation = new AdapterConversation(getContext(),this,list_email,list_avatar,list_time,list_message,list_name,list_seen);
            chat.setAdapter(adapterConversation);
        }

        showNoChat(false);
    }

    void setNotif(){
        if (stateNotif){
            stateNotif = false;
            try {
                if (mp!=null){
                    mp.start();
                }
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

    @Override
    public void chat(String email) {
        Private.setPrivateWith(email);
        startActivity(new Intent(getActivity(),Chatroom.class));
    }

    @Override
    public void onPause() {
        super.onPause();
        mp = null;
        listchat.removeEventListener(chat_listener);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (chat_listener==null){
            initListener();
        }
        mp = MediaPlayer.create(getContext(), R.raw.point);
        listchat.addValueEventListener(chat_listener);
    }
}
