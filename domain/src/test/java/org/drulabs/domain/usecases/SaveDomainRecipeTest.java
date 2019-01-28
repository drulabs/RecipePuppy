package org.drulabs.domain.usecases;

import org.drulabs.domain.repository.RecipeRepository;
import org.drulabs.domain.utils.TestFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class SaveDomainRecipeTest {

    private SaveRecipeTask saveRecipeTask;
    @Mock
    private RecipeRepository repository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        saveRecipeTask = new SaveRecipeTask(repository, Schedulers.trampoline(), Schedulers.trampoline());
    }

    @Test
    public void testProjectSavedUseCase() {
        when(repository.saveRecipe(any())).thenReturn(Completable.complete());
        TestObserver observer = saveRecipeTask.build(TestFactory.getRecipe()).test();
        observer.assertComplete();
    }

}
