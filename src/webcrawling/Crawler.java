package webcrawling;

import posts.Post;
import java.util.List;

public abstract class Crawler {

    // Attributes

    private String subject;
    private List<Post> postList;

    // Methods

    public abstract void crawl();
}
