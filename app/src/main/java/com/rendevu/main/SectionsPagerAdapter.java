package com.rendevu.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        try {
            switch (position) {
                case 0:
                    return new contactActivity();
                case 1:
                    return new mapActivity();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        try {
            switch (position) {
                case 0:
                    return "Contacts";
                case 1:
                    return "Main";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
