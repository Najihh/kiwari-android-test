package com.senjacreative.kiwarichat.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.senjacreative.kiwarichat.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowDialog {
    public void show(Activity activity, Session session){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_profile);

        TextView name = (TextView) dialog.findViewById(R.id.name);
        name.setText(session.getName());

        TextView email = (TextView) dialog.findViewById(R.id.email);
        email.setText(session.getEmail());

        CircleImageView avatar = (CircleImageView) dialog.findViewById(R.id.avatar);
        Glide.with(activity)
                .load(session.getAvatar())
                .placeholder(activity.getResources().getDrawable(R.color.colorPrimary))
                .into(avatar);

        dialog.show();
    }

    public void show(final Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_about);

        TextView made = (TextView) dialog.findViewById(R.id.made);
        made.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://github.com/MakesMeInspire")));
            }
        });

        dialog.show();
    }
}
