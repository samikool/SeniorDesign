package com.example.myapplication;

import com.example.myapplication.BackendClasses.Item;
import com.example.myapplication.BackendClasses.Linker;

import java.util.ArrayList;

public class Menu {


    public static ArrayList<Item> BBQFoods;
    //public static ArrayList<Model> BBQFoods;
    public static ArrayList<Model> BBQDescriptions;
    public static ArrayList<Model> SideDishFoods;
    public static ArrayList<Model> SideDishDescriptions;
    public static ArrayList<Model> Drinks;
    public static ArrayList<Model> DrinkDescriptions;
    public static ArrayList<Model> Utensils;
    public static ArrayList<Model> UtensilDescriptions;
    public static ArrayList<Integer> UtensilQuant;

    private static String BBQItem1 = "Sam-Gyup-Ssal";
    private static String BBQItem2 = "Wang-Gal-Bi";
    private static String BBQItem3 = "Bul-Go-Gi";
    private static String BBQItem4 = "Gal-Bi";
    private static String BBQItem5 = "Fire Chicken";
    private static String menuDescription1 = "Sliced side pork cooked on iron pan.";
    private static String menuDescription2 = "Marinated big beef rib slices cooked on iron pan.";
    private static String menuDescription3 = "Marinated thin sliced beef cooked on iron pan.";
    private static String menuDescription4 = "Marinated beef short ribs cooked on iron pan.";
    private static String menuDescription5 = "Marinated spicy chicken cooked on stone pan.";

    private static String[] BBQFoodList = new String[]{BBQItem1, BBQItem2, BBQItem3, BBQItem4, BBQItem5};
    private static String[] BBQFoodDescription = new String[]{menuDescription1, menuDescription2, menuDescription3, menuDescription4, menuDescription5};

    private static String[] SideFoodList = new String[]{"mk", "j", "hj", "jk", "gy"};
    private static String[] SideFoodDescription = new String[]{"Asdf","asdf","asdf","asdf","adsf"};

    private static String[] DrinkList = new String[]{"mk", "j", "hj", "jk", "gy"};
    private static String[] DrinkListDescription = new String[]{"Asdf","asdf","asdf","asdf","adsf"};

    private static String[] UtensilList = new String[]{"Fork","Spoon","Steak Knife","Chopsticks","Ladel","Extra Bowl","Extra Plate","Napkin","Towel"};
    private static String[] UDescriptions = new String[]{"Fork","Spoon","Steak Knife","Chopsticks","Ladel","Extra Bowl","Extra Plate","Napkin","Towel"};


    public static void BBQ_Submenu(){
        BBQFoods = Linker.getBBQItems();
       // BBQFoods = getModel(BBQFoodList);
       // BBQDescriptions = getModelDescriptions(BBQFoodDescription);
    }

    public static void SideDish_Submenu(){
        SideDishFoods = getModel(SideFoodList);
        SideDishDescriptions = getModelDescriptions(SideFoodDescription);
    }

    public static void Drinks_Submenu(){
        Drinks = getModel(DrinkList);
        DrinkDescriptions = getModelDescriptions(DrinkListDescription);
    }

    public static void Utensils_Submenu(){
        Utensils = getModel(UtensilList);
        UtensilDescriptions = getModelDescriptions(UDescriptions);
        UtensilQuant = new ArrayList<>(Utensils.size());
    }

    private static ArrayList<Model> getModel(String[] foodList){
        ArrayList<Model> list = new ArrayList<>();
        for(int i = 0; i < foodList.length; i++){

            Model model = new Model();
            model.setNumber(0);
            model.setFood(foodList[i]);
            list.add(model);
        }
        return list;
    }

    private static ArrayList<Model> getModelDescriptions(String[] foodDescription){
        ArrayList<Model> list = new ArrayList<>();
        for(int i = 0; i < foodDescription.length; i++){

            Model model = new Model();
            model.setNumber(0);
            model.setDescription(foodDescription[i]);
            list.add(model);
        }
        return list;
    }
}
