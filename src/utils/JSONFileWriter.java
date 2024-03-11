package utils;

import org.json.JSONArray;
import org.json.JSONObject;
import posts.Post;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class JSONFileWriter {
    public void writeFile(List<Post> postList) {
        try {
            JSONArray jArray = new JSONArray();
            // Create a writer for the output file
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("json/data.json"));
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

                writer.write(currentPost.toString());
            }
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}