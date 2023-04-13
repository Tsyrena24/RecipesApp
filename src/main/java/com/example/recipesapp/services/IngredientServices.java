package com.example.recipesapp.services;

import com.example.recipesapp.dto.IngredientDTO;
import com.example.recipesapp.model.Ingredient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
}
