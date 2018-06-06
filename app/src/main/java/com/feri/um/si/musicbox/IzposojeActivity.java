package com.feri.um.si.musicbox;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class IzposojeActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izposoje);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ustvari adapter, ki bo vrnil fragment za vsako od treh primarnih sekcij te aktivnosti
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // inicializiramo viewpager z adapterjem
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // get item se klice za inicializacijo ustreznega fragmenta
           switch (position) {
               case 0:
                   return new FragmentOglasi();

               case 1:
                   return new FragmentZahteve();

               case 2:
                   return new FragmentZgodovina();

                   default:
                       return null;
           }
        }

        @Override
        public int getCount() {
            // prikazemo 3 zavihke
            return 3;
        }
    }
}
