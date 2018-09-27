package ir.berimbasket.app.ui.common.swipe_showcase;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

public abstract class Shape {
    private int color = Color.argb(0, 0, 0, 0);
    private Paint paint;

    public Shape() {
        this.paint = new Paint();
        this.paint.setColor(getColor());
        this.paint.setAntiAlias(true);
        this.paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));

        Paint borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(Color.parseColor("#AA999999"));
    }

    public void setColor(int color) {
        this.color = color;
        this.paint.setColor(this.color);
    }

    public int getColor() {
        return color;
    }

    abstract void drawOn(Canvas canvas);
}
