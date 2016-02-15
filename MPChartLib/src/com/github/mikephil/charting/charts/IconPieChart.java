package com.github.mikephil.charting.charts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.util.AttributeSet;

import com.github.mikephil.charting.R;
import com.github.mikephil.charting.renderer.IconPieChartRenderer;

public class IconPieChart extends PieChart {
    public IconPieChart(Context context) {
        super(context);
    }

    public IconPieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IconPieChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init(AttributeSet attrs) {
        super.init(attrs);

        int iconWidth = (int) (24 * getResources().getDisplayMetrics().density);
        int iconHeight = (int) (24 * getResources().getDisplayMetrics().density);
        float radiusIconDivider = 4f;
        float circleRadius = 100;
        float circleWidth = 10;

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.IconPieChart);
            iconWidth = typedArray.getDimensionPixelSize(R.styleable.IconPieChart_icon_width, iconWidth);
            iconHeight = typedArray.getDimensionPixelSize(R.styleable.IconPieChart_icon_height, iconHeight);
            radiusIconDivider = typedArray.getFloat(R.styleable.IconPieChart_radius_icon_divider, radiusIconDivider);

            circleRadius = typedArray.getDimension(R.styleable.IconPieChart_circle_radius, 100);
            circleWidth = typedArray.getDimension(R.styleable.IconPieChart_circle_thickness, 10);

            typedArray.recycle();
        }

        mRenderer = new IconPieChartRenderer(this, mAnimator, mViewPortHandler, getResources(), new PointF(iconWidth, iconHeight), radiusIconDivider, circleRadius, circleWidth);
    }
}
