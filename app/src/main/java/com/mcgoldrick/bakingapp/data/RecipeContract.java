package com.mcgoldrick.bakingapp.data;

import java.util.HashMap;

/**
 * Built up some of the infrastructure to connect with SQLITE.
 * This app does not require SQLITE, since  it does not edit the
 * data and never even sorts the data.
 */
public class RecipeContract {

    public static class Recipes {
        /**
         * Type: int
         */
        public static String _ID = "id";
        /**
         * Type: TEXT NOT NULL
         */
        public static String NAME = "name";
        /**
         * Type: Array
         */
        public static String INGREDIENTS = "ingredients";
        /**
         * Type: Array
         */
        public static String STEPS = "steps";
        /**
         * Type: int
         */
        public static String SERVINGS = "servings";
        /**
         * Type: TEXT NOT NULL
         */
        public static String IMAGE = "image";

        public static HashMap<String, Integer> indexDict;

        public static String[] nameLookup = new String[]{_ID, NAME,
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

    public static class Ingredients {
        /**
         * Type: int
         */
        public static String _ID = "id";
        /**
         * Type: TEXT NOT NULL
         */
        public static String QUANTITY = "quantity";
        /**
         * Type: TEXT NOT NULL
         */
        public static String MEASURE = "measure";
        /**
         * Type: TEXT NOT NULL
         */
        public static String INGREDIENT = "ingredient";

        public static HashMap<String, Integer> indexDict;

        public static String[] nameLookup = new String[]{_ID, QUANTITY,
                MEASURE, INGREDIENT};

        static {
            indexDict = new HashMap<>();
            indexDict.put(_ID, 0);
            indexDict.put(QUANTITY, 1);
            indexDict.put(MEASURE, 2);
            indexDict.put(INGREDIENT, 3);
        }
    }

    public static class Steps {
        /**
         * Type: int
         */
        public static String _ID = "id";
        /**
         * Type: TEXT NOT NULL
         */
        public static String SHORT_DESC = "shortDescription";
        /**
         * Type: TEXT NOT NULL
         */
        public static String INSTRUCTION = "description";
        /**
         * Type: TEXT NOT NULL
         */
        public static String VIDEO_URL = "videoURL";
        /**
         * Type: TEXT NOT NULL
         */
        public static String THUMBNAIL_URL = "thumbnailURL";

        public static HashMap<String, Integer> indexDict;

        public static String[] nameLookup = new String[]{_ID, SHORT_DESC,
                INSTRUCTION, VIDEO_URL, THUMBNAIL_URL};

        static {
            indexDict = new HashMap<>();
            indexDict.put(_ID, 0);
            indexDict.put(SHORT_DESC, 1);
            indexDict.put(INSTRUCTION, 2);
            indexDict.put(VIDEO_URL, 3);
            indexDict.put(THUMBNAIL_URL, 4);

        }
    }

}
