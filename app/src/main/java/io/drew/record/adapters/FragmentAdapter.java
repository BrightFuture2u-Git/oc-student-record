package io.drew.record.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/20 6:07 PM
 */
public class FragmentAdapter extends FragmentPagerAdapter {


    private List<Fragment> mFragmentList;//各导航的Fragment
    private String[] mTitle; //导航的标题

    public FragmentAdapter(FragmentManager fragmentManager, List<Fragment> fragments, String[] title) {
        super(fragmentManager);
        mFragmentList = fragments;
        mTitle = title;

    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle[position];
    }
}