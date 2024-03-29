package newsaggregator.post;

import java.util.List;

/**
 * Lớp này dùng để biểu diễn một bài viết.
 * @see newsaggregator.post.Author
 * @since 1.0
 * @author Lý Hiển Long
 */
public class Post {

    // Attributes

    private String guid;
    private String articleLink;
    private String websiteSource;
    private String articleType;
    private String articleTitle;
    private String articleSummary;
    private String articleDetailedContent;
    private String creationDate;
    private List<String> associatedTags;
    private Author author;
    private String thumbnailImage;
    private String category;

    // Constructors

    public Post() {
    }

    public Post(String guid, String articleLink, String websiteSource, String articleType, String articleTitle,
            String articleSummary, String articleDetailedContent, String creationDate,
            List<String> associatedTags, Author author, String thumbnailImage, String category) {
    this.guid = guid;
    this.articleLink = articleLink;
    this.websiteSource = websiteSource;
    this.articleType = articleType;
    this.articleTitle = articleTitle;
    this.articleSummary = articleSummary;
    this.articleDetailedContent = articleDetailedContent;
    this.creationDate = creationDate;
    this.associatedTags = associatedTags;
    this.author = author;
    this.thumbnailImage = thumbnailImage;
    this.category = category;
    }

    // Methods

    public void setGuid(String guid) {
        this.guid = guid;
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

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGuid() {
        return guid;
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

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public String getCategory() {
        return category;
    }

    public void display() {
        System.out.println();
        System.out.println("========================================================================================================================");
        System.out.println();
        System.out.println("GUID: " + guid);
        System.out.println("Article link: " + articleLink);
        System.out.println("Website source: " + websiteSource);
        System.out.println("Article type: " + articleType);
        System.out.println("Article title: " + articleTitle);
        System.out.println("Article Summary: " + articleSummary);
        System.out.println();
        System.out.println("Detailed article content: " + articleDetailedContent);
        System.out.println();
        System.out.println("Creation date: " + creationDate);
        System.out.println("Associated tags: " + associatedTags);
        author.display();
        System.out.println("Thumbnail image: " + thumbnailImage);
        System.out.println("Category: " + category);
        System.out.println();
        System.out.println("========================================================================================================================");
        System.out.println();
    }
}
