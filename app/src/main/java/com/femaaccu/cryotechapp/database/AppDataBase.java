package com.femaaccu.cryotechapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Favorites.class, Target.class}, version = 4, exportSchema = false)

public abstract class AppDataBase extends RoomDatabase {

    public abstract FavoriteDAO favouriteDAO();
    public abstract TargetDAO targetDAO();

    public static AppDataBase INSTANCE;

    public static AppDataBase getInstance(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context, AppDataBase.class, "currencydb")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
    public static AppDataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDataBase.class, "word_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
