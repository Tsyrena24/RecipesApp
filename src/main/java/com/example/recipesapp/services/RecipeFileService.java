package com.example.recipesapp.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;

public interface RecipeFileService {
    boolean saveToFile(String json);


    String readFromFile();

    File getDataFile();

    Path createTampFile(String suffix);

    boolean cleanDataFile();

    boolean uploadDataFile(MultipartFile file);

}
