package org.drulabs.presentation.viewmodels;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.entities.RecipeRequest;
import org.drulabs.domain.usecases.DeleteRecipeTask;
import org.drulabs.domain.usecases.GetRecipesTask;
import org.drulabs.domain.usecases.LookupRecipeTask;
import org.drulabs.domain.usecases.SaveRecipeTask;
import org.drulabs.presentation.entities.PresentationRecipe;
import org.drulabs.presentation.mapper.PresentationMapper;
import org.drulabs.presentation.mapper.PresentattionDomainMapper;
import org.drulabs.presentation.utils.Generator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Objects;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class HomeVMTest {
    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    private PresentationMapper<DomainRecipe> mapper = new PresentattionDomainMapper();

    @Mock
    private GetRecipesTask getRecipesTask;
    @Mock
    private SaveRecipeTask saveRecipeTask;
    @Mock
    private DeleteRecipeTask deleteRecipeTask;
    @Mock
    private LookupRecipeTask lookupRecipeTask;

    private HomeVM homeVM;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        homeVM = new HomeVM(mapper, getRecipesTask, saveRecipeTask, deleteRecipeTask,
                lookupRecipeTask);
    }

    @Test
    public void testSearchRecipes() {
        PresentationRecipe presentationRecipe = Generator.generateRecipe("01");
        DomainRecipe domainRecipe = mapper.mapTo(presentationRecipe);

        RecipeRequest request = new RecipeRequest("test", 1);

        Observable<DomainRecipe> obs = Observable.just(domainRecipe);
        when(getRecipesTask.run(request)).thenReturn(obs);
        when(lookupRecipeTask.run(any())).thenReturn(Single.just(domainRecipe));

        homeVM.getRecipesLiveData()
                .observeForever(listModel -> {
                    assert listModel != null;

                    assertTrue(listModel.isLoading() || listModel.getData().contains
                            (presentationRecipe));

                });

        homeVM.searchRecipes(request.getSearchQuery(), request.getPageNum());

        // assert homeVM.getRecipesLiveData().getValue().getData().contains(presentationRecipe);
    }

    @Test
    public void testFavoritesRecipe() {
        PresentationRecipe presentationRecipe = Generator.generateRecipe("01");
        DomainRecipe domainRecipe = mapper.mapTo(presentationRecipe);

        RecipeRequest request = new RecipeRequest("test", 1);

        Observable<DomainRecipe> obs = Observable.just(domainRecipe);
        when(getRecipesTask.run(request)).thenReturn(obs);
        when(lookupRecipeTask.run(any())).thenReturn(Single.just(domainRecipe));

        homeVM.getRecipesLiveData()
                .observeForever(listModel -> {
                    assert listModel != null;

                    assertTrue(listModel.isLoading()
                            || listModel.getData().get(0).isFavorite());
                });

        homeVM.searchRecipes(request.getSearchQuery(), request.getPageNum());

        if (!Objects.requireNonNull(homeVM.getRecipesLiveData().getValue()).isLoading()) {
            assertTrue(homeVM.getRecipesLiveData().getValue().getData().get(0).isFavorite());
        }
    }

    @Test
    public void testSaveRecipeAsFavorite() {
        PresentationRecipe presentationRecipe = Generator.generateRecipe("01");
        DomainRecipe domainRecipe = mapper.mapTo(presentationRecipe);

        when(saveRecipeTask.run(domainRecipe)).thenReturn(Completable.complete());

        homeVM.getSaveRecipeStatus()
                .observeForever(Assert::assertTrue);

        homeVM.saveRecipeAsFavorite(presentationRecipe);
    }

    @Test
    public void testDeleteRecipeFromFavorite() {
        PresentationRecipe presentationRecipe = Generator.generateRecipe("01");
        DomainRecipe domainRecipe = mapper.mapTo(presentationRecipe);

        when(deleteRecipeTask.run(domainRecipe)).thenReturn(Completable.complete().subscribeOn
                (Schedulers.trampoline()).observeOn(Schedulers.trampoline()));

        homeVM.getDeleteRecipeStatus()
                .observeForever(aBoolean -> {
                    System.out.println("testDeleteRecipeFromFavorite Observe: called");
                    assertEquals("testDeleteRecipeFromFavorite", true, aBoolean);
                });

        homeVM.deleteRecipeFromFavorite(presentationRecipe);
    }
}
