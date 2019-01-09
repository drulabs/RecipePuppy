package org.drulabs.domain.entities;

import java.util.Objects;

public class Recipe {

    private String title;
    private String href;
    private String ingredients;
    private String thumbnail;

    public Recipe(String title, String href, String ingredients, String thumbnail) {
        this.title = title;
        this.href = href;
        this.ingredients = ingredients;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getHref() {
        return href;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(title, recipe.title) &&
                Objects.equals(href, recipe.href) &&
                Objects.equals(ingredients, recipe.ingredients) &&
                Objects.equals(thumbnail, recipe.thumbnail);
    }

    @Override
    public int hashCode() {

        return Objects.hash(title, href, ingredients, thumbnail);
    }
}
