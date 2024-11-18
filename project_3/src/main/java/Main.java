import article_parser.*;
import news_accessor.NewsRequester;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * The Main class demonstrates the parsing of news articles from both local files and a News API.
 * It sets up logging configurations and orchestrates the parsing and displaying of news articles.
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    /**
     * Configures the logger to output to a file, which allows for persistent storage of log messages.
     * The log file "main-parser-log.log" will accumulate messages over time without printing them to the console.
     */
    private static void setupLogger() {
        try {
            FileHandler fileHandler = new FileHandler("main-parser-log.log", true); // Append mode
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false); // Prevents logging to console
        } catch (SecurityException | IOException e) {
            logger.severe("Failed to setup logger handler: " + e.getMessage());
        }
    }

    /**
     * Retrieves news content from a remote News API based on the specified search parameters.
     * If the API request is successful, it constructs a NewsForParse object with the response.
     *
     * @param params the parameters for querying the News API.
     * @return a NewsForParse object with the API response, or null if the request fails.
     */
    private static NewsForParse getFromNewsAPI(String params) {
        NewsRequester newsRequester = new NewsRequester(logger);

        if (Boolean.TRUE.equals(newsRequester.attemptQuery(params))) {
            return new NewsForParse(NewsSource.URL, NewsFormat.NEWS_API, newsRequester.getResponse());
        }

        return null;
    }

    /**
     * Reads the entire contents of a file and creates a NewsForParse object with the content.
     *
     * @param file the file from which the news content is to be read.
     * @param format the format of the news content, e.g., JSON or XML.
     * @return a NewsForParse object containing the content from the file.
     */
    private static NewsForParse getFromFile(File file, NewsFormat format){
        return new NewsForParse(NewsSource.FILE, format, NewsForParse.getStringFromFile(file, logger));
    }

    /**
     * Parses articles from provided NewsForParse object and prints the resulting articles.
     * It employs a NewsJsonParser to parse the content and prints each parsed article.
     *
     * @param news NewsForParse object containing the content to parse.
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
     * The main entry point of the application. Initializes logging, reads content from specified files and APIs,
     * parses them, and prints the results.
     *
     * @param args the command-line arguments, currently not used.
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
