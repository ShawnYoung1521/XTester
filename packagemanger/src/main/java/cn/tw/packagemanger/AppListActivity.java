package cn.tw.packagemanger;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import cn.xy.library.XApp;
import cn.xy.library.util.app.XApplication;
import cn.xy.library.util.log.XLog;

public class AppListActivity extends AppCompatActivity {

    private ListView app_list;
    List<XApplication.AppInfo> appInfos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XApp.init(getApplication());
        setContentView(R.layout.activity_app);
        app_list = findViewById(R.id.list);
        appInfos = XApplication.getAppsInfo();
        app_list.setAdapter(app_adapter);
    }

    private BaseAdapter app_adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return appInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            if(convertView == null) {
                v = newView(parent);
            } else {
                v = convertView;
            }
            bindView(v, position, parent);
            return v;
        }

        class ViewHolder {
            TextView aName;
            ImageView aIcon;
            Switch aSwitch;
        }

        private View newView(ViewGroup parent) {
            View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item, parent, false);
            ViewHolder vh = new ViewHolder();
            vh.aName = (TextView) v.findViewById(R.id.app_name);
            vh.aSwitch = (Switch) v.findViewById(R.id.app_conntrol);
            vh.aIcon = (ImageView) v.findViewById(R.id.app_im);
            v.setTag(vh);
            return v;
        }

        private void bindView(View v, final int position, ViewGroup parent) {
            ViewHolder vh = (ViewHolder) v.getTag();
            vh.aName.setText(appInfos.get(position).getName());
            vh.aIcon.setImageDrawable(appInfos.get(position).getIcon());
//            vh.aSwitch.setChecked(true);
            vh.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    XLog.i(position+"  "+isChecked);
                }
            });
        }
    };
}
