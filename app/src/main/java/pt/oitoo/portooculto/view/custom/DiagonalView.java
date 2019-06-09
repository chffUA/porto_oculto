package pt.oitoo.portooculto.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import pt.oitoo.portooculto.R;

public class DiagonalView extends View {
    private Paint bgPaint;
    public DiagonalView (Context context) {
        super (context);
        init ();
    }
    public DiagonalView (Context context, AttributeSet attrs) {
        super (context, attrs);
        init ();
    }
    public DiagonalView (Context context, AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        init ();
    }
    private void init () {
        bgPaint = new Paint ();
        bgPaint.setFlags (Paint.ANTI_ALIAS_FLAG);
        bgPaint.setStyle (Paint.Style.FILL);
        bgPaint.setColor (getResources (). getColor (R.color.oitoo_orange));

    }
    @ Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure (widthMeasureSpec, heightMeasureSpec);
    }
    @Override
    protected void onDraw (Canvas canvas) {
        super.onDraw (canvas);
        int h = getMeasuredHeight ();
        int w = getMeasuredWidth ();
        Path path = new Path ();
        path.lineTo (w, 0);
        path.lineTo (h / 2 + h / 4, w);
        path.lineTo (0, h);
        path.close ();
        canvas.drawPath (path, bgPaint);
    }
}

