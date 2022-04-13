package com.femaaccu.cryotechapp.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorites")
public class Favorites {
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "currency_id")
    String currency_id;

    public Favorites(String currency_id) {
        this.currency_id = currency_id;
    }

    public String getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(String currency_id) {
        this.currency_id = currency_id;
    }
}
