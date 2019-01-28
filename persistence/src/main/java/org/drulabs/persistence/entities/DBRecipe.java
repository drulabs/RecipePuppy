package org.drulabs.persistence.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import androidx.annotation.NonNull;

import org.drulabs.persistence.converters.IngredientListConverter;

import java.util.List;
import java.util.Objects;

@Entity(
        tableName = "saved_recipes",
        indices = {@Index(value = {"recipe_name", "details_url"}, unique = true)}
)
public class DBRecipe {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "recipe_name")
    private String recipeName;

    @ColumnInfo(name = "details_url")
    private String detailsUrl;

    @TypeConverters(IngredientListConverter.class)
    @ColumnInfo(name = "ingredients")
    private List<String> ingredients;

    @ColumnInfo(name = "recipe_image")
    private String recipeImageUrl;

    @ColumnInfo(name = "created_at")
    private long createTime = System.currentTimeMillis();

    public DBRecipe(String recipeName, String detailsUrl, List<String> ingredients, String recipeImageUrl) {
        this.recipeName = recipeName;
        this.detailsUrl = detailsUrl;
        this.ingredients = ingredients;
        this.recipeImageUrl = recipeImageUrl;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public String getRecipeImageUrl() {
        return recipeImageUrl;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DBRecipe dbRecipe = (DBRecipe) o;
        return Objects.equals(recipeName, dbRecipe.recipeName) &&
                Objects.equals(detailsUrl, dbRecipe.detailsUrl) &&
                Objects.equals(ingredients, dbRecipe.ingredients) &&
                Objects.equals(recipeImageUrl, dbRecipe.recipeImageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeName, detailsUrl, ingredients, recipeImageUrl);
    }
}
