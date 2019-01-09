package org.drulabs.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NetworkRecipeResponse {

    @SerializedName("version")
    private double apiVersion;

    @SerializedName("title")
    private String apiTitle;

    @SerializedName("href")
    private String apiHomePage;

    @SerializedName("results")
    private List<NetworkRecipe> recipes;

    public NetworkRecipeResponse(double apiVersion, String apiTitle, String apiHomePage,
                                 List<NetworkRecipe> recipes) {
        this.apiVersion = apiVersion;
        this.apiTitle = apiTitle;
        this.apiHomePage = apiHomePage;
        this.recipes = recipes;
    }

    public double getApiVersion() {
        return apiVersion;
    }

    public String getApiTitle() {
        return apiTitle;
    }

    public String getApiHomePage() {
        return apiHomePage;
    }

    public List<NetworkRecipe> getRecipes() {
        return recipes;
    }
}
