package newsaggregator.posts;

public class Author {

    // Attributes
    private String name;
    private String dateOfBirth;
    private String lastPost;

    // Methods
    //TODO: Fix constructor
    public Author() {}
    public Author(String name, String dateOfBirth, String lastPost) {
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
    public void setLastPost(String lastPost) {
        this.lastPost = lastPost;
    }
    public String getName() {
        return name;
    }
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    public String getLastPost() {
        return lastPost;
    }
}
