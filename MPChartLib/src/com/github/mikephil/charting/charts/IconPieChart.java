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
        float chartInset = 1.3f;
        float radiusIconDivider = 4f;

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.IconPieChart);
            iconWidth = typedArray.getDimensionPixelSize(R.styleable.IconPieChart_icon_width, iconWidth);
            iconHeight = typedArray.getDimensionPixelSize(R.styleable.IconPieChart_icon_height, iconHeight);
            chartInset = typedArray.getFloat(R.styleable.IconPieChart_chart_inset, chartInset);
            radiusIconDivider = typedArray.getFloat(R.styleable.IconPieChart_radius_icon_divider, radiusIconDivider);

            typedArray.recycle();
        }

        mRenderer = new IconPieChartRenderer(this, mAnimator, mViewPortHandler, getResources(), new PointF(iconWidth, iconHeight), chartInset, radiusIconDivider);
    }
}
