import article_parser.*;
import news_accessor.NewsRequester;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Main class for demonstrating the parsing of news articles from both local files and a News API.
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    /**
     * Configures the logger with a file handler and formatter to record logging information.
     * Log messages are recorded in a file named "main-parser-log.log" and not printed to the console.
     */
    private static void setupLogger() {
        try {
            FileHandler fileHandler = new FileHandler("main-parser-log.log", true); // Append mode
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false); // Do not log to console
        } catch (SecurityException | IOException e) {
            logger.severe("Failed to setup logger handler: " + e.getMessage());
        }
    }

    /**
     * Fetches and prints results from the News API based on the specified parameters.
     * If the request is successful, the response is parsed and printed.
     *
     * @param params the parameters for querying the News API
     */
    private static NewsForParse getFromNewsAPI(String params) {
        NewsRequester newsRequester = new NewsRequester(logger);

        if (Boolean.TRUE.equals(newsRequester.attemptQuery(params))) {
            return new NewsForParse(NewsSource.URL, NewsFormat.NEWS_API, newsRequester.getResponse());
        }

        return null;
    }

    /**
     * Reads the entire contents of a file into a string.
     * @param file the file to read
     * @return a string containing the contents of the file
     */
    private static NewsForParse getFromFile(File file, NewsFormat format){
        return new NewsForParse(NewsSource.FILE, format, NewsForParse.getStringFromFile(file, logger));
    }

    /**
     * Parses articles from a file and prints the results.
     * This method reads a JSON file specified by fileName, parses its content, and prints each article.
     *
     * @param fileName the path to the JSON file containing the articles
     */
    private static void printParseResults(NewsForParse news){
        NewsJsonParser parser = new NewsJsonParser(logger);

        if(news.accept(parser)){
            List<? extends Article> parsed = parser.getArticles();
            for(Article a : parsed){
                System.out.println(a);
            }
        }
    }

    /**
     * The entry point of the application.
     * This method sets up the logger, then parses and prints articles from several JSON sources.
     *
     * @param args the command-line arguments (not used)
     */
    public static void main(String[] args){
        setupLogger();

        System.out.println("\nArticles parsed from 'bad.json' (Proj1):");
        printParseResults(getFromFile(new File("inputs/bad.json"), NewsFormat.NEWS_API));

        System.out.println("\nArticles parsed from NewsAPI headlines: ");
        printParseResults(getFromNewsAPI("top-headlines?country=us"));

        System.out.println("\nArticles parsed from 'newsapi.json':");
        printParseResults(getFromFile(new File("inputs/newsapi.json"), NewsFormat.NEWS_API));

        System.out.println("Articles parsed from 'example.json':");
        printParseResults(getFromFile(new File("inputs/example.json"), NewsFormat.NEWS_API));

        System.out.println("\nArticles parsed from 'simple.json':");
        printParseResults(getFromFile(new File("inputs/simple.json"), NewsFormat.NEWS_API));

    }
}
