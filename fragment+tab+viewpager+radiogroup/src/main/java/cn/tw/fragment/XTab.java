package cn.tw.fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import java.util.List;

public class XTab {
    public static void addTab(TabLayout tabLayout,
                              ViewPager viewPager,
                              final List<Fragment> fragment,
                              final List<String> title,
                              FragmentManager fragmentManager,
                              int offscreenPageLimit,
                              final onPageSelected onPageSelected){
        set(tabLayout,viewPager,fragment,title,fragmentManager,offscreenPageLimit,onPageSelected);
    }
    public static void addTab(TabLayout tabLayout,
                              ViewPager viewPager,
                              final List<Fragment> fragment,
                              final List<String> title,
                              FragmentManager fragmentManager,
                              int offscreenPageLimit){
        set(tabLayout,viewPager,fragment,title,fragmentManager,offscreenPageLimit,null);
    }

    public static void addTab(TabLayout tabLayout,
                              ViewPager viewPager,
                              final List<Fragment> fragment,
                              final List<String> title,
                              FragmentManager fragmentManager
                              ){
        set(tabLayout,viewPager,fragment,title,fragmentManager,fragment.size(),null);
    }

    public static void addTab(TabLayout tabLayout,
                              ViewPager viewPager,
                              final List<Fragment> fragment,
                              final List<String> title,
                              FragmentManager fragmentManager,
                              final onPageSelected onPageSelected
                              ){
        set(tabLayout,viewPager,fragment,title,fragmentManager,fragment.size(),onPageSelected);
    }

    private static void set(TabLayout tabLayout,
                            ViewPager viewPager,
                            final List<Fragment> fragment,
                            final List<String> title,
                            FragmentManager fragmentManager,
                            int offscreenPageLimit,
                            final onPageSelected onPageSelected){
        viewPager.setOffscreenPageLimit(offscreenPageLimit);
        viewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return fragment.get(position);
            }
            @Override
            public int getCount() {
                return fragment.size();
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return title.get(position);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {}

            @Override
            public void onPageSelected(int i) {
                onPageSelected.onPageSelected(i);}

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        // TabLayout关联ViewPager
        tabLayout.setupWithViewPager(viewPager);
    }

    public interface onPageSelected{
        void onPageSelected(int position);
    }
}
