package tindre.love.quim.quimtindre.utils;

import java.util.HashMap;

import tindre.love.quim.quimtindre.R;

public class AnimalUtils {


    public final static HashMap<String, Integer> animals;

    static {
        animals = new HashMap<>();
        animals.put("Koala", R.mipmap.user);
        animals.put("Dog", R.mipmap.user);
        animals.put("Cat", R.mipmap.user);
        animals.put("Cow", R.mipmap.user);
        animals.put("Duck", R.mipmap.user);
        animals.put("Crocodile", R.mipmap.user);
        animals.put("Dolphin", R.mipmap.user);
        animals.put("Panda", R.mipmap.user);
        animals.put("Turtle", R.mipmap.user);
        animals.put("Fish", R.mipmap.user);
        animals.put("Dinosaus", R.mipmap.user);
    }

    public static String getRandomAnimal() {
        return animals.keySet().toArray(new String[animals.size()])[(int) (Math.random() % (animals.size() - 1))];
    }

    public static int getAnimalImageId(String animal) {
        if (!animals.containsKey(animal))
            return R.mipmap.user;
        return animals.get(animal);
    }
}
