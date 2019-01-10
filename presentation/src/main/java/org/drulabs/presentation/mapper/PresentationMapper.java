package org.drulabs.presentation.mapper;

import org.drulabs.presentation.entities.PresentationRecipe;

public interface PresentationMapper<T> {

    PresentationRecipe mapFrom(T t);

    T mapTo(PresentationRecipe presentationRecipe);

}
