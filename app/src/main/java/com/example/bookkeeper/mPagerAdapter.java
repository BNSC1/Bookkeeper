package com.example.bookkeeper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class mPagerAdapter extends FragmentStateAdapter {
    public mPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new BookkeepFragment();
            case 1: return new AccountFragment();
            default: return new BackupFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
