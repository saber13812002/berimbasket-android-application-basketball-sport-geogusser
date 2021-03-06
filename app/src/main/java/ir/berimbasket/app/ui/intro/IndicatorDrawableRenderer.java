/*
 *   The MIT License (MIT)
 *
 *   Copyright (c) 2015 Cleveroad
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in all
 *   copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 */
package ir.berimbasket.app.ui.intro;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.content.res.AppCompatResources;

import com.cleveroad.slidingtutorial.Renderer;

import ir.berimbasket.app.R;

/**
 * {@link Renderer} implementation for drawing indicators with bitmap.
 */
@SuppressWarnings("WeakerAccess")
public class IndicatorDrawableRenderer implements Renderer {

    private Drawable mDrawableActive;
    private Drawable mDrawable;

    public static IndicatorDrawableRenderer create(@NonNull Context context) {
        return new IndicatorDrawableRenderer(context);
    }

    private IndicatorDrawableRenderer(@NonNull Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            mDrawableActive = ContextCompat.getDrawable(context, R.drawable.vec_checkbox_fill_circle_outline);
            mDrawable = ContextCompat.getDrawable(context, R.drawable.vec_checkbox_blank_circle_outline);
        } else{
            mDrawableActive = AppCompatResources.getDrawable(context, R.drawable.vec_checkbox_fill_circle_outline);
            mDrawable = AppCompatResources.getDrawable(context, R.drawable.vec_checkbox_blank_circle_outline);
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull RectF elementBounds, @NonNull Paint paint, boolean isActive) {
        Drawable drawable = isActive ? mDrawableActive : mDrawable;
        drawable.setBounds((int) elementBounds.left, (int) elementBounds.top,
                (int) elementBounds.right, (int) elementBounds.bottom);
        drawable.draw(canvas);
    }
}
