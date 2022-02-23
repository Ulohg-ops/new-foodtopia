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
        kfc.add("kfc_beverage");
        kfc.add("kfc_breakfast");
        kfc.add("kfc_mainmeal");
        kfc.add("kfc_sideDish");

        List<String> subway = new ArrayList<>();
        subway.add("subway_bread");
        subway.add("subway_breakfast");
        subway.add("subway_cookie");
        subway.add("subway_salad");
        subway.add("subway_sauce");
        subway.add("subway_sideDish");
        subway.add("subway_subway");


        List<String> mosburger = new ArrayList<>();
        mosburger.add("mosburger_breakfast");
        mosburger.add("mosburger_burger");
        mosburger.add("mosburger_dessert");
        mosburger.add("mosburger_hotCoffee");
        mosburger.add("mosburger_hotdogBurger");
        mosburger.add("mosburger_riceBurger");
        mosburger.add("mosburger_beverage");
        mosburger.add("mosburger_iceCoffee");
        mosburger.add("mosburger_newArrival");
        mosburger.add("mosburger_snack");
        mosburger.add("mosburger_soup");
        mosburger.add("mosburger_tea");



        titles.add(mcdonalds);
        titles.add(kfc);
        titles.add(subway);
        titles.add(mosburger);
    }

    public List<List<String>> getTitles() {
        return titles;
    }
}
