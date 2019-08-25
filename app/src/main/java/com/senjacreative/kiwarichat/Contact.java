package com.senjacreative.kiwarichat;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.senjacreative.kiwarichat.Adapter.AdapterContact;
import com.senjacreative.kiwarichat.Utils.Private;
import com.senjacreative.kiwarichat.Utils.Session;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class Contact extends Fragment implements AdapterContact.EventListener{
    Session session;
    RecyclerView contact;
    AdapterContact adapterContact;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> list_name,list_email,list_avatar;
    public Contact() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getContext()!=null)
        session = new Session(getContext());
        initLayout(view);
        initRecycler();
        getContact();
    }

    void initLayout(View v){
        contact = (RecyclerView) v.findViewById(R.id.contact);

        list_name = new ArrayList<String>();
        list_email = new ArrayList<String>();
        list_avatar = new ArrayList<String>();
    }

    void initRecycler(){
        contact.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        contact.setLayoutManager(layoutManager);
    }

    void getContact(){
        int userpos=0;
        boolean userselected=false;
        ArrayList<String> u_email = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.user_email)));
        ArrayList<String> u_name = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.user_name)));
        ArrayList<String> u_avatar = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.user_avatar)));

        for (int a=0;a<u_name.size();a++){
            if (session.getName().equals(u_name.get(a))){
                userselected=true;
                userpos=a;
            }
        }

        if (userselected){
            for (int a=0;a<u_name.size();a++){
                if (a!=userpos){
                    list_name.add(u_name.get(a));
                    list_email.add(u_email.get(a));
                    list_avatar.add(u_avatar.get(a));
                }
            }

            adapterContact = new AdapterContact(getActivity(),Contact.this,list_name,list_email,list_avatar);
            contact.setAdapter(adapterContact);
        }else {
            Toast.makeText(getActivity(), "No Contact Avalaible", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void chat(String email) {
        Private.setPrivateWith(email);
        startActivity(new Intent(getActivity(),Chatroom.class));
    }
}
