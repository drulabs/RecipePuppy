package org.drulabs.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class NetworkRecipe {

    @SerializedName("title")
    private String name;

    @SerializedName("href")
    private String detailsPage;

    @SerializedName("ingredients")
    private String commaSeparatedIngredients;

    @SerializedName("thumbnail")
    private String imageUrl;

    public NetworkRecipe(String name, String detailsPage, String commaSeparatedIngredients,
                          String imageUrl) {
        this.name = name;
        this.detailsPage = detailsPage;
        this.commaSeparatedIngredients = commaSeparatedIngredients;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getDetailsPage() {
        return detailsPage;
    }

    public String getCommaSeparatedIngredients() {
        return commaSeparatedIngredients;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NetworkRecipe that = (NetworkRecipe) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(detailsPage, that.detailsPage) &&
                Objects.equals(commaSeparatedIngredients, that.commaSeparatedIngredients) &&
                Objects.equals(imageUrl, that.imageUrl);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, detailsPage, commaSeparatedIngredients, imageUrl);
    }
}
