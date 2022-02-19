package com.mss.chatbd.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mss.chatbd.Fragments.friendsFragment;
import com.mss.chatbd.Fragments.homeFragment;
import com.mss.chatbd.Fragments.messageFragment;

public class FragmentAdapter extends FragmentPagerAdapter {

    String [] names = {"","",""};

    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new homeFragment();
            case 1:
                return new friendsFragment();
            case 2:
                return new messageFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return names[position];
    }
}
