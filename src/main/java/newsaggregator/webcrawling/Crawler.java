package newsaggregator.webcrawling;

import newsaggregator.posts.Post;

import java.util.List;

/**
 * Lớp trừu tượng này dùng để lấy dữ liệu từ các trang web báo.
 * <br> Lớp này sẽ được kế thừa bởi các lớp con.
 * @see newsaggregator.webcrawling.dynamic.BlockchainNewsCrawler
 * @see newsaggregator.webcrawling.dynamic.CoinTelegraphCrawler
 * @since 1.0
 * @author Long Ly
 */
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
