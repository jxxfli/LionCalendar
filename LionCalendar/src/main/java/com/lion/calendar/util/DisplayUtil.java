package com.lion.calendar.util;

import android.content.Context;

/**
 * Created by Lion-082
 * Todo:  dip工具类
 * Date:  2020/5/26 13:14
 */
public class DisplayUtil {
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
