package com.example.recipesapp.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class FilesService {

    private final Path filesPath;
    private final ObjectMapper objectMapper;

    //преобразование текущей мапы (рецептов и ингред) в строку
    public FilesService(ObjectMapper objectMapper, @Value("${path.to.data.file}") Path filesPath) {
        this.objectMapper = objectMapper;
        this.filesPath = filesPath;
    }


    //сохранение файла
    public <T> void saveToFile(String fileName, T objectToSave) {
        try {
            String file = objectMapper.writeValueAsString(objectToSave);
            Files.createDirectories(filesPath);
            Path fileNewPath = filesPath.resolve(fileName + ".json");
            Files.deleteIfExists(fileNewPath);
            Files.createFile(fileNewPath);
            Files.writeString(fileNewPath, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //чтение файла
    public <T> T readFromFile(String fileName, TypeReference <T> typeReference){
        Path fileNewPath = filesPath.resolve(fileName + ".json");
        if (!Files.exists(fileNewPath)) {
            return null;
        }
        try {
            String jsonString = Files.readString(fileNewPath);
            T obj = objectMapper.readValue(jsonString, typeReference);
            return obj;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Загрузка файла
    //про имя файла знает recipeService, но скачиваем мы через fileService
    //нужно добавить метод в recipeService getfile и там обратиться к файл сервису
    public Resource getResource(String fileName) {
        Path fileNewPath = filesPath.resolve(fileName + ".json");
        return new FileSystemResource(fileNewPath);
    }

    //
    public void saveResource(String fileName, Resource resource) {
        Path fileNewPath = filesPath.resolve(fileName + ".json");
        try {
            Files.copy(resource.getInputStream(), fileNewPath, StandardCopyOption.REPLACE_EXISTING); //копия не заменяет файл если он существуе, передаем флажок, который будет заменят файл по умолчанию
        } catch (IOException e) {
        e.printStackTrace();
        }

    }



}
