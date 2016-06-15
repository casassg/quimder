package tindre.love.quim.quimtindre.utils;

public class AnimalUtils {

    public final static String[] animalList = new String[]{ "Koala", "Dog", "Cat", "Cow", "Duck",
                                                            "Crocodile", "Dolphin", "Panda", "Turtle",
                                                            "Fish", "Dinosaur"};

    public static String getRandomAnimal() {
        return animalList[(int) (Math.random() % (animalList.length - 1))];
    }

    public static int getAnimalImageId(String animal) {
        return 0;
    }
}
