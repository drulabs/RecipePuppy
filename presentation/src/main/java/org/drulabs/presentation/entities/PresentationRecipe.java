package org.drulabs.presentation.entities;

import java.util.List;

public class PresentationRecipe {

    private String name;
    private String detailsUrl;
    private List<String> ingredientList;
    private String thumbnailUrl;
    private boolean favorite;

    public PresentationRecipe(String name, String detailsUrl, List<String> ingredientList, String
            thumbnailUrl, boolean favorite) {
        this.name = name;
        this.detailsUrl = detailsUrl;
        this.ingredientList = ingredientList;
        this.thumbnailUrl = thumbnailUrl;
        this.favorite = favorite;
    }

    public String getName() {
        return name;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public List<String> getIngredientList() {
        return ingredientList;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PresentationRecipe that = (PresentationRecipe) o;

        if (!name.equals(that.name)) return false;
        if (!detailsUrl.equals(that.detailsUrl)) return false;
        if (ingredientList != null ? !ingredientList.equals(that.ingredientList) : that.ingredientList != null)
            return false;
        return thumbnailUrl != null ? thumbnailUrl.equals(that.thumbnailUrl) : that.thumbnailUrl == null;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + detailsUrl.hashCode();
        result = 31 * result + (ingredientList != null ? ingredientList.hashCode() : 0);
        result = 31 * result + (thumbnailUrl != null ? thumbnailUrl.hashCode() : 0);
        return result;
    }
}
