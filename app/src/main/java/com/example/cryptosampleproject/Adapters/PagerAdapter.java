package com.example.cryptosampleproject.Adapters;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> Fragment = new ArrayList<>();
    private final List<String> Titles = new ArrayList<>();


    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return Fragment.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return Titles.get(position);
    }

    @Override
    public int getCount() {
        return Titles.size();
    }

    public void AddFragment(Fragment fragment, String title){
        Fragment.add(fragment);
        Titles.add(title);
    }
}
