package ir.berimbasket.app.view.preference;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by mohammad hosein on 7/4/2017.
 */

public class CustomListPreference extends ListPreference {

    public CustomListPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomListPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomListPreference(Context context) {
        super(context);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        TextView titleView = (TextView) holder.findViewById(android.R.id.title);
        TextView summaryView = (TextView) holder.findViewById(android.R.id.summary);
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/yekan.ttf");
        titleView.setTypeface(typeface);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        summaryView.setTypeface(typeface);
        summaryView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    }


}
