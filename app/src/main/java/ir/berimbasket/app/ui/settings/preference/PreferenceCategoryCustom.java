package ir.berimbasket.app.ui.settings.preference;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.widget.TextView;

import ir.berimbasket.app.R;

public class PreferenceCategoryCustom extends PreferenceCategory {

    private Context context;

    public PreferenceCategoryCustom(Context context) {
        super(context);
        setLayoutResource(R.layout.item_preference_category);
        this.context = context;
    }

    public PreferenceCategoryCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.item_preference_category);
        this.context = context;
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        TextView titleView = (TextView) holder.findViewById(android.R.id.title);
        titleView.setTextColor(Color.RED);
    }
}