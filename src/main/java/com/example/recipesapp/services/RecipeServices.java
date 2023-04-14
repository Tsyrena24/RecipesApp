package com.example.recipesapp.services;

import com.example.recipesapp.dto.IngredientDTO;
import com.example.recipesapp.dto.RecipeDTO;
import com.example.recipesapp.exception.RecipeNotFoundException;
import com.example.recipesapp.model.Ingredient;
import com.example.recipesapp.model.Recipe;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service  //Сервис рецептов использует сервис ингредиентов как зависимость и заинжектить
public class RecipeServices {
    private int idCounter = 0; //счетчик
    private final IngredientServices ingredientServices;
    private final Map<Integer, Recipe> recipes = new HashMap<>();

    public RecipeServices(IngredientServices ingredientServices) {
        this.ingredientServices = ingredientServices;
    }

    //Создание рецепта //нужно обращаться к ингредиент сервис и создать нужные ингредиенты
    //Добавление дубликатами
    public RecipeDTO addRecipe(Recipe recipe) {
        int id = idCounter++;
        recipes.put(id, recipe);
        for (Ingredient ingredient : recipe.getIngredients()) {
            this.ingredientServices.addIngredient(ingredient);
        }
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


    public RecipeDTO getRecipe(int id) {
        Recipe recipe = recipes.get(id);
        if (recipe != null) {
            return RecipeDTO.from(id, recipe);
        }
        return null;
    }

    public List<RecipeDTO> getAllRecipe() {
        List<RecipeDTO> result = new ArrayList<>();
        for (Map.Entry<Integer, Recipe> entry : recipes.entrySet()) {
            result.add(RecipeDTO.from(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    public RecipeDTO editRecipe(int id, Recipe recipe) {
        Recipe existingRecipe = recipes.get(id); //нахождение рецепта
        if (existingRecipe == null) {
            throw new RecipeNotFoundException(); //если null - исключение
        }
        recipes.put(id, recipe);
        return RecipeDTO.from(id, recipe);
    }


    public RecipeDTO deleteRecipe(int id) {
        Recipe existingRecipe = recipes.remove(id);  //нахождение рецепта и remove(id) удаление
        if (existingRecipe == null) {
            throw new RecipeNotFoundException();  //если null - исключение
        }
        return RecipeDTO.from(id, existingRecipe);

    }
}


