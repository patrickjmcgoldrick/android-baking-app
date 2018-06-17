package com.mcgoldrick.bakingapp.data;

import java.util.HashMap;

public class RecipeContract {

    public static class Recipes {
        /** Type: int */
        public static String _ID = "id";
        /** Type: TEXT NOT NULL */
        public static String NAME = "name";
        /** Type: Array */
        public static String INGREDIENTS = "ingredients";
        /** Type: Array */
        public static String STEPS = "steps";
        /** Type: int */
        public static String SERVINGS = "servings";
        /** Type: TEXT NOT NULL */
        public static String IMAGE = "image";

        public static HashMap<String, Integer> indexDict;

        public static String[] nameLookup = new String[]{ _ID, NAME,
                                                        INGREDIENTS, STEPS,
                                                         SERVINGS, IMAGE};

        static {
            indexDict = new HashMap<>();
            indexDict.put(_ID, 0);
            indexDict.put(NAME, 1);
            indexDict.put(INGREDIENTS, 2);
            indexDict.put(STEPS, 3);
            indexDict.put(SERVINGS, 4);
            indexDict.put(IMAGE, 5);
        }


    }



}
