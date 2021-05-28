package cn.tw.time;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cn.xy.library.XApp;
import cn.xy.library.constant.TimeConstants;
import cn.xy.library.util.time.XTime;

public class MainActivity extends AppCompatActivity {
    /**
     * getSafeDateFormat       : 获取安全的日期格式
     * millis2String           : 将时间戳转为时间字符串
     * string2Millis           : 将时间字符串转为时间戳
     * string2Date             : 将时间字符串转为 Date 类型
     * date2String             : 将 Date 类型转为时间字符串
     * date2Millis             : 将 Date 类型转为时间戳
     * millis2Date             : 将时间戳转为 Date 类型
     * getTimeSpan             : 获取两个时间差（单位：unit）
     * getFitTimeSpan          : 获取合适型两个时间差
     * getNowMills             : 获取当前毫秒时间戳
     * getNowString            : 获取当前时间字符串
     * getNowDate              : 获取当前 Date
     * getTimeSpanByNow        : 获取与当前时间的差（单位：unit）
     * getFitTimeSpanByNow     : 获取合适型与当前时间的差
     * getFriendlyTimeSpanByNow: 获取友好型与当前时间的差
     * getMillis               : 获取与给定时间等于时间差的时间戳
     * getString               : 获取与给定时间等于时间差的时间字符串
     * getDate                 : 获取与给定时间等于时间差的 Date
     * getMillisByNow          : 获取与当前时间等于时间差的时间戳
     * getStringByNow          : 获取与当前时间等于时间差的时间字符串
     * getDateByNow            : 获取与当前时间等于时间差的 Date
     * isToday                 : 判断是否今天
     * isLeapYear              : 判断是否闰年
     * getChineseWeek          : 获取中式星期
     * getUSWeek               : 获取美式式星期
     * isAm                    : 判断是否上午
     * isPm                    : 判断是否下午
     * getValueByCalendarField : 根据日历字段获取值
     * getChineseZodiac        : 获取生肖
     * getZodiac               : 获取星座
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XApp.init(getApplication());
        setContentView(R.layout.activity_main);
        time = XTime.getNowMills();
    }

    private long time;
    private long time2;
    public void getNowMills(View view){
        time2 = XTime.getNowMills();
        Toast.makeText(XApp.getApp(),""+XTime.getNowMills(),Toast.LENGTH_SHORT).show();
    }

    public void getNowDate(View view){
        Toast.makeText(XApp.getApp(),""+XTime.getNowDate(),Toast.LENGTH_SHORT).show();
    }

    public void getNowString(View view){
        Toast.makeText(XApp.getApp(),""+XTime.getNowString(),Toast.LENGTH_SHORT).show();
    }
    public void getTimeSpanByNow(View view){
        Toast.makeText(XApp.getApp(),"打开应用已经过去"+XTime.getTimeSpanByNow(time, TimeConstants.SEC)+"秒！",Toast.LENGTH_SHORT).show();
    }
    public void isToday(View view){
        Toast.makeText(XApp.getApp(),"今天是否获取了时间戳："+XTime.isToday(time2)+"！",Toast.LENGTH_SHORT).show();
    }
    public void isLeapYear(View view){
        Toast.makeText(XApp.getApp(),""+XTime.isLeapYear(time)+"！",Toast.LENGTH_SHORT).show();
    }
    public void getChineseWeek(View view){
        Toast.makeText(XApp.getApp(),""+XTime.getChineseWeek(time)+"！",Toast.LENGTH_SHORT).show();
    }
    public void getUSWeek(View view){
        Toast.makeText(XApp.getApp(),""+XTime.getUSWeek(time)+"！",Toast.LENGTH_SHORT).show();
    }
    public void isAm(View view){
        Toast.makeText(XApp.getApp(),""+XTime.isAm(time)+"！",Toast.LENGTH_SHORT).show();
    }
    public void getChineseZodiac(View view){
        Toast.makeText(XApp.getApp(),""+XTime.getChineseZodiac(time)+"！",Toast.LENGTH_SHORT).show();
    }
    public void getZodiac(View view){
        Toast.makeText(XApp.getApp(),""+XTime.getZodiac(time)+"！",Toast.LENGTH_SHORT).show();
    }
}