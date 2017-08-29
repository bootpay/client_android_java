package kr.co.bootpay.model;

import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

public final class Item {
    private Item() {
        this("", 0, "", 0); /* not allow */
    }

    public Item(@NonNull String name, @IntRange(from = 0) int quantity, @NonNull String primaryKey, @FloatRange(from = 0) double price) {
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
