package tindre.love.quim.quimtindre.utils;

import java.util.HashMap;
import java.util.Random;

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
        animals.put("Dinosaur", R.mipmap.user);
    }

    public static String getRandomAnimal() {
        Random r = new Random();
        return animals.keySet().toArray(new String[animals.size()])[r.nextInt(animals.size())];
    }

    public static int getAnimalImageId(String animal) {
        if (!animals.containsKey(animal))
            return R.mipmap.user;
        return animals.get(animal);
    }
}
