package org.drulabs.recipepuppy.ui.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.drulabs.presentation.entities.PresentationRecipe;
import org.drulabs.recipepuppy.R;
import org.drulabs.recipepuppy.utils.GlideApp;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeVH> {

    private List<PresentationRecipe> recipes;
    private ItemClickListener listener;

    public RecipeListAdapter(ItemClickListener clickListener) {
        this.recipes = new ArrayList<>();
        this.listener = clickListener;
    }

    public void populateRecipes(List<PresentationRecipe> recipes) {
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
        ImageView imgRecipe, imgStar;
        TextView tvRecipeName;
        ChipGroup ingredientChipsGroup;

        RecipeVH(@NonNull View itemView) {
            super(itemView);
            imgRecipe = itemView.findViewById(R.id.img_recipe);
            imgStar = itemView.findViewById(R.id.img_star);
            tvRecipeName = itemView.findViewById(R.id.tvRecipeName);
            ingredientChipsGroup = itemView.findViewById(R.id.ingredients_chips_group);
        }

        void bind(PresentationRecipe recipe) {
            GlideApp.with(itemView)
                    .load(recipe.getThumbnailUrl())
                    .placeholder(R.drawable.logo_tile)
                    .into(imgRecipe);

            if (recipe.isFavorite()) {
                imgStar.setImageResource(R.drawable.ic_star_mono);
            } else {
                imgStar.setImageResource(R.drawable.ic_star_white_mono);
            }

            imgStar.setOnClickListener(v -> listener.onStarTapped(recipe));
            imgRecipe.setOnClickListener(v -> listener.onRecipeItemTapped(recipe));

            tvRecipeName.setText(recipe.getName());
            ingredientChipsGroup.removeAllViews();

            for (String ingredient : recipe.getIngredientList()) {
                Chip ingredientChip = new Chip(itemView.getContext());
                ingredientChip.setText(ingredient);
                ingredientChipsGroup.addView(ingredientChip);
            }
        }
    }

    public interface ItemClickListener {
        void onRecipeItemTapped(PresentationRecipe recipe);

        void onStarTapped(PresentationRecipe recipe);
    }

}
