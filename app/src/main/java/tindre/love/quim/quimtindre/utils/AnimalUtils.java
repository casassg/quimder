package tindre.love.quim.quimtindre.utils;

import java.util.HashMap;
import java.util.Random;

import tindre.love.quim.quimtindre.R;

public class AnimalUtils {


    public final static HashMap<String, Integer> animals;

    static {
        animals = new HashMap<>();
        animals.put("Koala", R.mipmap.koala);
        animals.put("Dog", R.mipmap.dog);
        animals.put("Cat", R.mipmap.cat);
        animals.put("Cow", R.mipmap.cow);
        animals.put("Duck", R.mipmap.duck);
        animals.put("Crocodile", R.mipmap.crocodile);
        animals.put("Dolphin", R.mipmap.dolphin);
        animals.put("Panda", R.mipmap.panda);
        animals.put("Turtle", R.mipmap.turtle);
        animals.put("Fish", R.mipmap.fish);
        animals.put("Dinosaur", R.mipmap.dinosaur);
        animals.put("Sheep", R.mipmap.sheep);
        animals.put("Horse", R.mipmap.horse);
        animals.put("Pig", R.mipmap.pig);
        animals.put("Elephant", R.mipmap.elephant);
        animals.put("Giraffe", R.mipmap.giraffe);
        animals.put("Cock", R.mipmap.cock);
        animals.put("Wolf", R.mipmap.wolf);
        animals.put("Elk", R.mipmap.elk);
        animals.put("Mouse", R.mipmap.mouse);
        animals.put("Frog", R.mipmap.frog);
        animals.put("Kangaroo", R.mipmap.kangaroo);
        animals.put("Owl", R.mipmap.owl);
        animals.put("Monkey", R.mipmap.monkey);
        animals.put("Rabbit", R.mipmap.rabbit);
        animals.put("Hippo", R.mipmap.hipo);
        animals.put("Quokka", R.mipmap.quokka);
    }

    public static String getRandomAnimal() {
        Random r = new Random();
        return animals.keySet().toArray(new String[animals.size()])[r.nextInt(animals.size())];
    }

    public static int getAnimalImageId(String animal) {
        if (animal.equals("BOSS"))
            return R.mipmap.boss;
        if (!animals.containsKey(animal))
            return R.mipmap.user;
        return animals.get(animal);
    }

    public static boolean contains(String animal) {
        return animals.keySet().contains(animal);
    }
}
