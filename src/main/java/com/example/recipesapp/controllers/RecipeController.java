package com.example.recipesapp.controllers;

import com.example.recipesapp.dto.IngredientDTO;
import com.example.recipesapp.dto.RecipeDTO;
import com.example.recipesapp.model.Recipe;
import com.example.recipesapp.services.impl.RecipeServicesImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/recipe")
@Tag(name = "Рецепты", description = "Добавление, введение, редактирование, удаление рецептов (id или список)")

public class RecipeController {

    private final RecipeServicesImpl recipeServices;

    public RecipeController(RecipeServicesImpl recipeServices) {
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

    //Список рецептов по ингредиенту
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
            summary = "Удаление рецептов",
            description = "Введите в строку id удаляемого рецепта, для исключения рецепта из списка"

    )
    public RecipeDTO deleteRecipe(@PathVariable ("id") int id) {
        return recipeServices.deleteRecipe(id);
    }

    @GetMapping("/export/text")
    @Operation(
            summary = "Загрузка репецт файла в текстовом формате",
            description = "Загрузка файла"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Файл успешно загружен",
                    content = {
                            @Content(
                                    mediaType = "application/text-plain",
                                    array = @ArraySchema(schema =
                                    @Schema(implementation = Recipe.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Файл не имеет содержимого",
                    content = {
                            @Content(
                                    mediaType = "application/text-plain",
                                    array = @ArraySchema(schema =
                                    @Schema(implementation = Recipe.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка параметров запроса",
                    content = {
                            @Content(
                                    mediaType = "application/text-plain",
                                    array = @ArraySchema(schema =
                                    @Schema(implementation = Recipe.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Неверный URL или такого действия нет в веб-приложении",
                    content = {
                            @Content(
                                    mediaType = "application/text-plain",
                                    array = @ArraySchema(schema =
                                    @Schema(implementation = Recipe.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Во время выполнения запроса произошла ошибка на сервере",
                    content = {
                            @Content(
                                    mediaType = "application/text-plain",
                                    array = @ArraySchema(schema =
                                    @Schema(implementation = Recipe.class))
                            )
                    }
            )
    })
    public ResponseEntity<Object> downloadTextDataFile() {
        try {
            Path path = recipeServices.createTextDataFile();
            if (Files.size(path) == 0) {
                return ResponseEntity.noContent().build();
            }
            InputStreamResource resource = new InputStreamResource(new FileInputStream(path.toFile()));
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .contentLength(Files.size(path))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"recipesDataFile.txt\"")
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.toString());
        }
    }
}
