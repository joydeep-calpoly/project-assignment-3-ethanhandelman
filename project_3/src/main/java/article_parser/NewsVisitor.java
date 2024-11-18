package article_parser;

public interface NewsVisitor {
    boolean parseArticles(NewsForParse news);
}
