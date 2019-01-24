package org.drulabs.presentation.mapper;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.presentation.entities.PresentationRecipe;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class PresentationDomainMapper implements PresentationMapper<DomainRecipe> {

    @Inject
    public PresentationDomainMapper() {
    }

    @Override
    public PresentationRecipe mapFrom(DomainRecipe domainRecipe) {
        List<String> ingredientList = Arrays.asList(domainRecipe.getIngredients().split(","));
        return (new PresentationRecipe(domainRecipe.getTitle(), domainRecipe.getHref(),
                ingredientList, domainRecipe.getThumbnail(), domainRecipe.isStarred()));
    }

    @Override
    public DomainRecipe mapTo(PresentationRecipe presentationRecipe) {
        String ingredients = presentationRecipe.getIngredientList().toString()
                .replace(", ", ",")
                .replaceAll("[\\[.\\]]", "");
        DomainRecipe domainRecipe = new DomainRecipe(presentationRecipe.getName(),
                presentationRecipe.getDetailsUrl(), ingredients, presentationRecipe
                .getThumbnailUrl());
        domainRecipe.setStarred(presentationRecipe.isFavorite());
        return domainRecipe;
    }
}
