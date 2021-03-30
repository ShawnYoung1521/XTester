package com.xy.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import cn.xy.library.XApp;
import cn.xy.library.util.log.XLog;
import cn.xy.library.util.toast.XToast;

public class MainActivity extends AppCompatActivity {

    TabLayout mTabLayout;
    ViewPager mViewPager;
    private List<Fragment> mFragment;
    private List<String> mTitle;
    FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XApp.init(getApplication());
        setContentView(R.layout.activity_main);
        mFragment = new ArrayList<>();
        mFragment.add(new fragment_00());
        mFragment.add(new fragment_11());
        mFragment.add(new fragment_22());
        mFragment.add(new fragment_33());
        mTitle = new ArrayList<>();
        mTitle.add("歌曲");
        mTitle.add("歌词");
        mTitle.add("歌手");
        mTitle.add("专辑");
        mTabLayout = (TabLayout)findViewById(R.id._tab);
        fm = getSupportFragmentManager();
        mViewPager = (ViewPager)findViewById(R.id._vp);
        mViewPager.setOffscreenPageLimit(2);//界面缓存个数
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                /**
                 * 滑动距离
                 */
                XLog.i("position  "+position+"  positionOffset  "+positionOffset+"   "+positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                /**
                 * 页面位置
                 */
                XToast.getInstance().Text("当前页面位置： "+position).LayoutParamsY(750).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /**
                 * 滑动状态
                 */
                XLog.i("onPageScrollStateChanged: "+state);
            }
        });
        mViewPager.setAdapter(new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }
            @Override
            public int getCount() {
                return mFragment.size();
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });
        // TabLayout关联ViewPager
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
