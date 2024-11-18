package article_parser;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides a simple storage for a single article, implementing the ArticleStorage interface.
 * It is designed to store and retrieve a single article.
 */
class SimpleArticleStorage implements ArticleStorage {
    private final Article article;

    /**
     * Constructs a SimpleArticleStorage instance using provided article details.
     *
     * @param title       the title of the article
     * @param description the description of the article
     * @param publishedAt the publication date of the article
     * @param url         the URL of the article
     */
    @JsonCreator
    SimpleArticleStorage(@JsonProperty("title") String title,
                         @JsonProperty("description") String description,
                         @JsonProperty("publishedAt") String publishedAt,
                         @JsonProperty("url") String url) {
        article = new Article(title, description, publishedAt, url);
    }

    /**
     * Retrieves a list of articles from storage, optionally filtering to include only the article that has all fields completely filled.
     * This method returns either a single article if it meets the specified criteria, or an empty list if the article does not meet the criteria
     * and onlyCompleteArticles is true. If an article is excluded due to missing fields, a warning is logged.
     *
     * @param onlyCompleteArticles if true, the method returns the article only if all its fields are filled; if false, it returns the article regardless of field completeness.
     * @param logger the Logger used to log a warning if the article is omitted because it does not have all fields filled.
     * @return a list containing either the single stored article if it meets the filter criteria, or an empty list if it does not.
     */
    @Override
    public List<Article> getArticles(boolean onlyCompleteArticles, Logger logger) {
        if (!onlyCompleteArticles || article.allFieldsFilled()) {
            ArrayList<Article> single = new ArrayList<Article>();
            single.add(article);
            return single;
        }
        logger.log(Level.WARNING, "Removed 1 article due to incomplete fields.");
        return Collections.emptyList();
    }

}
