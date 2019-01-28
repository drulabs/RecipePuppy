package org.drulabs.recipepuppy.ui.favorites;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.drulabs.presentation.entities.PresentationRecipe;
import org.drulabs.presentation.factory.FavoritesVMFactory;
import org.drulabs.presentation.viewmodels.FavoritesVM;
import org.drulabs.recipepuppy.R;
import org.drulabs.recipepuppy.ui.common.RecipeListAdapter;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.AndroidInjection;

public class FavoritesActivity extends AppCompatActivity implements RecipeListAdapter.ItemClickListener {

    @Inject
    FavoritesVMFactory favoritesVMFactory;

    FavoritesVM favoritesVM;
    RecyclerView rvSavedRecipeList;
    RecipeListAdapter recipeAdapter;
    ProgressBar progressBar;
    TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starred_recipes);

        Toolbar toolbar = findViewById(R.id.toolbar_favorites);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_mono);

        rvSavedRecipeList = findViewById(R.id.rv_saved_recipe_list);
        progressBar = findViewById(R.id.progress_indicator_favorites);
        tvError = findViewById(R.id.tv_error_favorites);

        recipeAdapter = new RecipeListAdapter(this);
        rvSavedRecipeList.setLayoutManager(new LinearLayoutManager(this));
        rvSavedRecipeList.setAdapter(recipeAdapter);

        favoritesVM = ViewModelProviders.of(this, favoritesVMFactory).get(FavoritesVM.class);

        loadSavedRecipes();
    }

    private void hideLoading() {
        rvSavedRecipeList.setAlpha(1.0f);
        tvError.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void showLoading() {
        rvSavedRecipeList.setAlpha(0.3f);
        tvError.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void showError(@Nullable Throwable throwable) {
        rvSavedRecipeList.setAlpha(0.3f);
        progressBar.setVisibility(View.GONE);
        if (throwable != null) {
            tvError.setText(String.format("%s - %s", getString(R.string.something_went_wrong),
                    throwable.getMessage()));
        } else {
            tvError.setText(getString(R.string.txt_no_data));
        }
        tvError.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_delete_all:
                handleDeleteAll();
                break;
            case R.id.action_last_saved_recipe:
                showLastSavedRecipeInfo();
                break;
            case R.id.action_about_recipe_puppy:
                final String recipePuppyURL = "http://www.recipepuppy.com/";
                Intent openUrlIntent = new Intent(Intent.ACTION_VIEW);
                openUrlIntent.setData(Uri.parse(recipePuppyURL));
                startActivity(openUrlIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLastSavedRecipeInfo() {
        favoritesVM.getLastSavedRecipe().observe(this, model -> {
            if (model != null && model.getData() != null) {
                PresentationRecipe lastRecipe = model.getData();
                Intent openUrlIntent = new Intent(Intent.ACTION_VIEW);
                openUrlIntent.setData(Uri.parse(lastRecipe.getDetailsUrl()));
                startActivity(openUrlIntent);
            }
        });
    }

    private void handleDeleteAll() {
        favoritesVM.deleteAllFavoriteRecipes().observe(this, operationStatus -> {
            if (operationStatus) {
                loadSavedRecipes();
            } else {
                Toast.makeText(this, getString(R.string.something_went_wrong),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSavedRecipes() {
        favoritesVM.getSavedRecipes().observe(this, listModel -> {
            if (listModel.isLoading()) {
                showLoading();
            } else if (listModel.getError() != null) {
                showError(listModel.getError());
            } else {
                hideLoading();
                if (listModel.getData() == null || listModel.getData().size() == 0) {
                    showError(null);
                } else {
                    recipeAdapter.populateRecipes(listModel.getData());
                }
            }
        });
    }

    @Override
    public void onRecipeItemTapped(PresentationRecipe recipe) {
        final String recipePuppyURL = recipe.getDetailsUrl();
        Intent openUrlIntent = new Intent(Intent.ACTION_VIEW);
        openUrlIntent.setData(Uri.parse(recipePuppyURL));
        startActivity(openUrlIntent);
    }

    @Override
    public void onStarTapped(PresentationRecipe recipe) {
        if (recipe.isFavorite()) {
            LiveData<Boolean> status = favoritesVM.deleteRecipeFromFavorite(recipe);
            showLoading();
            status.observe(this, operationStatus -> {
                hideLoading();
                if (operationStatus) {
                    Toast.makeText(this, getString(R.string.txt_msg_successful),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.something_went_wrong),
                            Toast.LENGTH_SHORT).show();
                }
                status.removeObservers(FavoritesActivity.this);
            });
        }
    }
}
