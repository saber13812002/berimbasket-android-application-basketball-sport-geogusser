package ir.berimbasket.app.ui.donate;

public enum Donate {

    PRICE_1_THOUSAND_TOMAN("donate_1000_toman", 1000),
    PRICE_2_THOUSAND_TOMAN("donate_2000_toman", 2000),
    PRICE_5_THOUSAND_TOMAN("donate_5000_toman", 5000),
    PRICE_10_THOUSAND_TOMAN("donate_10000_toman", 10000);

    private String sku;
    private int requestCode;

    private Donate(String sku, int requestCode) {
        this.sku = sku;
        this.requestCode = requestCode;
    }

    public String getSku() {
        return sku;
    }

    public int getRequestCode() {
        return requestCode;
    }
}
