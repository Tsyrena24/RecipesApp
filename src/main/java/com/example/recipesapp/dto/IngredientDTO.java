package com.example.recipesapp.dto;

import com.example.recipesapp.model.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IngredientDTO {
    private final int id;
    private final String nameIngredient;
    private final int quantityOfIngredients;
    private final String measure;


    public static IngredientDTO from(int id, Ingredient ingredient) {
        return new IngredientDTO(id, ingredient.getNameIngredient(), ingredient.getQuantityOfIngredients(),
                ingredient.getMeasure());

    }
}

