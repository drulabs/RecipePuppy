package org.drulabs.recipepuppy.di;

import org.drulabs.data.mapper.DataDomainMapper;
import org.drulabs.data.mapper.DataMapper;
import org.drulabs.data.repository.RecipeRepositoryImpl;
import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.repository.RecipeRepository;

import dagger.Binds;
import dagger.Module;

@Module
abstract class DataModule {

    @Binds
    abstract RecipeRepository bindsRecipeRepository(RecipeRepositoryImpl recipeRepositoryImpl);

    @Binds
    abstract DataMapper<DomainRecipe> bindsDataDomainMapper(DataDomainMapper mapper);

}
