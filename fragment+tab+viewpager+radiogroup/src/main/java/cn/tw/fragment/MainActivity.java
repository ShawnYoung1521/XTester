package cn.tw.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.RadioGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import java.util.ArrayList;
import java.util.List;
import cn.xy.library.XApp;

@SuppressLint("RestrictedApi")
public class MainActivity extends FragmentActivity {

    FragmentManager fragmentManager ;
    Fragment1 fragment_00;
    Fragment2 fragment_11;
    Fragment3 fragment_22;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XApp.init(getApplication());
        setContentView(R.layout.activity_main);
        RadioGroup radioGroup = findViewById(R.id.radiogp);
        radioGroup.setOnCheckedChangeListener(listener);
        fragmentManager = getSupportFragmentManager();
        add();
        show();
    }

    private void show() {
        XFragment.showHide(fragment_00,lists);
    }

    List<Fragment> lists = new ArrayList<>();
    private void add() {
        fragment_00 = new Fragment1();
        fragment_11 = new Fragment2();
        fragment_22 = new Fragment3();
        lists.add(fragment_00);
        lists.add(fragment_11);
        lists.add(fragment_22);
        for (Fragment fragment:lists){
            XFragment.add(fragmentManager,fragment,R.id.main_fragment,true);
        }
    }

    private RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.radiobt1:
                    XFragment.showHide(fragment_00,lists);
                    break;
                case R.id.radiobt2:
                    XFragment.showHide(fragment_11,lists);
                    break;
                case R.id.radiobt3:
                    XFragment.showHide(fragment_22,lists);
                    break;
            }
        }
    };
}
