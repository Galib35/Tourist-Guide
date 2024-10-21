package com.example.ghuraghuri;

import java.util.ArrayList;
import java.util.HashMap;

public class TestModel {
    public String name;
    public int age;
    //public ArrayList<String> favouriteColors = new ArrayList<>();
    public HashMap<String,String> favouriteColors=new HashMap<>();

    public TestModel(String name, int age, HashMap<String, String> favouriteColors) {
        this.name = name;
        this.age = age;
        this.favouriteColors = favouriteColors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public HashMap<String, String> getFavouriteColors() {
        return favouriteColors;
    }

    public void setFavouriteColors(HashMap<String, String> favouriteColors) {
        this.favouriteColors = favouriteColors;
    }
}
