package article_parser;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Extends the Article class to include more detailed attributes of an article such as the author,
 * image URL, content, and the source from which the article originated.
 */
public class FullArticle extends Article {
    @JsonProperty
    private final String author;
    @JsonProperty
    private final String urlToImage;
    @JsonProperty
    private final String content;
    @JsonProperty
    private final Source source;

    /**
     * Constructs a FullArticle with additional details such as author, URL to an image, and content.
     *
     * @param source      the source of the article
     * @param author      the author of the article
     * @param title       the title of the article
     * @param description the description of the article
     * @param url         the URL where the article can be accessed
     * @param publishedAt the publication date and time of the article
     * @param urlToImage  the URL to an image related to the article
     * @param content     the main content of the article
     */
    @JsonCreator
    public FullArticle(@JsonProperty("source") Source source,
                       @JsonProperty("author") String author,
                       @JsonProperty("title") String title,
                       @JsonProperty("description") String description,
                       @JsonProperty("url") String url,
                       @JsonProperty("publishedAt") String publishedAt,
                       @JsonProperty("urlToImage") String urlToImage,
                       @JsonProperty("content") String content) {
        super(title, description, publishedAt, url);
        this.source = source;
        this.author = author;
        this.urlToImage = urlToImage;
        this.content = content;
    }

    /**
     * Determines if this FullArticle is equal to another object. The comparison
     * includes attributes from the superclass as well as the additional fields in FullArticle.
     *
     * @param o the object to compare with this FullArticle
     * @return true if the provided object represents an identical FullArticle, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(super.equals(o))) return false;
        if (!(o instanceof FullArticle fullArticle)) return false;
        return Objects.equals(author, fullArticle.author) &&
                Objects.equals(urlToImage, fullArticle.urlToImage) &&
                Objects.equals(content, fullArticle.content) &&
                Objects.equals(source, fullArticle.source);
    }

    /**
     * Generates a hash code for this FullArticle based on its attributes.
     *
     * @return an integer hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), author, urlToImage, content, source);
    }

    /**
     * Returns the author of the article.
     *
     * @return the author's name as a String
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Returns the URL to an image related to the article.
     *
     * @return the image URL as a String
     */
    public String getUrlToImage() {
        return urlToImage;
    }

    /**
     * Returns the main content of the article.
     *
     * @return the article's content as a String
     */
    public String getContent() {
        return content;
    }

    /**
     * Returns the source of the article.
     *
     * @return the source object representing where the article originated
     */
    public Source getSource() {
        return source;
    }

    /**
     * Returns a boolean that is false if any fields are null
     *
     * @return boolean that is true if there are no null fields
     */
    @Override
    public boolean allFieldsFilled() {
        return super.allFieldsFilled() &&
                author != null && urlToImage != null && content != null && source.getId() != null && source.getName() != null;
    }
}
