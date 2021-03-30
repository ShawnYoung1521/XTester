package cn.tw.viewpager2;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tw.music.R;
import java.util.ArrayList;
import java.util.List;
import cn.xy.library.XApp;
import cn.xy.library.util.log.XLog;

public class Activity extends AppCompatActivity {

    public static ViewPager2 mViewPager;
    private ViewPager2Adapter mAdapter;
    List<Fragment> fragments = new ArrayList<>();
    TabLayout mTabLayout;
    private List<String> mTitle;
    private TabLayoutMediator mediator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XApp.init(getApplication());
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main);
        mTabLayout = (TabLayout)findViewById(R.id._tab);
        mTitle = new ArrayList<>();
        mTitle.add("播放界面");
        mTitle.add("歌曲界面");
        mViewPager = findViewById(R.id.viewpager2);
        mAdapter = new ViewPager2Adapter(this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL); //ViewPager2.ORIENTATION_VERTICAL
        mViewPager.setPageTransformer(new MyPageTransformer());
        fragments.add(new Fragment1(mViewPager));
        fragments.add(new Fragment2(mViewPager));
        mediator = new TabLayoutMediator(mTabLayout, mViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(TabLayout.Tab tab, int position) {
                XLog.i(position);
                TextView tabView = new TextView(Activity.this);
                tabView.setText(mTitle.get(position));
                tabView.setTextColor(getColor(R.color.design_default_color_primary));
                tab.setCustomView(tabView);
            }
        });
        mediator.attach();
    }

    class ViewPager2Adapter extends FragmentStateAdapter {
        public ViewPager2Adapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }
    }
}
