package com.biyesheji.android.robot.entity;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

public class Mygridview extends GridView {

    public Mygridview(Context context) {
        this(context, null);
    }

    public Mygridview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Mygridview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, i);
    }
}
