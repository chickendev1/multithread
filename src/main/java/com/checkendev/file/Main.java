package com.checkendev.file;

public class Main {
    public static void main(String[] args) {
        IJsonFile jsonFile = new JsonFile();
        String strJson = jsonFile.parseJsonFile("customer.json");
        System.out.println(strJson);
    }
}
