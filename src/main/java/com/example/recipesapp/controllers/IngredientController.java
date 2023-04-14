package com.example.recipesapp.controllers;

import com.example.recipesapp.dto.IngredientDTO;
import com.example.recipesapp.model.Ingredient;
import com.example.recipesapp.services.IngredientServices;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredient")
public class IngredientController {
    private final IngredientServices ingredientServices;

    public IngredientController(IngredientServices ingredientServices) {
        this.ingredientServices = ingredientServices;
    }

    //Список всех ингредиентов
    @GetMapping
    public List<IngredientDTO> getAllIngred() {
        return ingredientServices.getAllIngredient();
    }

    //Ингредиент по id
    @GetMapping("/{id}")
    public IngredientDTO getIngred(@PathVariable("id") int id) {
        return ingredientServices.getIngredient(id);
    }

    //Создать ингредиент
    @PostMapping
    public IngredientDTO addIngred(@RequestBody Ingredient ingredient) {
        return ingredientServices.addIngredient(ingredient);
    }

    //Изменить ингредиент
    @PutMapping("/{id}")
    public IngredientDTO editIngted(@PathVariable("id") int id,
                                    @RequestBody Ingredient ingredient) {
        return ingredientServices.editIngredient(id, ingredient);
    }

    //Удалить ингредиент
    @DeleteMapping("/{id}")
    public IngredientDTO deleteIngred(@PathVariable("id") int id) {
        return ingredientServices.deleteIngredient(id);
    }
}