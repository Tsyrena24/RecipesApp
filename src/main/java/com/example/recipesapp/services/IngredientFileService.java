package com.example.recipesapp.services;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface IngredientFileService {
    //сохранение файла
    <T> void saveToFile(String fileName, T objectToSave);

    //чтение файла
    <T> T readFromFile(String fileName, TypeReference<T> typeReference);

    //Загрузка файла
    //про имя файла знает recipeService, но скачиваем мы через fileService
    //нужно добавить метод в recipeService getfile и там обратиться к файл сервису

    //Загрузка файла
    //про имя файла знает recipeService, но скачиваем мы через fileService
    //нужно добавить метод в recipeService getfile и там обратиться к файл сервису
    File getDataFile();

    boolean cleanDataFile();


    boolean uploadDataFile(MultipartFile file);
}
