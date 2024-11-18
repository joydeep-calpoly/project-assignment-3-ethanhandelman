package article_parser;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;
import java.util.logging.Logger;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @JsonSubTypes.Type(FullArticleStorage.class),
        @JsonSubTypes.Type(SimpleArticleStorage.class)
})

public interface ArticleStorage {
    public List<Article> getArticles(boolean onlyCompleteArticles, Logger logger);
}
