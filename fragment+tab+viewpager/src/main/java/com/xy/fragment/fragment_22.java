package com.xy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cn.xy.library.util.log.XLog;

public class fragment_22 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_layout, null);
        ((TextView)view.findViewById(R.id._tv)).setText("这是歌手页面");
        return view;
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
