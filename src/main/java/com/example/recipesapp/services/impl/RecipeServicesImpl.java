package com.example.recipesapp.services.impl;

import com.example.recipesapp.dto.IngredientDTO;
import com.example.recipesapp.dto.RecipeDTO;
import com.example.recipesapp.exception.RecipeNotFoundException;
import com.example.recipesapp.model.Ingredient;
import com.example.recipesapp.model.Recipe;
import com.example.recipesapp.services.RecipeFileService;
import com.example.recipesapp.services.RecipeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

@Service  //Сервис рецептов использует сервис ингредиентов как зависимость и заинжектить
public class RecipeServicesImpl implements RecipeService {

    private static final String STONE_FILE_NAME = "recipes";
    private int idCounter = 0; //счетчик
    private Map<Integer, Recipe> recipes = new HashMap<>();
    private final RecipeFileService recipeFileService;
    private final IngredientServicesImpl ingredientServices;

    private final IngredientFilesServiceImpl filesService;

    public RecipeServicesImpl(IngredientServicesImpl ingredientServices,
                              IngredientFilesServiceImpl filesService,
                              RecipeFileService recipeFileService) {
        this.ingredientServices = ingredientServices;
        this.filesService = filesService;
        this.recipeFileService = recipeFileService;
    }


    @PostConstruct
    private void init() {
        TypeReference<Map<Integer, Recipe>> typeReference = new TypeReference<Map<Integer, Recipe>>() {};
        Map<Integer, Recipe> recipes = this.filesService.readFromFile(STONE_FILE_NAME, typeReference);
        if (recipes != null) {
            this.recipes.putAll(recipes);
        } else {
            this.recipes = new HashMap<>();
        }
    }

    //Создание рецепта //нужно обращаться к ингредиент сервис и создать нужные ингредиенты
    //Добавление дубликатами

    @Override
    public RecipeDTO addRecipe(Recipe recipe) {
        int id = idCounter++;
        recipes.put(id, recipe);
        for (Ingredient ingredient : recipe.getIngredients()) {
            this.ingredientServices.addIngredient(ingredient);
        }
        this.filesService.saveToFile(STONE_FILE_NAME, this.recipes);
        return RecipeDTO.from(id, recipe);
    }

    //Поиск рецепта по ингредиенту
    public List<RecipeDTO> getRecipeByIngredientId(int ingredientId) {
        IngredientDTO ingredient = this.ingredientServices.getIngredient(ingredientId);
        if (ingredient == null) {
            throw new RecipeNotFoundException(); //  исключение
        }
                                                                                                      //Делаем в стрим мапе. Нужно извлечь все рецепты по entry.set() для него стрим
        return this.recipes.entrySet()
                .stream()                                                                             // сделали стрим от entrySet(), в стриме будут записи с айди и рецептами
                .filter(e -> e.getValue().getIngredients().stream()                                   // используем метод filter, чтобы взять только те рецепты у которых отрабатывает предикат (функция, которая принимает один или несколько аргументов и возвращает значение типа boolean)
                                                                                                      // предикат: getValue() (получает рецепт) getIngredients (его ингредиенты), создается стрим от ингредиентов, вызывается терминальный оператор anyMatch (возвращает тру, если хоть один элемент из стрима ингредиентов подходит под условие) условие:  название ингредиента = названию искомого ингредиента  )
                        .anyMatch(i -> i.getNameIngredient().equals(ingredient.getNameIngredient()))) //подходят те элементы в которых рецепте, в ингредиенте, в стриме
                .map(e -> RecipeDTO.from(e.getKey(), e.getValue()))                                   // трансформируем enrpy в ДТО
                .collect(Collectors.toList());                                                        //колектором собираем вместе
    }

