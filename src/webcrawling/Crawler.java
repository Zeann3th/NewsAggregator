package webcrawling;

import posts.Post;
import java.util.List;

public abstract class Crawler {

    // Attributes

    private List<Post> postList;

    // Methods

    public abstract void crawl();

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }
}
