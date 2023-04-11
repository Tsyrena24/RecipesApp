package com.example.recipesapp.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstControllers {

    @GetMapping("/")
    public String applicationStatus() {
        return "Приложение запущено";
    }

    @GetMapping("/info")
    public String data() {
        return "Имя студента: Цырена Александровна; " +
                "Название проекта: Recipes; " +
                "Дата создания проекта: 10.04.2023; " +
                "Описание проекта: приложение, в котором представлены рецепты приготовления различных блюд";
    }

}
