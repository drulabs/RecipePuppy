package org.drulabs.data.mapper;

import org.drulabs.data.entity.DataRecipe;

public interface DomainMapper<R> {
    R mapTo(DataRecipe dataRecipe);

    DataRecipe mapFrom(R r);
}
