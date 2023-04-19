package com.example.recipesapp.services.impl;

import com.example.recipesapp.services.RecipeFileService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
@Service
public class RecipeFilesServiceImpl implements RecipeFileService {
    @Value("${path.to.data.file}")
    private String dataFailPath;

    @Value("${name.to.data.file.recipes}")
    private String dataRecipe;

    @Override
    public boolean saveToFile(String json) {
        try {
            cleanDataFile();
            Files.writeString(Path.of(dataFailPath, dataRecipe), json);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public String readFromFile() {
        try {
            return Files.readString(Path.of(dataFailPath, dataRecipe));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public File getDataFile() {
        return new File(dataFailPath + "/" + dataRecipe);
    }

    @Override
    public Path createTampFile(String suffix) {
        try {
            return Files.createTempFile(Path.of(dataFailPath), "tempFile", suffix);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean cleanDataFile() {
        try {
            Path path = Path.of(dataFailPath, dataRecipe);
            Files.deleteIfExists(path);
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean uploadDataFile(MultipartFile file) {
        cleanDataFile();
        File dataFile = getDataFile();
        try (FileOutputStream fos = new FileOutputStream(dataFile)) {
            IOUtils.copy(file.getInputStream(), fos);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


}
