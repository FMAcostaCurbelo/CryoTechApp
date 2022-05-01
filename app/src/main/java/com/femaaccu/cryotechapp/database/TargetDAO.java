package com.femaaccu.cryotechapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TargetDAO {

    @Query("SELECT * FROM Target")
    List<Target> getAll();

    @Query("SELECT * FROM Target WHERE currency_name = :currency_name LIMIT 1")
    Target findByName(String currency_name);

    @Insert
    Long insert(Target target);
    @Update
    void update(Target target);
    @Delete
    void delete(Target target);
}
