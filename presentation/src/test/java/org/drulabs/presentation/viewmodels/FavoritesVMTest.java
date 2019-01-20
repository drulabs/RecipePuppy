package org.drulabs.presentation.viewmodels;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.usecases.DeleteAllRecipesTask;
import org.drulabs.domain.usecases.DeleteRecipeTask;
import org.drulabs.domain.usecases.GetLastSavedRecipeTask;
import org.drulabs.domain.usecases.GetSavedRecipesTask;
import org.drulabs.presentation.entities.PresentationRecipe;
import org.drulabs.presentation.mapper.PresentationMapper;
import org.drulabs.presentation.mapper.PresentattionDomainMapper;
import org.drulabs.presentation.utils.Generator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class FavoritesVMTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private PresentationMapper<DomainRecipe> mapper = new PresentattionDomainMapper();

    @Mock
    private GetSavedRecipesTask getSavedRecipesTask;
    @Mock
    private GetLastSavedRecipeTask getLastSavedRecipeTask;
    @Mock
    private DeleteAllRecipesTask deleteAllRecipesTask;
    @Mock
    private DeleteRecipeTask deleteRecipeTask;

    private FavoritesVM favoritesVM;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        favoritesVM = new FavoritesVM(mapper, getSavedRecipesTask, getLastSavedRecipeTask,
                deleteAllRecipesTask, deleteRecipeTask);
    }

    @Test
    public void testGetSavedRecipes() {
        PresentationRecipe presentationRecipe = Generator.generateRecipe("dsds");
        DomainRecipe domainRecipe = mapper.mapTo(presentationRecipe);

        when(getSavedRecipesTask.run(null)).thenReturn(Observable.just(domainRecipe));

        favoritesVM.getSavedRecipesData()
                .observeForever(listModel -> {
                    assert listModel != null;
                    assert listModel.getData() == null || listModel.getData().contains
                            (presentationRecipe);
                });
        favoritesVM.fetchSavedRecipes();
    }

    @Test
    public void testGetLastSavedRecipe() {
        PresentationRecipe presentationRecipe = Generator.generateRecipe("dsds");
        DomainRecipe domainRecipe = mapper.mapTo(presentationRecipe);

        when(getLastSavedRecipeTask.run(null)).thenReturn(Single.just(domainRecipe));

        favoritesVM.getLastSavedRecipe()
                .observeForever(model -> {
                    assert model != null;
                    assert model.getData() == null || model.getData().equals(presentationRecipe);
                });
    }

    @Test
    public void testDeleteAllRecipes() {

        when(deleteAllRecipesTask.run(null)).thenReturn(Completable.complete());

        favoritesVM.getDeleteAllRecipeStatus()
                .observeForever(aBoolean -> {
                    System.out.println("deleteAllFavoriteRecipes Observe: called");
                    assertEquals("deleteAllFavoriteRecipes", true, aBoolean);
                });

        favoritesVM.deleteAllFavoriteRecipes();
    }

    @Test
    public void testDeleteRecipeFromFavorite() {
        PresentationRecipe presentationRecipe = Generator.generateRecipe("01");
        DomainRecipe domainRecipe = mapper.mapTo(presentationRecipe);

        when(deleteRecipeTask.run(domainRecipe)).thenReturn(Completable.complete().subscribeOn
                (Schedulers.trampoline()).observeOn(Schedulers.trampoline()));

        favoritesVM.getDeleteRecipeStatus()
                .observeForever(aBoolean -> {
                    System.out.println("testDeleteRecipeFromFavorite Observe: called");
                    assertEquals("testDeleteRecipeFromFavorite", true, aBoolean);
                });

        favoritesVM.deleteRecipeFromFavorite(presentationRecipe);
    }

}
