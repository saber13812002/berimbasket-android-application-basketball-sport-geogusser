package ir.berimbasket.app.ui.common.swipe_showcase;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

class TutView extends View {

    static final int DEFAULT_ALPHA_COLOR = 200;
    int backgroundOverlayColor = Color.argb(DEFAULT_ALPHA_COLOR, 0, 0, 0);
    List<Shape> shapes;

    public TutView(Context context) {
        super(context);
        initialize();
    }

    public TutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public TutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(backgroundOverlayColor);
        for (Shape shape : shapes) {
            shape.drawOn(canvas);
        }

    }

    private void initialize() {
        shapes = new ArrayList<>();
        setDrawingCacheEnabled(true);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }
}
