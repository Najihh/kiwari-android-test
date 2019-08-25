package com.senjacreative.kiwarichat.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.senjacreative.kiwarichat.Chat;
import com.senjacreative.kiwarichat.Contact;

public class AdapterTabLayout extends FragmentStatePagerAdapter {
    public AdapterTabLayout(FragmentManager fm) {
        super(fm);
    }

    @Override public Fragment getItem(int position) {
        switch (position){
            case 0: return new Chat();
            case 1: return new Contact();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "CHAT";
            case 1: return "CONTACT";
            default: return null;
        }
    }


}
