package com.example.recipesapp.services.impl;

import com.example.recipesapp.dto.IngredientDTO;
import com.example.recipesapp.exception.IngredientNotFoundException;
import com.example.recipesapp.model.Ingredient;
import com.example.recipesapp.services.IngredientService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IngredientServicesImpl implements IngredientService {
    private static final  String STONE_FILE_NAME = "ingredient";
    private int idCounter = 0; //счетчик

    private final IngredientFilesServiceImpl filesService;

    public Map<Integer, Ingredient> ingredients = new HashMap<>();

    public IngredientServicesImpl(IngredientFilesServiceImpl filesService) {
        this.filesService = filesService;
    }

    @PostConstruct
    private void init() {
        TypeReference<Map<Integer, Ingredient>> typeReference = new TypeReference<Map<Integer, Ingredient>>() {};
        Map<Integer, Ingredient> ingredientMap = this.filesService.readFromFile(STONE_FILE_NAME, typeReference);
        if (ingredientMap != null) {
            ingredients.putAll(ingredientMap);
        } else {
            ingredients = new HashMap<>();
        }
    }

    @Override
    public IngredientDTO addIngredient(Ingredient ingredient) {
        int id = idCounter++;
        ingredients.put(id, ingredient);
        filesService.saveToFile(STONE_FILE_NAME, ingredients);
        return IngredientDTO.from(id, ingredient);
    }

    @Override
    public IngredientDTO getIngredient(int id) {
        Ingredient ingredient = ingredients.get(id);
        if (ingredient != null) {
            return IngredientDTO.from(id, ingredient);
        }
        return null;
    }

    @Override
    public List<IngredientDTO> getAllIngredient() {
        List<IngredientDTO> result = new ArrayList<>();
        for (Map.Entry<Integer, Ingredient> entry : ingredients.entrySet()) {
            result.add(IngredientDTO.from(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    @Override
    public IngredientDTO editIngredient(int id, Ingredient ingredient) {
        Ingredient existingIngred = ingredients.get(id); //нахождение рецепта
        if (existingIngred == null) {
            throw new IngredientNotFoundException(); //если null - исключение
        }
        ingredients.put(id, ingredient);
        filesService.saveToFile(STONE_FILE_NAME, ingredients);
        return IngredientDTO.from(id, ingredient);
    }

    @Override
    public IngredientDTO deleteIngredient(int id) {
        Ingredient existingIngred = ingredients.remove(id); //нахождение рецепта и удаление
        if (existingIngred == null) {
            throw new IngredientNotFoundException(); //если null - исключение
        }
        filesService.saveToFile(STONE_FILE_NAME, ingredients);
        return IngredientDTO.from(id, existingIngred);
    }


    public void uploadIngredient(Resource resource) {
        filesService.saveResource(STONE_FILE_NAME, resource);
        this.ingredients = filesService.readFromFile(STONE_FILE_NAME,
                new TypeReference<>() {
                });
    }
//    private void saveToFile() {
//        try {
//            String json = new ObjectMapper().writeValueAsString(ingredients);
//            filesService.saveToFile(json);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private void readFromFile() {
//        try {
//            TypeReference<Map<Integer, Ingredient>> typeReference = new TypeReference<Map<Integer, Ingredient>>() {};
//
//            String json = filesService.readFromFile(STONE_FILE_NAME, typeReference).toString();
//            ingredients = new ObjectMapper().readValue(json, new TypeReference<HashMap<Integer, Ingredient>>() {
//            });
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
}

