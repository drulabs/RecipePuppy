package org.drulabs.domain.entities;

public class RecipeRequest {

    private String searchQuery;
    private int pageNum;

    public RecipeRequest(String searchQuery, int pageNum) {
        this.searchQuery = searchQuery;
        this.pageNum = pageNum;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public int getPageNum() {
        return pageNum;
    }
}
