package ir.berimbasket.app.ui.base;

/**
 * this is base of an item inside recyclerView
 */
public interface BaseItem {
    int DISMISSIBLE_INFO = 1;
    int STADIUM_ITEM = 2;

    int getViewType();
}
