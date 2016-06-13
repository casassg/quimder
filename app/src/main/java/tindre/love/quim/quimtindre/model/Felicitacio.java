package tindre.love.quim.quimtindre.model;

/**
 * Created by casassg on 12/06/16.
 *
 * @author casassg
 */
public class Felicitacio {
    public String text;
    public String path;


    public Felicitacio(String text){
        this.text = text;
    }

    public Felicitacio() {}

    @Override
    public String toString() {
        return text;
    }
}
