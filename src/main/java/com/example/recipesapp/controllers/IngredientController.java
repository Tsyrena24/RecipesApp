package com.example.recipesapp.controllers;

import com.example.recipesapp.dto.IngredientDTO;
import com.example.recipesapp.dto.RecipeDTO;
import com.example.recipesapp.model.Ingredient;
import com.example.recipesapp.model.Recipe;
import com.example.recipesapp.services.IngredientServices;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ingredient")
public class IngredientController {
    private final IngredientServices ingredientServices;

    public IngredientController(IngredientServices ingredientServices) {
        this.ingredientServices = ingredientServices;
    }
    @GetMapping("/id")
    public IngredientDTO getIngred(@PathVariable("id") int id) {
        return ingredientServices.getIngredient(id);
    }

    @PostMapping
    public IngredientDTO addIngred(@RequestBody Ingredient ingredient) {
        return ingredientServices.addIngredient(ingredient);
    }
}
