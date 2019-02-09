package org.drulabs.presentation.viewmodels;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.repository.RecipeRepository;
import org.drulabs.domain.usecases.DeleteAllRecipesTask;
import org.drulabs.domain.usecases.DeleteRecipeTask;
import org.drulabs.domain.usecases.GetLastSavedRecipeTask;
import org.drulabs.domain.usecases.GetSavedRecipesTask;
import org.drulabs.presentation.data.Model;
import org.drulabs.presentation.entities.PresentationRecipe;
import org.drulabs.presentation.mapper.PresentationDomainMapper;
import org.drulabs.presentation.mapper.PresentationMapper;
import org.drulabs.presentation.utils.Generator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class FavoritesVMTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private PresentationMapper<DomainRecipe> mapper = new PresentationDomainMapper();

    @Mock
    private RecipeRepository recipeRepository;

    private FavoritesVM favoritesVM;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        GetSavedRecipesTask getSavedRecipesTask = new GetSavedRecipesTask(recipeRepository,
                Schedulers.trampoline(), Schedulers.trampoline());
        GetLastSavedRecipeTask getLastSavedRecipeTask =
                new GetLastSavedRecipeTask(recipeRepository, Schedulers.trampoline(),
                        Schedulers.trampoline());
        DeleteAllRecipesTask deleteAllRecipesTask = new DeleteAllRecipesTask(recipeRepository,
                Schedulers.trampoline(), Schedulers.trampoline());
        DeleteRecipeTask deleteRecipeTask = new DeleteRecipeTask(recipeRepository,
                Schedulers.trampoline(), Schedulers.trampoline());

        favoritesVM = new FavoritesVM(mapper, getSavedRecipesTask, getLastSavedRecipeTask,
                deleteAllRecipesTask, deleteRecipeTask);
    }

    @Test
    public void testGetSavedRecipes() {
        PresentationRecipe presentationRecipe = Generator.generateRecipe("dsds");
        DomainRecipe domainRecipe = mapper.mapTo(presentationRecipe);

        when(recipeRepository.getSavedRecipes())
                .thenReturn(Observable.just(Collections.singletonList(domainRecipe)));

        LiveData<Model<List<PresentationRecipe>>> savedRecipes = favoritesVM.getSavedRecipes();
        savedRecipes.observeForever(Generator.generateMockObserver());

//        verify(getSavedRecipesTask).run(observableCaptor.capture(), eq(null));
//        observableCaptor.getValue().onNext(Collections.singletonList(domainRecipe));
//        observableCaptor.getValue().onComplete();

        assertEquals(presentationRecipe, savedRecipes.getValue().getData().get(0));
    }

    @Test
    public void testGetLastSavedRecipe() {
        PresentationRecipe presentationRecipe = Generator.generateRecipe("dsds");
        DomainRecipe domainRecipe = mapper.mapTo(presentationRecipe);

        when(recipeRepository.getLastSavedRecipe()).thenReturn(Single.just(domainRecipe));

        LiveData<Model<PresentationRecipe>> lastRecipe = favoritesVM.getLastSavedRecipe();
        lastRecipe.observeForever(Generator.generateMockObserver());

//        verify(getLastSavedRecipeTask).run(singleCaptor.capture(), eq(null));
//        singleCaptor.getValue().onSuccess(domainRecipe);

        assertEquals(presentationRecipe, lastRecipe.getValue().getData());
    }

    @Test
    public void testDeleteAllRecipes() {

        when(recipeRepository.deleteAllRecipes()).thenReturn(Completable.complete());

        LiveData<Boolean> status = favoritesVM.deleteAllFavoriteRecipes();
        status.observeForever(Generator.generateMockObserver());

//        verify(deleteAllRecipesTask).run(completableCaptor.capture(), eq(null));
//        completableCaptor.getValue().onComplete();

        assertEquals(true, status.getValue());
    }

    @Test
    public void failure_testDeleteAllRecipes() {

        when(recipeRepository.deleteAllRecipes()).thenReturn(Completable.error(new Throwable()));

        LiveData<Boolean> status = favoritesVM.deleteAllFavoriteRecipes();
        status.observeForever(Generator.generateMockObserver());

//        verify(deleteAllRecipesTask).run(completableCaptor.capture(), eq(null));
//        completableCaptor.getValue().onError(new Exception());

        assertEquals(false, status.getValue());
    }

    @Test
    public void testDeleteRecipeFromFavorite() {
        PresentationRecipe presentationRecipe = Generator.generateRecipe("01");
        DomainRecipe domainRecipe = mapper.mapTo(presentationRecipe);

        when(recipeRepository.deleteRecipe(domainRecipe)).thenReturn(Completable.complete());

        LiveData<Boolean> status = favoritesVM.deleteRecipeFromFavorite(presentationRecipe);
        status.observeForever(Generator.generateMockObserver());

//        verify(deleteRecipeTask).run(completableCaptor.capture(), eq(domainRecipe));
//        completableCaptor.getValue().onComplete();

        assertEquals(true, status.getValue());
    }

    @Test
    public void testDeleteRecipeFromFavoriteFAILURE() {
        PresentationRecipe presentationRecipe = Generator.generateRecipe("01");
        DomainRecipe domainRecipe = mapper.mapTo(presentationRecipe);

        when(recipeRepository.deleteRecipe(domainRecipe))
                .thenReturn(Completable.error(new Throwable()));

        LiveData<Boolean> status = favoritesVM.deleteRecipeFromFavorite(presentationRecipe);
        status.observeForever(Generator.generateMockObserver());

//        verify(deleteRecipeTask).run(completableCaptor.capture(), eq(domainRecipe));
//        completableCaptor.getValue().onError(new Exception());

        assertEquals(false, status.getValue());
    }

}
