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
 * A utility class for parsing JSON formatted news data into structured article objects using the Jackson library.
 * This parser is designed to work with a specific JSON structure expected in the news data and populates an ArticleStorage instance.
 */
public class NewsJsonParser implements NewsVisitor {
    private final Logger logger;
    private boolean parsingComplete;
    private ArticleStorage articleStorage;

    /**
     * Constructs a NewsJsonParser with a specified logger.
     * This constructor initializes the parser and sets up the logging framework used to log parsing events and errors.
     *
     * @param logger the Logger instance to log parsing events and errors.
     */
    public NewsJsonParser(Logger logger){
        this.logger = logger;
        parsingComplete = false;
    }

    /**
     * Parses the JSON data provided by the NewsForParse object into an ArticleStorage instance.
     * This method configures the ObjectMapper to be lenient with ignored and unknown properties to avoid parsing errors.
     * The parsing process captures and logs errors without throwing exceptions, allowing the parsing process to be robust.
     *
     * @param news the NewsForParse object containing the JSON data to be parsed.
     * @return true if the parsing completes successfully, false otherwise.
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
     * Retrieves a list of articles that have been successfully parsed from the JSON data.
     * If the parsing has not been completed or was unsuccessful, this method returns an empty list.
     * This ensures that the method can be safely called without checking parsing status first.
     *
     * @return a list of Article objects if parsing was successful, or an empty list if parsing was not completed or failed.
     */
    public List<Article> getArticles(){
        if(parsingComplete){
            return new ArrayList<>(articleStorage.getArticles(true, logger));
        }
        return Collections.emptyList();
    }

}
