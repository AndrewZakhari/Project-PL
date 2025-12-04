package com.inventory.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileService {
  private String DIR = "../data";
  
  public List<String> readAllLines(String fileName) {
    try {
     java.nio.file.Path path = Paths.get(DIR + fileName);
     
     if(Files.exists(path)){
       return Files.readAllLines(path);
     }else {
       return new ArrayList<>();
     }
    } catch (Exception e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  public void appendToFile(String fileName, String data){
    try {
      java.nio.file.Path path = Paths.get( DIR + fileName);

      String dataWithNewLine = data + System.lineSeparator();
      
      // StandardOpenOption.CREATE here creates the file if it's missing APPEND appends to the end of the file
      Files.write(path, dataWithNewLine.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
      
    } catch (Exception e) {
     System.err.println("Error writing to file: "+ fileName) ;
     e.printStackTrace();
    }
  }
  public void overwriteFile(String fileName, List<String> data) {
    try {
      java.nio.file.Path path = Paths.get( DIR + fileName);

      Files.write(path, data);

    } catch (IOException e) {
      System.err.println("Error overwriting file: " + fileName );
      e.printStackTrace();
    }
  }
}


