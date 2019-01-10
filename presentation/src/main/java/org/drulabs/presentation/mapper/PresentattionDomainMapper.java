package org.drulabs.presentation.mapper;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.presentation.entities.PresentationRecipe;

import java.util.Arrays;
import java.util.List;

public class PresentattionDomainMapper implements PresentationMapper<DomainRecipe> {

    @Override
    public PresentationRecipe mapFrom(DomainRecipe domainRecipe) {
        List<String> ingredientList = Arrays.asList(domainRecipe.getIngredients().split(","));
        return (new PresentationRecipe(domainRecipe.getTitle(), domainRecipe.getHref(),
                ingredientList, domainRecipe.getThumbnail()));
    }

    @Override
    public DomainRecipe mapTo(PresentationRecipe presentationRecipe) {
        String ingredients = presentationRecipe.getIngredientList().toString()
                .replace(", ", ",")
                .replaceAll("[\\[.\\]]", "");
        return (new DomainRecipe(presentationRecipe.getName(), presentationRecipe.getDetailsUrl()
                , ingredients, presentationRecipe.getThumbnailUrl()));
    }
}
