package com.artursworld.nccn.view.statistics;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.artursworld.nccn.R;

import java.util.ArrayList;
import java.util.List;

import eu.long1.spacetablayout.SpaceTabLayout;


public class StatisticsTabsActivity extends AppCompatActivity {

    private SpaceTabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_tabs);

        //add the fragments you want to display in a List
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HadsdStatisticsFragment());
        fragmentList.add(new DistressThermometerStatisticsFragment());
        fragmentList.add(new QualityOfLifeStatisticsFragment());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (SpaceTabLayout) findViewById(R.id.spaceTabLayout);

        //we need the savedInstanceState to get the position
        tabLayout.initialize(viewPager, getSupportFragmentManager(),
                fragmentList, savedInstanceState);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        tabLayout.saveState(outState);
        super.onSaveInstanceState(outState);
    }


}
