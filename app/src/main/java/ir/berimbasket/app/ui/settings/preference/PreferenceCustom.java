package ir.berimbasket.app.ui.settings.preference;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import ir.berimbasket.app.R;
import ir.berimbasket.app.util.TypefaceManager;

/**
 * Created by mohammad hosein on 7/4/2017.
 */

public class PreferenceCustom extends Preference {

    private Context context;

    public PreferenceCustom(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    public PreferenceCustom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public PreferenceCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public PreferenceCustom(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        TextView titleView = (TextView) holder.findViewById(android.R.id.title);
        Typeface typeface = TypefaceManager.get(context, context.getString(R.string.font_yekan));
        titleView.setTypeface(typeface);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
    }
}
