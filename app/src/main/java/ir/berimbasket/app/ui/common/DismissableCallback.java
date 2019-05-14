package ir.berimbasket.app.ui.common;

import ir.berimbasket.app.ui.common.model.DismissibleInfo;

public interface DismissableCallback {
    void onDismissibleActionClick(DismissibleInfo dismissibleInfo);

    void onDismissibleSkipClick(DismissibleInfo dismissibleInfo);
}
