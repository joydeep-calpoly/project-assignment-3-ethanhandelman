package article_parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A utility class that parses JSON formatted news data into structured article objects.
 */
public class NewsJsonParser implements NewsVisitor {
    private final Logger logger;
    private boolean parsingComplete;
    private ArticleStorage articleStorage;

    /**
     * Constructs a NewsJsonParser with a string source.
     * @param logger the Logger to log parsing events and errors
     * @param toParse the String containing the JSON data to be parsed
     */
    public NewsJsonParser(Logger logger){
        this.logger = logger;
        parsingComplete = false;
    }



    /**
     * Parses the JSON data into ArticleStorage which holds the articles.
     * It configures the ObjectMapper to ignore unknown or ignored properties to prevent parsing errors.
     * @return true if the parsing completes successfully, false otherwise
     */
    public boolean parseArticles(NewsForParse news) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            articleStorage = objectMapper.readValue(news.getContent(), ArticleStorage.class);
            parsingComplete = true;
        } catch (JsonMappingException ex) {
            logger.log(Level.WARNING, "JsonMappingException occurred while parsing: ", ex);
        } catch (JsonProcessingException ex) {
            logger.log(Level.WARNING, "JsonProcessingException occurred while parsing: ", ex);
        }
        return parsingComplete;
    }

    /**
     * Returns a list of articles parsed from the JSON data.
     * If the parsing has not been completed or was unsuccessful, returns an empty list.
     * @return a list of articles or an empty list if parsing was not completed
     */
    public List<Article> getArticles(){
        if(parsingComplete){
            return new ArrayList<>(articleStorage.getArticles(true, logger));
        }
        return Collections.emptyList();
    }

}
