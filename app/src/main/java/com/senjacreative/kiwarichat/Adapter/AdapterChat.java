package com.senjacreative.kiwarichat.Adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.senjacreative.kiwarichat.R;
import com.senjacreative.kiwarichat.Utils.Private;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.vh>{

    Context mContext;
    ArrayList<String> list_message,list_email,list_avatar,list_time;

    Runnable runnable;
    int delayAnimate = 200;
    int itemCount;

    Handler controlDelay = new Handler();
    int bindPos, lastBindPos, lastGetDelay;

    public AdapterChat(Context mContext, ArrayList<String> list_message, ArrayList<String> list_email, ArrayList<String> list_avatar, ArrayList<String> list_time) {
        this.mContext = mContext;
        this.list_message = list_message;
        this.list_email = list_email;
        this.list_avatar = list_avatar;
        this.list_time = list_time;
    }

    @NonNull
    @Override
    public vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.chatbox,parent,false);
        return new AdapterChat.vh(v);
    }

    @Override
    public void onBindViewHolder(@NonNull vh holder, int position) {
        holder.item.setVisibility(View.INVISIBLE);
        setAnimation(holder.item);
        controlDelay();

        if (list_email.get(position).equals(Private.getPrivateWith())){
            holder.chat_left.setVisibility(View.VISIBLE);
            holder.chat_right.setVisibility(View.GONE);

            holder.l_chat.setText(list_message.get(position));
            holder.l_time.setText(list_time.get(position));

            Glide.with(mContext)
                    .load(list_avatar.get(position))
                    .placeholder(mContext.getResources().getDrawable(R.color.colorPrimary))
                    .into(holder.l_avatar);

        }else {
            holder.chat_left.setVisibility(View.GONE);
            holder.chat_right.setVisibility(View.VISIBLE);

            holder.r_chat.setText(list_message.get(position));
            holder.r_time.setText(list_time.get(position));

            Glide.with(mContext)
                    .load(list_avatar.get(position))
                    .placeholder(mContext.getResources().getDrawable(R.color.colorPrimary))
                    .into(holder.r_avatar);
        }

    }

    @Override
    public int getItemCount() {
        return list_email.size();
    }

    private void setAnimation(final View view) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
                if (view != null) {
                    view.startAnimation(animation);
                    view.setVisibility(View.VISIBLE);
                    runnable = this;
                }
            }
        }, delayAnimate);
        delayAnimate += 100;
    }

    void controlDelay() {
        controlDelay.postDelayed(new Runnable() {
            @Override
            public void run() {
                runnable = this;
                if (lastGetDelay == delayAnimate) {
                    delayAnimate = 0;
                    controlDelay.removeCallbacks(runnable);
                } else {
                    controlDelay.postDelayed(runnable, 200);
                    lastGetDelay = delayAnimate;
                }
            }
        }, 200);
    }

    class vh extends RecyclerView.ViewHolder{
        FrameLayout chat_left,chat_right;
        LinearLayout item;
        TextView l_time,l_chat,r_time,r_chat;
        CircleImageView l_avatar,r_avatar;

        public vh(@NonNull View itemView) {
            super(itemView);
            item = (LinearLayout)itemView.findViewById(R.id.item);
            chat_left = (FrameLayout)itemView.findViewById(R.id.chat_left);
            chat_right = (FrameLayout)itemView.findViewById(R.id.chat_right);
            l_time = (TextView) itemView.findViewById(R.id.l_time);
            l_chat = (TextView)itemView.findViewById(R.id.l_chat);
            r_time = (TextView) itemView.findViewById(R.id.r_time);
            r_chat = (TextView)itemView.findViewById(R.id.r_chat);
            r_avatar = (CircleImageView)itemView.findViewById(R.id.r_avatar);
            l_avatar = (CircleImageView)itemView.findViewById(R.id.l_avatar);
        }
    }
}
