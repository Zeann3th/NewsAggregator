package webcrawling;

import posts.Post;
import java.util.ArrayList;

public abstract class Crawler {

    // Attributes

    private String subject;
    private ArrayList<Post> postList;

    // Methods

    public abstract void crawl();
}
