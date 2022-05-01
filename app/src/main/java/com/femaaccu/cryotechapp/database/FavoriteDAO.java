package com.femaaccu.cryotechapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FavoriteDAO {
        @Query("SELECT * FROM Favorites")
        List<Favorites> getAll();

        @Query("SELECT * FROM Favorites WHERE currency_id = :currency_id LIMIT 1")
        Favorites findByName(String currency_id);

        @Insert
        Long insert(Favorites favorites);
        @Update
        void update(Favorites favorites);
        @Delete
        void delete(Favorites favorites);

}
