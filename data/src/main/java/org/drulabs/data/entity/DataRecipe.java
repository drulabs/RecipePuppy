package org.drulabs.data.entity;

import java.util.List;
import java.util.Objects;

public class DataRecipe {

    private String name;
    private String detailsUrl;
    private List<String> ingredients;
    private String recipeImageUrl;

    public DataRecipe(String name, String detailsUrl, List<String> ingredients, String recipeImageUrl) {
        this.name = name;
        this.detailsUrl = detailsUrl;
        this.ingredients = ingredients;
        this.recipeImageUrl = recipeImageUrl;
    }

    public String getName() {
        return name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataRecipe that = (DataRecipe) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(detailsUrl, that.detailsUrl) &&
                Objects.equals(ingredients, that.ingredients) &&
                Objects.equals(recipeImageUrl, that.recipeImageUrl);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, detailsUrl, ingredients, recipeImageUrl);
    }
}
