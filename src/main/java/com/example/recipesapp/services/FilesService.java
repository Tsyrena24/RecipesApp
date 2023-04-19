package com.example.recipesapp.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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

//
//    //удаление файла
//    public boolean cleanDataFile() {
//
//    }
}
