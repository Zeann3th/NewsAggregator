package newsaggregator.post;

/**
 * Lớp này dùng để lưu trữ thông tin của tác giả.
 * <br> Mỗi tác giả sẽ có tên, ngày sinh và bài viết mới nhất.
 * <br> Lớp này sẽ được sử dụng trong lớp Post.
 * @see Post
 * @author Lý Hiển Long
 * @since 1.0
 */
public class Author {

    // Attributes
    private String name;
    private String description;

    // Constructors

    public Author() {}

    public Author(String name) {
        this.name = name;
    }

    public Author(String name, String description) {
        this.name = name;
        this.description = description;
    }
    // Methods

    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }

    public void display() {
        System.out.println("Author name: " + name);
        System.out.println("Author description: " + description);
    }
}
