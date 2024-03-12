package newsaggregator.jsonfilewriter;

import org.json.JSONArray;
import org.json.JSONObject;
import newsaggregator.posts.Post;

import java.io.FileWriter;
import java.util.List;

public class JSONFileWriter {
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
            FileWriter writer = new FileWriter("src/main/resources/data/data.json");
            writer.write(jArray.toString());
            writer.close();
            System.out.println("Dữ liêu đã được viết thành công!!!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}