package com.example.recipesapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Ingredient {
    private String nameIngredient;
    private int quantityOfIngredients;
    private String measure;
}
