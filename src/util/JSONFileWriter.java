package util;

import org.json.JSONArray;
import org.json.JSONObject;
import posts.Post;


import java.io.FileWriter;
import java.util.List;

public class JSONFileWriter {
    public void writeFile(List<Post> postList) {
        JSONObject jsonFile = new JSONObject();
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
        jsonFile.put("articles", jArray);
            try {
                FileWriter writer = new FileWriter("json/data.json");
                writer.write(jsonFile.toString());
                writer.close();
                System.out.println("Dữ liêu đã được viết thành công!!!");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
    }
}