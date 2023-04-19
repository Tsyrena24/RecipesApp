package com.example.recipesapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Recipe {
    private String nameRecipe;
    private int cookedTime;
    private List<Ingredient> ingredients;
    private List<String> cookingSteps;



}
//POJO (англ. Plain Old Java Object) — «старый добрый Java-объект»,
// простой Java-объект, не унаследованный от какого-то специфического
// объекта и не реализующий никаких служебных интерфейсов сверх тех,
// которые нужны для бизнес-модели.