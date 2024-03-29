package com.example.cryptosampleproject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.cryptosampleproject.Adapters.Currency;
import com.example.cryptosampleproject.Adapters.PagerAdapter;
import com.example.cryptosampleproject.Fragments.CurrenciesFragment;
import com.example.cryptosampleproject.Fragments.NewsFragment;
import com.example.cryptosampleproject.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class TabbedActivity extends AppCompatActivity{


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private com.example.cryptosampleproject.Adapters.PagerAdapter pagerAdapter;
    private List<Currency> itemList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.pager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.AddFragment(new CurrenciesFragment(),"Currencies");
        pagerAdapter.AddFragment(new NewsFragment(),"News");
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

//        Intent intent = new Intent(this, Linechart.class);
//        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tabbed_act_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);

    }


}
