package article_parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a data structure for storing news content along with its source and format.
 * This class facilitates the parsing of news data from different sources and formats by providing a common interface.
 */
public class NewsForParse {
    private NewsSource newsSource;
    private NewsFormat newsFormat;
    private String content;

    /**
     * Reads the entire content of a file into a String. This method is particularly useful for obtaining text data
     * from files that are intended to be parsed as news content.
     *
     * @param file   The file from which content is to be read.
     * @param logger Logger to log any exceptions or important events occurring during the file read operation.
     * @return A String containing the content of the file, or null if an IOException occurs during the read.
     */
    public static String getStringFromFile(File file, Logger logger){
        try{
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Exception occurred while trying to read file: ", ex);
        }
        return null;
    }

    /**
     * Constructs an instance of NewsForParse with specified source, format, and content.
     * This constructor initializes a new object to store all necessary data for parsing operations.
     *
     * @param newsSource The source of the news (e.g., FILE, URL).
     * @param newsFormat The format of the news content (e.g., JSON, XML).
     * @param content    The raw string content of the news.
     */
    public NewsForParse(NewsSource newsSource, NewsFormat newsFormat, String content) {
        this.newsSource = newsSource;
        this.newsFormat = newsFormat;
        this.content = content;
    }

    /**
     * Returns the content of the news.
     *
     * @return The content string of this news instance.
     */
    public String getContent(){
        return content;
    }

    /**
     * Returns the source of the news.
     *
     * @return The news source enum (e.g., FILE, URL).
     */
    public NewsSource getNewsSource() {
        return newsSource;
    }

    /**
     * Returns the format of the news.
     *
     * @return The news format enum (e.g., JSON, XML).
     */
    public NewsFormat getNewsFormat() {
        return newsFormat;
    }

    /**
     * Accepts a NewsVisitor (parser) and attempts to parse the content based on the visitor's implementation.
     * This method is part of the Visitor design pattern, allowing different types of parsers to process the news data.
     *
     * @param parser The NewsVisitor that will parse the content.
     * @return true if the parser successfully processes the news content, false otherwise.
     */
    public boolean accept(NewsVisitor parser){
        return parser.parseArticles(this);
    }
}
