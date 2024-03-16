package newsaggregator.posts;

import java.util.List;

/**
 * Lớp này dùng để biểu diễn một bài viết.
 * @see newsaggregator.posts.Author
 * @since 1.0
 * @author Lý Hiển Long
 */
public class Post {

    // Attributes

    private String articleLink;
    private String websiteSource;
    private String articleType;
    private String articleTitle;
    private String articleSummary;
    private String articleDetailedContent;
    private String creationDate;
    private List<String> associatedTags;
    private Author author;
    private String category;

    // Methods
    //TODO: Fix constructor

    public Post() {}

    public Post(String articleLink, String websiteSource, String articleType, String articleTitle,
                String articleSummary, String articleDetailedContent, String creationDate,
                List<String> associatedTags, Author author, String category) {
        this.articleLink = articleLink;
        this.websiteSource = websiteSource;
        this.articleType = articleType;
        this.articleTitle = articleTitle;
        this.articleSummary = articleSummary;
        this.articleDetailedContent = articleDetailedContent;
        this.creationDate = creationDate;
        this.associatedTags = associatedTags;
        this.author = author;
        this.category = category;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public void setWebsiteSource(String websiteSource) {
        this.websiteSource = websiteSource;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public void setArticleSummary(String articleSummary) {
        this.articleSummary = articleSummary;
    }

    public void setArticleDetailedContent(String articleDetailedContent) {
        this.articleDetailedContent = articleDetailedContent;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setAssociatedTags(List<String> associatedTags) {
        this.associatedTags = associatedTags;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public String getWebsiteSource() {
        return websiteSource;
    }

    public String getArticleType() {
        return articleType;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getArticleSummary() {
        return articleSummary;
    }

    public String getArticleDetailedContent() {
        return articleDetailedContent;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public List<String> getAssociatedTags() {
        return associatedTags;
    }

    public Author getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public void display() {
        System.out.println("Article link: " + articleLink);
        System.out.println("Website source: " + websiteSource);
        System.out.println("Article type: " + articleType);
        System.out.println("Article Summary: " + articleSummary);
        System.out.println("Article title: " + articleTitle);
        System.out.println("Detailed article content: " + articleDetailedContent);
        System.out.println("Creation date: " + creationDate);
        System.out.println("Associated tags: " + associatedTags);
        System.out.println("Author's name: " + author.getName());
        System.out.println("Category: " + category);
    }
}
