package org.drulabs.persistence.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import org.drulabs.persistence.entities.DBRecipe;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM saved_recipes")
    Observable<List<DBRecipe>> getSavedRecipes();

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
