package ir.berimbasket.app.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.PopupMenu;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ir.berimbasket.app.util.TypefaceManager;

/**
 * Created by mohammad hosein on 29/10/2017.
 */

public class FontHelper {

    private Context context;
    private String fontPath;

    /**
     * @param context
     * @param fontPath fontPath in asset
     */
    public FontHelper(Context context, String fontPath) {
        this.context = context;
        this.fontPath = fontPath;
    }

    public void bottomNavigationApplyFont(BottomNavigationView bottomNavigationView) {
        Menu m = bottomNavigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem menuItem = m.getItem(i);

            SubMenu subMenu = menuItem.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            applyFontToMenuItem(menuItem);
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = TypefaceManager.get(context, fontPath);
        SpannableString title = new SpannableString(mi.getTitle());
        title.setSpan(new CustomTypefaceSpan("", font), 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(title);
    }

    public void tablayoutApplyFont(TabLayout tabLayout) throws RuntimeException{
        //fixme throws RuntimeException not working well
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        Typeface font = TypefaceManager.get(context, fontPath);
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildesCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildesCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(font);
                }
            }
        }

    }

    public void popupApplyFont(PopupMenu popupMenu) throws RuntimeException{
        Menu menu = popupMenu.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem mi = menu.getItem(i);
            applyFontToMenuItem(mi);
        }
    }


}


