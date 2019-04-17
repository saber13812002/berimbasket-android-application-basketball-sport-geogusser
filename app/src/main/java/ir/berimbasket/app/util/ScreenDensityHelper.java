package ir.berimbasket.app.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import ir.berimbasket.app.R;
import ir.berimbasket.app.data.network.model.Profile;

public class ScreenDensityHelper {

    private final static float SCREEN_DENSITY_MDPI = 1f;
    private final static float SCREEN_DENSITY_HDPI = 1.5f;
    private final static float SCREEN_DENSITY_XHDPI = 2f;
    private final static float SCREEN_DENSITY_XXHDPI = 3f;
    private final static float SCREEN_DENSITY_XXXHDPI = 4f;

    public static float getDisplayDensity(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.density;
    }

    /**
     * Load avatar to an imageView based on screen density
     * @param target Avatar image view
     * @param avatar Avatar class
     * @param context Current context
     */
    public static void loadAvatarBasedOnDensity(ImageView target, Profile.Avatar avatar, Context context) {
        String avatarUri;
        float screenDensity = getDisplayDensity(context);
        if (screenDensity <= ScreenDensityHelper.SCREEN_DENSITY_MDPI) {
            avatarUri = avatar.get_24dips();
        } else if (screenDensity <= ScreenDensityHelper.SCREEN_DENSITY_XHDPI) {
            avatarUri = avatar.get_48dips();
        } else if (screenDensity > ScreenDensityHelper.SCREEN_DENSITY_XHDPI) {
            avatarUri = avatar.get_96dips();
        } else {
            avatarUri = avatar.get_96dips();
        }

        Picasso.with(context)
                .load(avatarUri)
                .placeholder(R.drawable.profile_default)
                .error(R.drawable.profile_default)
                .into(target);
    }

}