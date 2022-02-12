package com.example.foodtopia.classes;

import java.util.ArrayList;
import java.util.List;

public class AddTitle {

    List<List<String>> titles = new ArrayList<>();


    public AddTitle() {
        List<String> mcdonalds = new ArrayList<>();
        mcdonalds.add("mcdonalds_food");
        mcdonalds.add("mcdonalds_beverage");
        List<String> kfc = new ArrayList<>();
        kfc.add("kfc_food");
        kfc.add("kfc_beverage");
        kfc.add("kfc_dessert");
        titles.add(mcdonalds);
        titles.add(kfc);
    }

    public List<List<String>> getTitles() {
        return titles;
    }
}
