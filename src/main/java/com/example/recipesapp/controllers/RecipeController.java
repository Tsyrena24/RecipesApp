package com.example.recipesapp.controllers;

import com.example.recipesapp.dto.RecipeDTO;
import com.example.recipesapp.model.Recipe;
import com.example.recipesapp.services.RecipeServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeServices recipeServices;

    public RecipeController(RecipeServices recipeServices) {
        this.recipeServices = recipeServices;
    }

    //Список всех рецептов
    @GetMapping
    public List<RecipeDTO> getAllRecipe() {
        return recipeServices.getAllRecipe();
    }

    //Рецепт по id
    @GetMapping("/{id}")
    public RecipeDTO getRecipe(@PathVariable("id") int id) {
        return recipeServices.getRecipe(id);
    }

    //Списик рецептов по ингредиенту
    @GetMapping("/byIngredient/{id}")
    public List<RecipeDTO> getRecipeByIngredientId (@PathVariable("id") int id) {
        return recipeServices.getRecipeByIngredientId(id);
    }

    //Списик рецептов по нескольким ингредиентам //в путь можно передавать только одну переменную, она не может быть списком
    @GetMapping("/byIngredients") // "/byIngredients?ids=1,2,3,4  и тд"
    public List<RecipeDTO> getRecipeByIngredientsIds (@RequestParam ("ids") List<Integer> ids) {
        return recipeServices.getRecipeByIngredientsIds(ids);
    }

    //Постраничный вывод // На уровне контролера
    @GetMapping("/page/{pageNumber}")
    public List<RecipeDTO> getPage(@PathVariable ("pageNumber") int pageNumber) {
        return recipeServices
                .getAllRecipe()
                .stream()
                .skip(pageNumber* 10L)                                               //методом skip пропускаем столько элементов сколько нужно для страницы
                .limit(10).toList();
    }


    //Добавить рецепт
    @PostMapping
    public RecipeDTO addRecipe(@RequestBody Recipe recipe) {
        return recipeServices.addRecipe(recipe);
    }

    //Редактировать рецепт
    @PutMapping("/{id}")
    public RecipeDTO editTRecipe(@PathVariable("id") int id,
                                 @RequestBody Recipe recipe) {
        return recipeServices.editRecipe(id, recipe);
    }

    @DeleteMapping("/{id}")
    public RecipeDTO deleteRecipe(@PathVariable ("id") int id) {
        return recipeServices.deleteRecipe(id);
    }
}
