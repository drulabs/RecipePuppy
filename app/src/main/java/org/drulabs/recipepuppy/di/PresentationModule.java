package org.drulabs.recipepuppy.di;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.usecases.DeleteAllRecipesTask;
import org.drulabs.domain.usecases.DeleteRecipeTask;
import org.drulabs.domain.usecases.GetLastSavedRecipeTask;
import org.drulabs.domain.usecases.GetRecipesTask;
import org.drulabs.domain.usecases.GetSavedRecipesTask;
import org.drulabs.domain.usecases.SaveRecipeTask;
import org.drulabs.presentation.factory.FavoritesVMFactory;
import org.drulabs.presentation.factory.HomeVMFactory;
import org.drulabs.presentation.mapper.PresentationDomainMapper;
import org.drulabs.presentation.mapper.PresentationMapper;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module(includes = PresentationModule.Binder.class)
class PresentationModule {

    @Module
    interface Binder {

        @Binds
        PresentationMapper<DomainRecipe> bindsPresentationDomainMapper(PresentationDomainMapper mapper);
    }

    @Provides
    HomeVMFactory providesHomeVMFactory(PresentationMapper<DomainRecipe> mapper,
                                        SaveRecipeTask saveRecipeTask,
                                        DeleteRecipeTask deleteRecipeTask,
                                        GetRecipesTask getRecipesTask) {
        return new HomeVMFactory(mapper, saveRecipeTask, deleteRecipeTask, getRecipesTask);
    }

    @Provides
    FavoritesVMFactory providesFavoritesVMFactory(PresentationMapper<DomainRecipe> mapper,
                                                  GetSavedRecipesTask getSavedRecipesTask,
                                                  GetLastSavedRecipeTask getLastSavedRecipeTask,
                                                  DeleteAllRecipesTask deleteAllRecipesTask,
                                                  DeleteRecipeTask deleteRecipeTask) {
        return new FavoritesVMFactory(mapper, getSavedRecipesTask, getLastSavedRecipeTask,
                deleteAllRecipesTask, deleteRecipeTask);
    }
}
