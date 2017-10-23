package ir.berimbasket.app.view.preference;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.widget.TextView;

import ir.berimbasket.app.R;
import ir.berimbasket.app.util.TypefaceManager;

public class CustomPreferenceCategory extends PreferenceCategory {

    private Context context;

    public CustomPreferenceCategory(Context context) {
        super(context);
        setLayoutResource(R.layout.item_preference_category);
        this.context = context;
    }

    public CustomPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.item_preference_category);
        this.context = context;
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        TextView titleView = (TextView) holder.findViewById(android.R.id.title);
        titleView.setTextColor(Color.RED);
        Typeface typeface = TypefaceManager.get(context, context.getString(R.string.font_yekan));
        titleView.setTypeface(typeface);
    }
}