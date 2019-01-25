package org.drulabs.recipepuppy.ui.common;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pchmn.materialchips.ChipsInput;

import org.drulabs.presentation.entities.PresentationRecipe;
import org.drulabs.recipepuppy.R;

import java.util.ArrayList;
import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeVH> {

    private List<PresentationRecipe> recipes;

    public RecipeListAdapter() {
        recipes = new ArrayList<>();
    }

    void populateRecipes(List<PresentationRecipe> recipes) {
        this.recipes.clear();
        this.recipes.addAll(recipes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View convertView = inflater.inflate(R.layout.item_recipe, parent, false);
        return (new RecipeVH(convertView));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeVH recipeVH, int index) {
        PresentationRecipe recipe = recipes.get(index);
        recipeVH.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class RecipeVH extends RecyclerView.ViewHolder {
        ImageView imgRecipe;
        TextView tvRecipeName;
        ChipsInput ingredientChips;

        RecipeVH(@NonNull View itemView) {
            super(itemView);
            imgRecipe = itemView.findViewById(R.id.img_recipe);
            tvRecipeName = itemView.findViewById(R.id.tvRecipeName);
            ingredientChips = itemView.findViewById(R.id.chips_ingredients);
        }

        void bind(PresentationRecipe recipe) {
            Glide.with(itemView)
                    .load(recipe.getThumbnailUrl())
                    .into(imgRecipe);
            tvRecipeName.setText(recipe.getName());
            for (String ingredient : recipe.getIngredientList()) {
                ingredientChips.addChip(ingredient, "ingredient");
            }
        }
    }

}
