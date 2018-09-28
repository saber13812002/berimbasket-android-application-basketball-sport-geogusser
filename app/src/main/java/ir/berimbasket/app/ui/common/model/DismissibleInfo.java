package ir.berimbasket.app.ui.common.model;

import ir.berimbasket.app.ui.base.BaseItem;

public class DismissibleInfo implements BaseItem {
    private String header;
    private String message;
    private String action;
    private String skip;

    public DismissibleInfo(String header, String message, String action, String skip) {
        this.header = header;
        this.message = message;
        this.action = action;
        this.skip = skip;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSkip() {
        return skip;
    }

    public void setSkip(String skip) {
        this.skip = skip;
    }

    @Override
    public int getViewType() {
        return BaseItem.DISMISSIBLE_INFO;
    }
}
