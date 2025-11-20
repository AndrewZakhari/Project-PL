package com.inventory.pl_project.services;

import com.google.gson.reflect.TypeToken;
import com.inventory.pl_project.models.Category;
import com.inventory.pl_project.utils.FileManager;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryService {
    private static final String CATEGORIES_FILE = "categories.json";
    private static final Type CATEGORY_LIST_TYPE = new TypeToken<List<Category>>(){}.getType();

    public void addCategory(Category category) {
        FileManager.appendToFile(CATEGORIES_FILE, category, CATEGORY_LIST_TYPE);
    }

    public void updateCategory(Category updatedCategory) {
        List<Category> categories = getAllCategories();
        categories = categories.stream()
                .map(c -> c.getId().equals(updatedCategory.getId()) ? updatedCategory : c)
                .collect(Collectors.toList());
        FileManager.updateInFile(CATEGORIES_FILE, categories);
    }

    public void deleteCategory(String categoryId) {
        FileManager.deleteFromFile(CATEGORIES_FILE, categoryId, CATEGORY_LIST_TYPE);
    }

    public List<Category> getAllCategories() {
        return FileManager.readFromFile(CATEGORIES_FILE, CATEGORY_LIST_TYPE);
    }

    public Category getCategoryById(String id) {
        return getAllCategories().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
