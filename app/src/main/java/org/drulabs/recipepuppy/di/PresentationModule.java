package org.drulabs.recipepuppy.di;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.usecases.DeleteRecipeTask;
import org.drulabs.domain.usecases.GetRecipesTask;
import org.drulabs.domain.usecases.SaveRecipeTask;
import org.drulabs.presentation.mapper.PresentationDomainMapper;
import org.drulabs.presentation.mapper.PresentationMapper;
import org.drulabs.presentation.viewmodels.HomeVM;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module(includes = PresentationModule.Binder.class)
class PresentationModule {

    interface Binder {

        @Binds
        PresentationMapper<DomainModule> bindsPresentationDomainMapper(PresentationDomainMapper mapper);
    }

    @Provides
    HomeVM providesHomeViewModel(PresentationMapper<DomainRecipe> mapper,
                                 GetRecipesTask getRecipesTask,
                                 SaveRecipeTask saveRecipeTask,
                                 DeleteRecipeTask deleteRecipeTask) {
        return new HomeVM(mapper, getRecipesTask, saveRecipeTask, deleteRecipeTask);
    }
}
