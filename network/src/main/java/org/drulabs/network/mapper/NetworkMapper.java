package org.drulabs.network.mapper;

import org.drulabs.network.entities.NetworkRecipe;

public interface NetworkMapper<T> {

    NetworkRecipe mapFrom(T t);

    T mapTo(NetworkRecipe networkRecipe);
}
