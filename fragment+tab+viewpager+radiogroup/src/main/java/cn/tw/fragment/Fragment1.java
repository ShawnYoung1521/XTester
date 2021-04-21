package cn.tw.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;
import cn.xy.library.util.log.XLog;

public class Fragment1 extends Fragment {

    TabLayout mTabLayout;
    ViewPager mViewPager;
    private List<Fragment> mFragment;
    private List<String> mTitle;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_layout, null);
        create(view);
        return  view;
    }

    private void create(View view) {
        mFragment = new ArrayList<>();
        mFragment.add(new Fragment1_Tab1());
        mFragment.add(new Fragment1_Tab2());
        mFragment.add(new Fragment1_Tab3());
        mTitle = new ArrayList<>();
        mTitle.add("TAB1");
        mTitle.add("TAB2");
        mTitle.add("TAB3");
        mTabLayout = (TabLayout)view.findViewById(R.id._tab);
        mViewPager = (ViewPager)view.findViewById(R.id._vp);
        XTab.addTab(mTabLayout, mViewPager, mFragment, mTitle, getFragmentManager(), new XTab.onPageSelected() {
            @Override
            public void onPageSelected(int position) {
                XLog.i(position);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        XLog.i();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        XLog.i();
    }

    @Override
    public void onPause() {
        super.onPause();
        XLog.i();
    }
}
