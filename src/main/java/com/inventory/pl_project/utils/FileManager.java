package com.inventory.pl_project.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    private static final String DATA_DIR_NAME = "data";
    private static Path DATA_PATH;

    static {
        try {
            // Get the path of the directory where the JAR is located
            URL location = FileManager.class.getProtectionDomain().getCodeSource().getLocation();
            Path jarPath = Paths.get(location.toURI()).getParent();
            DATA_PATH = jarPath.resolve(DATA_DIR_NAME);

            if (!Files.exists(DATA_PATH)) {
                Files.createDirectories(DATA_PATH);
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            // Fallback to a local directory if running in an IDE or other environment
            DATA_PATH = Paths.get(DATA_DIR_NAME);
            try {
                if (!Files.exists(DATA_PATH)) {
                    Files.createDirectories(DATA_PATH);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static <T> List<T> readFromFile(String filename, Type typeToken) {
        Path filePath = DATA_PATH.resolve(filename);
        System.out.println("Attempting to read from: " + filePath.toAbsolutePath());

        // If the file doesn't exist in the external data directory, try reading from resources
        if (!Files.exists(filePath)) {
            try (InputStream inputStream = FileManager.class.getResourceAsStream("/data/" + filename);
                 Reader reader = new InputStreamReader(inputStream)) {
                List<T> data = gson.fromJson(reader, typeToken);
                if (data != null) {
                    // Write the data to the external directory for future use
                    writeToFile(filename, data);
                    return data;
                }
            } catch (Exception e) {
                // File not found in resources, proceed to create a new one
            }
        }

        // Read from the external data directory
        try (Reader reader = new FileReader(filePath.toFile())) {
            List<T> data = gson.fromJson(reader, typeToken);
            return data != null ? data : new ArrayList<>();
        } catch (IOException e) {
            // If reading fails, return an empty list
            return new ArrayList<>();
        }
    }

    public static <T> void writeToFile(String filename, List<T> data) {
        Path filePath = DATA_PATH.resolve(filename);
        try (Writer writer = new FileWriter(filePath.toFile())) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> void appendToFile(String filename, T item, Type typeToken) {
        List<T> data = readFromFile(filename, typeToken);
        data.add(item);
        writeToFile(filename, data);
    }

    public static <T> void updateInFile(String filename, List<T> updatedData) {
        writeToFile(filename, updatedData);
    }

    public static <T> void deleteFromFile(String filename, String id, Type typeToken) {
        List<T> data = readFromFile(filename, typeToken);
        data.removeIf(item -> {
            try {
                java.lang.reflect.Method getId = item.getClass().getMethod("getId");
                return id.equals(getId.invoke(item));
            } catch (Exception e) {
                return false;
            }
        });
        writeToFile(filename, data);
    }
}