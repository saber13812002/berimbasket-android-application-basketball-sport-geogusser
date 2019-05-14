package ir.berimbasket.app.ui.common;

import android.view.View;
import android.widget.TextView;

import ir.berimbasket.app.R;
import ir.berimbasket.app.ui.base.BaseItem;
import ir.berimbasket.app.ui.base.BaseViewHolder;
import ir.berimbasket.app.ui.common.model.DismissibleInfo;

public class DismissibleHolder extends BaseViewHolder {
        View view;
        TextView header, message, action, skip;

        DismissableCallback callback;

        public DismissibleHolder(View view) {
            super(view);
            this.view = view;
            this.header = view.findViewById(R.id.txtDismissibleHeader);
            this.message = view.findViewById(R.id.txtDismissibleMessage);
            this.action = view.findViewById(R.id.txtDismissibleAction);
            this.skip = view.findViewById(R.id.txtDismissibleSkip);
        }

        public void setCallback(DismissableCallback callback) {
            this.callback = callback;
        }

        @Override
        public void bind(BaseItem item, int position) {
            DismissibleInfo dismissible = (DismissibleInfo) item;
            header.setText(dismissible.getHeader());
            message.setText(dismissible.getMessage());
            action.setText(dismissible.getAction());
            skip.setText(dismissible.getSkip());
            action.setOnClickListener( v -> {
                if (callback != null) {
                    callback.onDismissibleActionClick(dismissible);
                }
            });
            skip.setOnClickListener( v -> {
                if (callback != null) {
                    callback.onDismissibleSkipClick(dismissible);
                }
            });
        }
    }