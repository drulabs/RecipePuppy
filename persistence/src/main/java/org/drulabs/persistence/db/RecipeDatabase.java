package org.drulabs.persistence.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import org.drulabs.persistence.entities.DBRecipe;

@Database(entities = {DBRecipe.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "recipes_db";
    private static RecipeDatabase INSTANCE;

    public static RecipeDatabase getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, RecipeDatabase.class, DATABASE_NAME)
                            //.allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract RecipeDao getRecipeDao();
}
