package com.senjacreative.kiwarichat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.senjacreative.kiwarichat.R;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterContact extends RecyclerView.Adapter<AdapterContact.vh>{
    Context mContext;
    EventListener listener;
    ArrayList<String> list_name,list_email,list_avatar;

    public AdapterContact(Context mContext, EventListener listener, ArrayList<String> list_name, ArrayList<String> list_email, ArrayList<String> list_avatar) {
        this.mContext = mContext;
        this.listener = listener;
        this.list_name = list_name;
        this.list_email = list_email;
        this.list_avatar = list_avatar;
    }

    public interface EventListener {
        void chat(String email);
    }

    @NonNull
    @Override
    public vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.contact,parent,false);
        return new AdapterContact.vh(v);
    }

    @Override
    public void onBindViewHolder(@NonNull vh holder, final int position) {
        Glide.with(mContext)
                .load(list_avatar.get(position))
                .placeholder(mContext.getResources().getDrawable(R.color.colorPrimary))
                .into(holder.avatar);
        holder.name.setText(list_name.get(position));
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.chat(list_email.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_name.size();
    }

    class vh extends RecyclerView.ViewHolder{
        LinearLayout item;
        CircleImageView avatar;
        TextView name;
        public vh(@NonNull View itemView) {
            super(itemView);
            item = (LinearLayout) itemView.findViewById(R.id.item);
            name = (TextView)itemView.findViewById(R.id.name);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
        }
    }
}
