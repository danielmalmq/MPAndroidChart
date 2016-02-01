
package com.github.mikephil.charting.charts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

import com.github.mikephil.charting.R;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.renderer.LineChartRenderer;

/**
 * Chart that draws lines, surfaces, circles, ...
 * 
 * @author Philipp Jahoda
 */
public class LineChart extends BarLineChartBase<LineData> implements LineDataProvider {

    public LineChart(Context context) {
        super(context);
    }

    public LineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init(AttributeSet attrs) {
        super.init(attrs);

        LineChartRenderer lineChartRenderer = new LineChartRenderer(this, mAnimator, mViewPortHandler);
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LineChart);
            boolean hasGradientBackground = typedArray.hasValue(R.styleable.LineChart_gradient_top_background) || typedArray.hasValue(R.styleable.LineChart_gradient_bottom_background);
            int topBackgroundColor = typedArray.getColor(R.styleable.LineChart_gradient_top_background, Color.TRANSPARENT);
            int bottomBackgroundColor = typedArray.getColor(R.styleable.LineChart_gradient_bottom_background, Color.TRANSPARENT);
            typedArray.recycle();

            if (hasGradientBackground) {
                lineChartRenderer.setBackgroundGradientColors(topBackgroundColor, bottomBackgroundColor);
            }
        }

        mRenderer = lineChartRenderer;
    }

    @Override
    protected void calcMinMax() {
        super.calcMinMax();

        if (mDeltaX == 0 && mData.getYValCount() > 0)
            mDeltaX = 1;
    }
    
    @Override
    public LineData getLineData() {
        return mData;
    }

    @Override
    protected void onDetachedFromWindow() {
        // releases the bitmap in the renderer to avoid oom error
        if(mRenderer != null && mRenderer instanceof LineChartRenderer) {
            ((LineChartRenderer) mRenderer).releaseBitmap();
        }
        super.onDetachedFromWindow();
    }
}
