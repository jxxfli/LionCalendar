package com.example.calendarmaven;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lion.calendar.LionCalendarPopup;
import com.lion.calendar.constant.SelectMoed;
import com.lion.calendar.interf.OnLionCalendarClickListener;

public class MainActivity extends Activity implements OnLionCalendarClickListener {
    String date = null;// 设置默认选中的日期  标准DATE格式
    String monthDate = null;// 设置默认选中的日期  格式为 “2014-04” 标准DATE格式
    String weekDate = null;// 设置默认选中的日期  格式为 “2014-04-05” 标准DATE格式

    Button mMonthBt;
    Button mWeekBt;

    CalendarPopup mCalendarPopup;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        LionCalendarPopup.builder().initsss(MainActivity.this, mMonthBt,0, monthDate);
        setContentView(R.layout.activity_main);
        mMonthBt = (Button) findViewById(R.id.month_bt);
        mMonthBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LionCalendarPopup.getInstance(MainActivity.this)
                        .setSetelectMode(SelectMoed.MODE_MONTH)
                        .setSetelectDate(monthDate)
                        .setShowAsDropDown(v)
                        .setOnCalendarClickListener(MainActivity.this)
                        .show();
                //                mCalendarPopup=   new LionCalendarPopup(MainActivity.this, mMonthBt, LionCalendarPopup.MODE_MONTH, monthDate).setOnCalendarClickListener(this);
            }
        });
        mWeekBt = findViewById(R.id.week_bt);
        mWeekBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LionCalendarPopup.getInstance(MainActivity.this)
                        .setSetelectMode(SelectMoed.MODE_WEEK)
                        .setSetelectDate(weekDate)
                        .setShowAsCenter(v)
                        .setOnCalendarClickListener(MainActivity.this)
                        .show();
//                new LionCalendarPopup(MainActivity.this, mWeekBt, LionCalendarPopup.MODE_WEEK, weekDate).setOnCalendarClickListener(this);
            }
        });
    }


    @Override
    public void onMonthCalendarClick(String dateFormat) {
        monthDate = dateFormat;
        mMonthBt.setText("（月）日期：" + dateFormat);
    }

    @Override
    public void onWeekCalendarClick(String dateFormat, int weekForMonth) {
        weekDate = dateFormat;
        mWeekBt.setText("（周）日期：" + dateFormat + "\t第" + weekForMonth + "周");
    }

}
