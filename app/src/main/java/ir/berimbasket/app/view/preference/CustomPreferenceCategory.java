package ir.berimbasket.app.view.preference;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.widget.TextView;

import ir.berimbasket.app.R;

public class CustomPreferenceCategory extends PreferenceCategory {

    public CustomPreferenceCategory(Context context) {
        super(context);
        setLayoutResource(R.layout.item_preference_category);
    }

    public CustomPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.item_preference_category);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        TextView titleView = (TextView) holder.findViewById(android.R.id.title);
        titleView.setTextColor(Color.RED);
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/yekan.ttf");
        titleView.setTypeface(typeface);
    }
}