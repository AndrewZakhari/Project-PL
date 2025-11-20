package com.inventory.pl_project.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
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

    private static final String DATA_DIR = "src/main/resources/data/";

    public static <T> List<T> readFromFile(String filename, Type typeToken) {
        File file = new File(DATA_DIR + filename);

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                writeToFile(filename, new ArrayList<>());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            List<T> data = gson.fromJson(reader, typeToken);
            return data != null ? data : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static <T> void writeToFile(String filename, List<T> data) {
        File file = new File(DATA_DIR + filename);

        try {
            file.getParentFile().mkdirs();
            try (Writer writer = new FileWriter(file)) {
                gson.toJson(data, writer);
            }
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