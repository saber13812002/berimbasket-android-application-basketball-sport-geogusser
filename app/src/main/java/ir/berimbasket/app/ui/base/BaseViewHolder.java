package ir.berimbasket.app.ui.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    public BaseViewHolder(View v) {
        super(v);
    }

    public abstract void bind(BaseItem item, int position);
}