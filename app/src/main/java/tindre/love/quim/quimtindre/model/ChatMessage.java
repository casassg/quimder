package tindre.love.quim.quimtindre.model;

public class ChatMessage {

    private String id;
    private String author;
    private String content;
//    private Long createdAt;

    public ChatMessage() {
        author="";
        content="";
//        createdAt = new Date().getTime();
    }

    public ChatMessage(String author, String content) {
        this.author = author;
        this.content = content;
//        this.createdAt = new Date().getTime();
    }

    public ChatMessage(String author, String content, Long createdAt) {
        this.author = author;
        this.content = content;
//        this.createdAt = createdAt;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

//    public Long getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(long createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public Date createdAt(){
//        return new Date(createdAt);
//    }

    @Override
    public String toString() {
        return this.content;
    }
}

