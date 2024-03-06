package posts;

public class Author {

    // Attributes
    private String name;
    private String dateOfBirth;
    private Post lastPost;

    // Methods
    //TODO: Fix constructor
    public Author() {}
    public Author(String name, String dateOfBirth, Post lastPost) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.lastPost = lastPost;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public void setLastPost(Post lastPost) {
        this.lastPost = lastPost;
    }
    public String getName() {
        return name;
    }
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    public Post getLastPost() {
        return lastPost;
    }
}
