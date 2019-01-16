package org.drulabs.persistence.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import org.drulabs.persistence.entities.DBRecipe;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM saved_recipes")
    Flowable<DBRecipe> getSavedRecipes();

    @Query("SELECT * FROM saved_recipes WHERE recipe_name =:name")
    Single<DBRecipe> lookupRecipe(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveRecipe(DBRecipe... recipe);

    @Delete
    void deleteRecipe(DBRecipe recipe);

    @Query("DELETE FROM saved_recipes")
    void deleteAllRecipes();

    @Query("SELECT * FROM saved_recipes ORDER BY created_at DESC LIMIT 1")
    Single<DBRecipe> getLastSavedRecipe();

}
