package bootpay.co.kr.samplepayment.model;

public final class Item {
    private Item() {
        this(null, 0, null, 0); /* not allow */
    }

    public Item(String name, int quantity, String primaryKey, double price) {
        this.name = name;
        this.quantity = quantity;
        this.primaryKey = primaryKey;
        this.price = price;
    }

    private final String name;
    private final int quantity;
    private final String primaryKey;
    private final double price;

    public final String getName() {
        return name;
    }

    public final int getQuantity() {
        return quantity;
    }

    public final String getPrimaryKey() {
        return primaryKey;
    }

    public final double getPrice() {
        return price;
    }
}
