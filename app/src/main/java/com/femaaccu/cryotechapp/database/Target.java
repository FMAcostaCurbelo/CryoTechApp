package com.femaaccu.cryotechapp.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "target")
public class Target {
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "currency_name")
    String currency_name;

    @ColumnInfo(name = "base_price")
    double base_price;

    @ColumnInfo(name = "target_price")
    double target_price;


    public Target(String currency_name, double base_price, double target_price ) {
        this.currency_name = currency_name;
        this.base_price = base_price;
        this.target_price = target_price;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrency_name() {
        return currency_name;
    }

    public void setCurrency_name(String currency_name) {
        this.currency_name = currency_name;
    }

    public double getBase_price() {
        return base_price;
    }

    public void setBase_price(double base_price) {
        this.base_price = base_price;
    }

    public double getTarget_price() {
        return target_price;
    }

    public void setTarget_price(double target_price) {
        this.target_price = target_price;
    }
}
