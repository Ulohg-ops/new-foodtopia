package com.example.foodtopia.Model;

import java.util.ArrayList;
import java.util.List;

public class AddTitle {

    List<List<String>> titles = new ArrayList<>();


    public AddTitle() {
        List<String> mcdonalds = new ArrayList<>();
        mcdonalds.add("mcdonalds_breakfast");
        mcdonalds.add("mcdonalds_beverage");
        mcdonalds.add("mcdonalds_snack");
        mcdonalds.add("mcdonalds_mainmeal");

        List<String> kfc = new ArrayList<>();
        kfc.add("kfc_food");
        kfc.add("kfc_beverage");
        kfc.add("kfc_dessert");

        List<String> subway = new ArrayList<>();
        subway.add("subway_breakfast");
        subway.add("subway_sandwitch");


        titles.add(mcdonalds);
        titles.add(kfc);
        titles.add(subway);
    }

    public List<List<String>> getTitles() {
        return titles;
    }
}
