package article_parser;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This class provides a container for storing a collection of articles along with metadata about the collection.
 * It implements the ArticleStorage interface and can hold multiple articles. Designed for JSON parsing
 */
class FullArticleStorage implements ArticleStorage {
    private final List<? extends Article> articles;
    private final Integer totalResults;
    private final String status;

    /**
     * Constructs a FullArticleStorage instance with the specified collection of articles and metadata.
     *
     * @param articles     the list of articles to be stored
     * @param totalResults the total number of results that match a query or total available articles
     * @param status       the status of the response or storage (e.g., "ok", "error")
     */
    @JsonCreator
    FullArticleStorage(@JsonProperty("articles") List<? extends Article> articles,
                       @JsonProperty("totalResults") Integer totalResults,
                       @JsonProperty("status") String status) {
        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
    }

    /**
     * Retrieves a list of articles from storage, optionally filtering to include only those articles that have all their fields completely filled.
     * This method can return either all articles stored or only those that fully meet the criteria of having all necessary fields filled. Additionally,
     * it logs a warning if any articles are removed due to missing fields when the filter is applied.
     *
     * @param onlyCompleteArticles if true, only articles with all fields filled are returned; if false, all articles are returned.
     * @param logger the Logger used to log warnings when articles with incomplete fields are filtered out.
     * @return a list of Article objects, possibly filtered to exclude articles with any fields unfilled.
     */
    public List<Article> getArticles(boolean onlyCompleteArticles, Logger logger) {
        if (!onlyCompleteArticles) return new ArrayList<>(articles);
        ArrayList<Article> filtered = new ArrayList<>(articles.stream()
                .filter(Article::allFieldsFilled).collect(Collectors.toList()));

        int diff = articles.size() - filtered.size();
        if(diff > 0){
            logger.log(Level.WARNING, String.format("Removed %d article(s) due to incomplete fields.", diff));
        }

        return filtered;
    }



    /**
     * Returns the total number of results stored in this storage, which could represent the total
     * number of articles that matched a query.
     *
     * @return the total number of articles as an Integer.
     */
    public Integer getTotalResults() {
        return totalResults;
    }

    /**
     * Provides the status of the storage or the response from which the article data was sourced.
     *
     * @return a string representing the status (e.g., "ok" if the data was successfully retrieved).
     */
    public String getStatus() {
        return status;
    }
}
