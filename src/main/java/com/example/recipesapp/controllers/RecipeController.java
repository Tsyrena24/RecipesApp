package com.example.recipesapp.controllers;

import com.example.recipesapp.dto.RecipeDTO;
import com.example.recipesapp.model.Recipe;
import com.example.recipesapp.services.RecipeServices;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeServices recipeServices;

    public RecipeController(RecipeServices recipeServices) {
        this.recipeServices = recipeServices;
    }

    @GetMapping("/{id}")
    public RecipeDTO getRecipe(@PathVariable("id") int id) {
        return recipeServices.getRecipe(id);
    }

    @PostMapping
    public RecipeDTO addRecipe(@RequestBody Recipe recipe) {
        return recipeServices.addRecipe(recipe);
    }
}у

