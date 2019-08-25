package com.senjacreative.kiwarichat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.senjacreative.kiwarichat.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterConversation extends RecyclerView.Adapter<AdapterConversation.vh>{

    Context mContext;
    EventListener listener;
    ArrayList<String>list_email,list_avatar,list_time,list_message,list_name,list_seen;

    public AdapterConversation(Context mContext, EventListener listener, ArrayList<String> list_email, ArrayList<String> list_avatar, ArrayList<String> list_time, ArrayList<String> list_message, ArrayList<String> list_name, ArrayList<String> list_seen) {
        this.mContext = mContext;
        this.listener = listener;
        this.list_email = list_email;
        this.list_avatar = list_avatar;
        this.list_time = list_time;
        this.list_message = list_message;
        this.list_name = list_name;
        this.list_seen = list_seen;
    }

    public interface EventListener {
        void chat(String email);
    }

    @NonNull
    @Override
    public vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.chat,parent,false);
        return new AdapterConversation.vh(v);
    }

    @Override
    public void onBindViewHolder(@NonNull vh holder, final int position) {
        holder.name.setText(list_name.get(position));
        holder.time.setText(list_time.get(position));
        holder.last_chat.setText(list_message.get(position));
        Glide.with(mContext)
                .load(list_avatar.get(position))
                .placeholder(mContext.getResources().getDrawable(R.color.colorPrimary))
                .into(holder.avatar);

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.chat(list_email.get(position));
            }
        });

        if (!list_seen.get(position).equals("0")){
            holder.notif.setVisibility(View.VISIBLE);
            holder.notif_count.setText(list_seen.get(position));
        }else {
            holder.notif.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list_email.size();
    }

    class vh extends RecyclerView.ViewHolder{
        RelativeLayout notif;
        LinearLayout item;
        CircleImageView avatar;
        TextView name,time,last_chat,notif_count;

        public vh(@NonNull View itemView) {
            super(itemView);

            item = (LinearLayout) itemView.findViewById(R.id.item);
            notif = (RelativeLayout) itemView.findViewById(R.id.notif);
            name = (TextView)itemView.findViewById(R.id.name);
            time = (TextView)itemView.findViewById(R.id.time);
            notif_count = (TextView)itemView.findViewById(R.id.notif_count);
            last_chat = (TextView)itemView.findViewById(R.id.last_chat);
            avatar = (CircleImageView)itemView.findViewById(R.id.avatar);
        }
    }
}
