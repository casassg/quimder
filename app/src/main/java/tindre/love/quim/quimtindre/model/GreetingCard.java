package tindre.love.quim.quimtindre.model;

/**
 * Created by casassg on 12/06/16.
 * @author casassg
 */
public class GreetingCard {

    private String author;
    private String text;

    public GreetingCard() {

    }

    public GreetingCard(String author, String text) {
        this.author = author;
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
