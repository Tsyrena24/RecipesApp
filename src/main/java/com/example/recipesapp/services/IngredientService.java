package com.example.recipesapp.services;

import com.example.recipesapp.dto.IngredientDTO;
import com.example.recipesapp.model.Ingredient;

import java.util.List;

public interface IngredientService {
    IngredientDTO addIngredient(Ingredient ingredient);

    IngredientDTO getIngredient(int id);

    List<IngredientDTO> getAllIngredient();

    IngredientDTO editIngredient(int id, Ingredient ingredient);

    IngredientDTO deleteIngredient(int id);
}
