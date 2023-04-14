package com.example.recipesapp.controllers;

import com.example.recipesapp.dto.IngredientDTO;
import com.example.recipesapp.dto.RecipeDTO;
import com.example.recipesapp.model.Recipe;
import com.example.recipesapp.services.RecipeServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipe")
@Tag(name = "Рецепты", description = "Добавление, введение, редактирование, удаление рецептов (id или список)")

public class RecipeController {

    private final RecipeServices recipeServices;

    public RecipeController(RecipeServices recipeServices) {
        this.recipeServices = recipeServices;
    }

    //Список всех рецептов
    @GetMapping
    @Operation(
            summary = "Получение списка всех рецептов"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список добавленных рецептов",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = IngredientDTO.class))
                            )
                    }

            )
    })
    public List<RecipeDTO> getAllRecipe() {
        return recipeServices.getAllRecipe();
    }

    //Рецепт по id
    @GetMapping("/{id}")
    @Operation(
            summary = "Получение рецепта",
            description = "Введите в строку id рецепта, для поиска из списка"

    )
    public RecipeDTO getRecipe(@PathVariable("id") int id) {
        return recipeServices.getRecipe(id);
    }

    //Списик рецептов по ингредиенту
    @GetMapping("/byIngredient/{id}")
    @Operation(
            summary = "Поиск рецептов по id ингредиента",
            description = "Введите в строку id ингредиента, для поиска рецепта"

    )
    public List<RecipeDTO> getRecipeByIngredientId (@PathVariable("id") int id) {
        return recipeServices.getRecipeByIngredientId(id);
    }

    //Списик рецептов по нескольким ингредиентам //в путь можно передавать только одну переменную, она не может быть списком
    @GetMapping("/byIngredients") // "/byIngredients?ids=1,2,3,4  и тд"
    @Operation(
            summary = "Поиск рецепта по нескольким ингредиентам",
            description = "Введите в строку id нескольких ингредиентов, для поиска рецепта"

    )
    public List<RecipeDTO> getRecipeByIngredientsIds (@RequestParam ("ids") List<Integer> ids) {
        return recipeServices.getRecipeByIngredientsIds(ids);
    }

    //Постраничный вывод // На уровне контролера
    @GetMapping("/page/{pageNumber}")
    @Operation(
            summary = "Вывод рецептов постранично по 10 штук."
    )
    public List<RecipeDTO> getPage(@PathVariable ("pageNumber") int pageNumber) {
        return recipeServices
                .getAllRecipe()
                .stream()
                .skip(pageNumber* 10L)                                               //методом skip пропускаем столько элементов сколько нужно для страницы
                .limit(10).toList();
    }


    //Добавить рецепт
    @PostMapping
    @Operation(
            summary = "Добавление рецепта",
            description = "Добавление информации нового рецепта в список"

    )
    public RecipeDTO addRecipe(@RequestBody Recipe recipe) {
        return recipeServices.addRecipe(recipe);
    }

    //Редактировать рецепт
    @PutMapping("/{id}")
    @Operation(
            summary = "Редактирование рецепта",
            description = "Введите в строку id редактируемого рецепта, для внесения новой информации о рецепте"

    )
    public RecipeDTO editTRecipe(@PathVariable("id") int id,
                                 @RequestBody Recipe recipe) {
        return recipeServices.editRecipe(id, recipe);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление рецептф",
            description = "Введите в строку id удаляемого рецепта, для исключения рецепта из списка"

    )
    public RecipeDTO deleteRecipe(@PathVariable ("id") int id) {
        return recipeServices.deleteRecipe(id);
    }

}
