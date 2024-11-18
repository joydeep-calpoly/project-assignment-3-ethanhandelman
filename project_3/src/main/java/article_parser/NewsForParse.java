package article_parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewsForParse {
    private NewsSource newsSource;
    private NewsFormat newsFormat;
    private String content;

    public static String getStringFromFile(File file, Logger logger){
        try{
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Exception occurred while trying to read file: ", ex);
        }
        return null;
    }

    public NewsForParse(NewsSource newsSource, NewsFormat newsFormat, String content) {
        this.newsSource = newsSource;
        this.newsFormat = newsFormat;
        this.content = content;
    }

    public String getContent(){
        return content;
    }

    public NewsSource getNewsSource() {
        return newsSource;
    }

    public NewsFormat getNewsFormat() {
        return newsFormat;
    }

    public boolean accept(NewsVisitor parser){
        return parser.parseArticles(this);
    }
}
