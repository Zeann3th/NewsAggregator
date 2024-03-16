package newsaggregator.jsonfilewriter;

import org.json.JSONArray;
import org.json.JSONObject;
import newsaggregator.posts.Post;

import java.io.FileWriter;
import java.util.List;

/**
 * Lớp này dùng để viết dữ liệu từ list các bài viết vào file JSON.
 * @see newsaggregator.posts.Post
 * @since 1.0
 * @author Long Ly
 */
public class JSONFileWriter {
    /**
     * Phương thức này dùng để viết dữ liệu từ list các bài viết vào file JSON.
     * @param postList List các bài viết.
     * @see newsaggregator.posts.Post
     */
    public void writeFile(List<Post> postList) {
        JSONArray jArray = new JSONArray();
        for (Post post : postList) {
            JSONObject currentPost = new JSONObject();
            currentPost.put("article_link", post.getArticleLink());
            currentPost.put("website_source", post.getWebsiteSource());
            currentPost.put("article_type", post.getArticleType());
            currentPost.put("article_title", post.getArticleTitle());

            JSONObject authorJObject = new JSONObject();
            authorJObject.put("name", post.getAuthor().getName());
            authorJObject.put("last_post", post.getAuthor().getLastPost());

            currentPost.put("author", authorJObject);
            currentPost.put("creation_date", post.getCreationDate());
            currentPost.put("article_summary", post.getArticleSummary());
            currentPost.put("article_detailed_content", post.getArticleDetailedContent());
            currentPost.put("associated_tags", post.getAssociatedTags());
            currentPost.put("category", post.getCategory());

            jArray.put(currentPost);
        }
        try {
            FileWriter writer = new FileWriter("src/main/resources/data.json");
            writer.write(jArray.toString());
            writer.close();
            System.out.println("Dữ liêu đã được viết thành công!!!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}