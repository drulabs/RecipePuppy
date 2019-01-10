package org.drulabs.domain.entities;

import java.util.Objects;

public class DomainRecipe {

    private String title;
    private String href;
    private String ingredients;
    private String thumbnail;

    public DomainRecipe(String title, String href, String ingredients, String thumbnail) {
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
        DomainRecipe domainRecipe = (DomainRecipe) o;
        return Objects.equals(title, domainRecipe.title) &&
                Objects.equals(href, domainRecipe.href) &&
                Objects.equals(ingredients, domainRecipe.ingredients) &&
                Objects.equals(thumbnail, domainRecipe.thumbnail);
    }

    @Override
    public int hashCode() {

        return Objects.hash(title, href, ingredients, thumbnail);
    }
}
