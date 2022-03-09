package com.example.qr_go;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SearchFragmentStateAdapter extends FragmentStateAdapter {
    public SearchFragmentStateAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int pos) {
        switch(pos) {
            case 0:
                QRSearchFragment qrFragment = new QRSearchFragment();
                return qrFragment;
            case 1:
                UserSearchFragment userFragment = new UserSearchFragment();
                return userFragment;
            default:
                return null;
        }
    }


    @Override
    public int getItemCount() {
        return 2;
    }
}
