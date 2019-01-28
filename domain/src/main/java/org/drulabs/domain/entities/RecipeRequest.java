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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecipeRequest request = (RecipeRequest) o;

        if (pageNum != request.pageNum) return false;
        return searchQuery != null ? searchQuery.equals(request.searchQuery) : request.searchQuery == null;
    }

    @Override
    public int hashCode() {
        int result = searchQuery != null ? searchQuery.hashCode() : 0;
        result = 31 * result + pageNum;
        return result;
    }
}
