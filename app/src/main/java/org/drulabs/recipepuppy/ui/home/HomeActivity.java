package org.drulabs.recipepuppy.ui.home;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.drulabs.presentation.factory.HomeVMFactory;
import org.drulabs.presentation.viewmodels.HomeVM;
import org.drulabs.recipepuppy.R;
import org.drulabs.recipepuppy.ui.common.RecipeListAdapter;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    @Inject
    HomeVMFactory vmFactory;

    HomeVM homeVM;

    RecyclerView rvRecipeList;
    RecipeListAdapter recipeAdapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvRecipeList = findViewById(R.id.rvRecipeList);
        progressBar = findViewById(R.id.progress_indicator_home);

        recipeAdapter = new RecipeListAdapter();
        rvRecipeList.setLayoutManager(new LinearLayoutManager(this));
        rvRecipeList.setAdapter(recipeAdapter);

        homeVM = ViewModelProviders.of(this, vmFactory).get(HomeVM.class);

        homeVM.getRecipesLiveData().observe(this, listModel -> {
            Log.d(TAG, "onCreate: Data received");
            if (listModel != null) {
                if (!listModel.isLoading() && listModel.getError() == null) {
                    Log.d(TAG, "onCreate: DATA = " + listModel.getData());
                    hideLoader();
                    recipeAdapter.populateRecipes(listModel.getData());
                } else if (!listModel.isLoading() && listModel.getError() != null) {
                    Log.d(TAG, "onCreate: DATA = " + listModel.getError());
                } else if (listModel.isLoading()) {
                    Log.d(TAG, "onCreate: LOADING");
                    showLoader();
                }
            }
        });

        homeVM.searchRecipes("pasta", 2);
    }

    private void showLoader() {
        rvRecipeList.setAlpha(0.3f);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        rvRecipeList.setAlpha(1.0f);
        progressBar.setVisibility(View.GONE);
    }
}
