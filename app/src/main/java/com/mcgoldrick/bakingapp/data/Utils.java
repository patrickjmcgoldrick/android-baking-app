package com.mcgoldrick.bakingapp.data;

import java.util.HashMap;

public class Utils {
    public static String NUTELLA_PIE = "https://www.recipeboy.com/wp-content/uploads/2016/09/No-Bake-Nutella-Pie.jpg";
    public static String BROWNIES = "https://images.food52.com/fLh5y8trZcKvYhDG-K_XeRy-RnY=/753x502/ae4bec90-f798-468e-a04b-b092a83e2a1b--brownies1.jpg";
    public static String YELLOW_CAKE = "https://bigoven-res.cloudinary.com/image/upload/basic-yellow-cake-2.jpg";
    public static String CHEESECAKE = "http://oohlalasweets.com/wp-content/uploads/2012/08/Cherry-Cheesecake-e1393362204957.jpg";

    /**
     * Lookup table for images based on known recipies
     */
    public static HashMap<String, String> imageDict;

     static {
        imageDict = new HashMap<>();
        imageDict.put("nutella pie", NUTELLA_PIE);
        imageDict.put("brownies", BROWNIES);
        imageDict.put("yellow cake", YELLOW_CAKE);
        imageDict.put("cheesecake", CHEESECAKE);
    }
}
