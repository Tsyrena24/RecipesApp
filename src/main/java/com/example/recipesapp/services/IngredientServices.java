package com.example.recipesapp.services;

import com.example.recipesapp.dto.IngredientDTO;
import com.example.recipesapp.exception.IngredientNotFoundException;
import com.example.recipesapp.model.Ingredient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IngredientServices {
    private int idCounter = 0; //счетчик


    public static Map<Integer, Ingredient> ingredients = new HashMap<>();


    public IngredientDTO addIngredient(Ingredient ingredient) {
        int id = idCounter++;
        ingredients.put(id, ingredient);
        return IngredientDTO.from(id, ingredient);
    }

    public IngredientDTO getIngredient(int id) {
        Ingredient ingredient = ingredients.get(id);
        if (ingredient != null) {
            return IngredientDTO.from(id, ingredient);
        }
        return null;
    }


    public List<IngredientDTO> getAllIngredient() {
        List<IngredientDTO> result = new ArrayList<>();
        for (Map.Entry<Integer, Ingredient> entry : ingredients.entrySet()) {
            result.add(IngredientDTO.from(entry.getKey(), entry.getValue()));
        }
        return result;
    }


    public IngredientDTO editIngredient(int id, Ingredient ingredient) {
        Ingredient existingIngred = ingredients.get(id); //нахождение рецепта
        if (existingIngred == null) {
            throw new IngredientNotFoundException(); //если null - исключение
        }
        ingredients.put(id, ingredient);
        return IngredientDTO.from(id, ingredient);
    }


    public IngredientDTO deleteIngredient(int id) {
        Ingredient existingIngred = ingredients.remove(id); //нахождение рецепта и удаление
        if (existingIngred == null) {
            throw new IngredientNotFoundException(); //если null - исключение
        }
        return IngredientDTO.from(id, existingIngred);
    }
}

