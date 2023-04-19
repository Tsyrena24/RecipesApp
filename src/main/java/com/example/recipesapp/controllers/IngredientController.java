package com.example.recipesapp.controllers;

import com.example.recipesapp.dto.IngredientDTO;
import com.example.recipesapp.model.Ingredient;
import com.example.recipesapp.services.impl.IngredientServicesImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredient")
@Tag(name = "Ингредиенты", description = "Добавление, введение, редактирование, удаление ингредиентов (id или список)")

public class IngredientController {
    private final IngredientServicesImpl ingredientServices;

    public IngredientController(IngredientServicesImpl ingredientServices) {
        this.ingredientServices = ingredientServices;
    }

    //Список всех ингредиентов
    @GetMapping
    @Operation (
            summary = "Получение полного списка ингредиентов"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список добавленных ингредиентов",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = IngredientDTO.class))
                            )
                    }

            )
    })
    public List<IngredientDTO> getAllIngred() {
        return ingredientServices.getAllIngredient();
    }

    //Ингредиент по id
    @GetMapping("/{id}")
    @Operation(
            summary = "Получение информации об ингредиенте",
            description = "Введите в строку id ингредиента, для получения информации"

    )
    public IngredientDTO getIngred(@PathVariable("id") int id) {
        return ingredientServices.getIngredient(id);
    }

    //Создать ингредиент
    @PostMapping
    @Operation(
            summary = "Добавление ингредиента",
            description = "Добавление информации нового ингредиента в список"

    )
    public IngredientDTO addIngred(@RequestBody Ingredient ingredient) {
        return ingredientServices.addIngredient(ingredient);
    }

    //Изменить ингредиент
    @PutMapping("/{id}")
    @Operation(
            summary = "Редактирование ингредиента",
            description = "Введите в строку id редактируемого ингредиента, для внесения новой информации о ингредиенте"

    )
    public IngredientDTO editIngted(@PathVariable("id") int id,
                                    @RequestBody Ingredient ingredient) {
        return ingredientServices.editIngredient(id, ingredient);
    }

    //Удалить ингредиент
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление ингредиента",
            description = "Введите в строку id удаляемого ингредиента, для исключения ингредиента из списка"

    )
    public IngredientDTO deleteIngred(@PathVariable("id") int id) {
        return ingredientServices.deleteIngredient(id);
    }
}