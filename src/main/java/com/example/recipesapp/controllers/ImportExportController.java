package com.example.recipesapp.controllers;

import com.example.recipesapp.services.IngredientServices;
import com.example.recipesapp.services.RecipeServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/files")
@Tag(name = "Импорт и экспорт", description = " ")
public class ImportExportController {
    private final RecipeServices recipeServices;

    private final IngredientServices ingredientServices;

    public ImportExportController(RecipeServices recipeServices, IngredientServices ingredientServices) {
        this.recipeServices = recipeServices;
        this.ingredientServices = ingredientServices;
    }




    //Скачивание рецептов //Resource - определяет методы для доступа к ресурсу, такие как получение его имени, чтение и запись данных, закрытие ресурса и т.д.
    @GetMapping("/export/recipes")
    @Operation(
            summary = "Получение полного списка ингредиентов"
    )
    public ResponseEntity<Resource> downloadRecipes() {
        Resource recipes = recipeServices.getRecipesFile();
        if (recipes.exists()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"RecipesData.json\"")
                    .body(recipes);
        } else {
            return ResponseEntity.noContent().build();  //в ок, но содержимого нет
        }
    }

    @PostMapping(value = "/import/recipes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Загрузка рецепта"
    )
    public ResponseEntity<Resource> uploadDataFileRecipe(@RequestParam MultipartFile file) {
        this.recipeServices.uploadRecipe(file.getResource());
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/import/ingredient", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Загрузка ингредиента"
    )
    public ResponseEntity<Resource> uploadDataFileIngredient(@RequestParam MultipartFile file) {
        this.ingredientServices.uploadIngredient(file.getResource());
        return ResponseEntity.noContent().build();
    }

}
//        filesService.cleanDataFile();
//        File fileData = filesService.getDataFile();
//
//        try (FileOutputStream fos = new FileOutputStream(fileData)) {
//            IOUtils.copy(file.getInputStream(), fos);
//            return ResponseEntity.ok().build();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//    }