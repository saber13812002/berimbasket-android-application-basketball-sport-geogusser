package ir.berimbasket.app.view.preference;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by mohammad hosein on 7/4/2017.
 */

public class CustomPreference extends Preference {

    public CustomPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomPreference(Context context) {
        super(context);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        TextView titleView = (TextView) holder.findViewById(android.R.id.title);
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/yekan.ttf");
        titleView.setTypeface(typeface);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
    }
}
