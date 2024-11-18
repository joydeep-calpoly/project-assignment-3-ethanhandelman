package article_parser;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Objects;

/**
 * Represents a news article with a title, description, publication date, and URL.
 * It supports JSON serialization and deserialization through Jackson annotations.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @JsonSubTypes.Type(Article.class),
        @JsonSubTypes.Type(FullArticle.class)
})
public class Article {
    @JsonProperty
    private final String title;
    @JsonProperty
    private final String description;
    @JsonProperty
    private final String publishedAt;
    @JsonProperty
    private final String url;

    /**
     * Constructs an article with the specified title, description, publication time, and URL.
     *
     * @param title        the title of the article
     * @param description  the description or summary of the article
     * @param publishedAt  the publication date and time of the article
     * @param url          the URL where the article can be accessed
     */
    public Article(@JsonProperty("title") String title,
                   @JsonProperty("description") String description,
                   @JsonProperty("publishedAt") String publishedAt,
                   @JsonProperty("url") String url) {
        this.title = title;
        this.description = description;
        this.publishedAt = publishedAt;
        this.url = url;
    }

    /**
     * Provides a string representation of the article.
     *
     * @return a string that includes the article's title, description, publication time, and URL.
     */
    @Override
    public String toString() {
        return "\nTitle: " + title +
                "\n\tDescription: " + description +
                "\n\tTime: " + publishedAt +
                "\n\tURL: " + url;
    }

    /**
     * Compares this article to another object to determine equality.
     *
     * @param o the object to compare with this article
     * @return true if the other object is an Article with the same attributes, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return Objects.equals(title, article.title) &&
                Objects.equals(description, article.description) &&
                Objects.equals(publishedAt, article.publishedAt) &&
                Objects.equals(url, article.url);
    }

    /**
     * Generates a hash code for this article based on its attributes.
     *
     * @return an integer hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(title, description, publishedAt, url);
    }

    /**
     * Returns the title of the article.
     *
     * @return the article's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the description of the article.
     *
     * @return the article's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the publication date of the article.
     *
     * @return the article's publication date as a string
     */
    public String getPublishedAt() {
        return publishedAt;
    }

    /**
     * Returns the URL where the article can be accessed.
     *
     * @return the URL of the article
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns a boolean that is false if any fields are null
     *
     * @return boolean that is true if there are no null fields
     */
    public boolean allFieldsFilled(){
        return title != null && description != null && publishedAt != null && url != null;
    }
}