    //Поиск рецепта по нескольким ингредиентам
    public List<RecipeDTO> getRecipeByIngredientsIds(List<Integer> ingredientsIds ) {                   // получаем рецепты список по индификатору ингредиентов
        List<String> ingredientsNames = ingredientsIds.stream()
                .map(i -> this.ingredientServices.getIngredient(i))
                .filter(Objects::nonNull)                                                               //из стрима убираем все null элементы
                .map(i -> i.getNameIngredient())
                .toList();                                                                             //собираем спиков ингредиентов

        if (ingredientsIds == null) {
            throw new RecipeNotFoundException(); //  исключение
        }
                                                                                                           //Делаем в стрим мапе. Нужно извлечь все рецепты по entry.set() для него стрим
        return this.recipes.entrySet()
                .stream()
                .filter(e -> {
                    Set<String> recipeIngredientsNames = e.getValue()                                //Set проверка по списку быстрее
                            .getIngredients()
                            .stream()
                            .map(i -> i.getNameIngredient())
                            .collect(Collectors.toSet());
                    return recipeIngredientsNames.containsAll(ingredientsNames);
                })
                .map(e -> RecipeDTO.from(e.getKey(), e.getValue()))                                   // трансформируем enrpy в ДТО
                .collect(Collectors.toList());                                                        //колектором собираем вместе
    }

    @Override
    public RecipeDTO getRecipe(int id) {
        Recipe recipe = recipes.get(id);
        if (recipe != null) {
            return RecipeDTO.from(id, recipe);
        }
        return null;
    }

    @Override
    public List<RecipeDTO> getAllRecipe() {
        List<RecipeDTO> result = new ArrayList<>();
        for (Map.Entry<Integer, Recipe> entry : recipes.entrySet()) {
            result.add(RecipeDTO.from(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    @Override
    public RecipeDTO editRecipe(int id, Recipe recipe) {
        Recipe existingRecipe = recipes.get(id); //нахождение рецепта
        if (existingRecipe == null) {
            throw new RecipeNotFoundException(); //если null - исключение
        }
        recipes.put(id, recipe);
        this.filesService.saveToFile(STONE_FILE_NAME, this.recipes);
        return RecipeDTO.from(id, recipe);
    }

    @Override
    public RecipeDTO deleteRecipe(int id) {
        Recipe existingRecipe = recipes.remove(id);  //нахождение рецепта и remove(id) удаление
        if (existingRecipe == null) {
            throw new RecipeNotFoundException();  //если null - исключение
        }
        this.filesService.saveToFile(STONE_FILE_NAME, this.recipes);
        return RecipeDTO.from(id, existingRecipe);

    }


    @Override
    public Path createTextDataFile() throws IOException {
        Path path = recipeFileService.createTampFile("recipesDataFile");
        for (Recipe recipe : recipes.values()) {
            try (Writer writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
                writer.append(recipe.getNameRecipe())
                        .append("\n \n")
                        .append("Время готовки: ")
                        .append(String.valueOf(recipe.getCookedTime()))
                        .append(" минут.")
                        .append("\n");
                writer.append("\n");
                writer.append("Ингредиенты: \n \n");
                recipe.getIngredients().forEach(ingredient -> {
                    try {
                        writer.append(" - ").
                                append(ingredient.getNameIngredient()).
                                append(" - ").
                                append(String.valueOf(ingredient.getQuantityOfIngredients())).
                                append(" ").
                                append(ingredient.getMeasure()).
                                append("\n \n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                writer.append("\n");
                writer.append("Инструкции по приготовлению: \n \n");
                recipe.getCookingSteps().forEach(step -> {
                    try {
                        writer.append(" > ").
                                append(step).
                                append("\n \n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                writer.append("\n \n");
            }
        }
        return path;
    }
    public void uploadRecipe(Resource resource) {
        filesService.saveResource(STONE_FILE_NAME, resource);
        this.recipes = filesService.readFromFile(STONE_FILE_NAME,
                new TypeReference<>() {
                });
    }
    private void saveToFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(recipes);
            recipeFileService.saveToFile(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    private void readFromFile() {
        try {
            String json = recipeFileService.readFromFile();
            recipes = new ObjectMapper().readValue(json, new TypeReference<HashMap<Integer, Recipe>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

//
//        public Resource getRecipesFile() {
//        return filesService.getDataFile(STONE_FILE_NAME);
//    }
//
//    public void uploadRecipe(Resource resource) {
//        filesService.saveResource(STONE_FILE_NAME, resource);
//        this.recipes = filesService.readFromFile(STONE_FILE_NAME,
//                new TypeReference<>() {
//                });
//    }



}


