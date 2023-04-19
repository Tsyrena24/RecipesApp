package com.example.recipesapp.services;

import com.example.recipesapp.dto.RecipeDTO;
import com.example.recipesapp.model.Recipe;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface RecipeService {
    RecipeDTO addRecipe(Recipe recipe);

    RecipeDTO getRecipe(int id);

    List<RecipeDTO> getAllRecipe();

    RecipeDTO editRecipe(int id, Recipe recipe);

    RecipeDTO deleteRecipe(int id);

    Path createTextDataFile() throws IOException;
}
