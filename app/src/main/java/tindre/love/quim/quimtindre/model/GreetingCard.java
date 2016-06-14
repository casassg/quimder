package tindre.love.quim.quimtindre.model;

/**
 * Created by casassg on 12/06/16.
 * @author casassg
 */
public class GreetingCard {

    private String author;
    private String text;
    private Integer age;
    private String description;
    private String photoPath;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    @Override
    public String toString() {
        return text;
    }
}
