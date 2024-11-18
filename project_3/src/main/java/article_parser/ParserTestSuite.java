package article_parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTestSuite {
    private static final Logger logger = Logger.getLogger(ParserTestSuite.class.getName());

    @BeforeEach
    public void setupLogger() {
        try {
            FileHandler fileHandler = new FileHandler("parser-test-suite-log.log", true); // Append mode
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            logger.setUseParentHandlers(false);

        } catch (SecurityException | IOException e) {
            logger.severe("Failed to setup logger handler: " + e.getMessage());
        }
    }

    // Tests that the parser can successfully parse a well-formed JSON file and
    // correctly constructs the list of article objects, ensuring all are instances of FullArticle.
    @Test
    public void testParseValidJson() {
        NewsForParse news = new NewsForParse(NewsSource.FILE, NewsFormat.NEWS_API, NewsForParse.getStringFromFile(new File("inputs/example.json"), logger));
        NewsJsonParser parser = new NewsJsonParser(logger);
        news.accept(parser);

        List<? extends Article> parsed = parser.getArticles();
        assertNotNull(parsed, "Parsed list should exist");
        assertEquals(10, parsed.size(), "Should be 10 validated articles");
        for (Article a : parsed) {
            assertTrue(a instanceof FullArticle, "All articles should be full");
        }
    }

    // Verifies the parser's ability to handle a JSON file with incorrect formatting
    // by still returning a list of parsed articles, showing error tolerance.
    @Test
    public void testParseInvalidJson() {
        NewsForParse news = new NewsForParse(NewsSource.FILE, NewsFormat.NEWS_API, NewsForParse.getStringFromFile(new File("inputs/bad.json"), logger));
        NewsJsonParser parser = new NewsJsonParser(logger);
        news.accept(parser);

        List<? extends Article> parsed = parser.getArticles();
        assertNotNull(parsed, "Parsed list should exist");
        assertEquals(7, parsed.size(), "Should be 7 articles due to errors");
    }

    // Checks the parser's handling of JSON files missing critical fields,
    // ensuring it can still create article objects and fill other fields correctly.
    @Test
    public void testParseMissingFieldsJson() {
        NewsForParse news = new NewsForParse(NewsSource.FILE, NewsFormat.NEWS_API, NewsForParse.getStringFromFile(new File("inputs/missing_fields.json"), logger));
        NewsJsonParser parser = new NewsJsonParser(logger);
        news.accept(parser);

        List<? extends Article> parsed = parser.getArticles();
        assertNotNull(parsed, "Parsed list should exist");
        assertEquals(1, parsed.size(), "Should be 1 article");

        assertEquals(parsed.get(0).getTitle(), "People line the streets of Boulder to honor slain police Officer Eric Talley - CNN");
    }

    // Ensures that the parser can handle additional unexpected fields in the JSON input
    // and still accurately create the expected article objects.
    @Test
    public void testParseExtraFieldsJson() {
        NewsForParse news = new NewsForParse(NewsSource.FILE, NewsFormat.NEWS_API, NewsForParse.getStringFromFile(new File("inputs/extra_fields.json"), logger));
        NewsJsonParser parser = new NewsJsonParser(logger);
        news.accept(parser);

        List<? extends Article> parsed = parser.getArticles();
        assertNotNull(parsed, "Parsed list should exist");
        assertEquals(2, parsed.size(), "Should be 2 articles");
        for (Article a : parsed) {
            assertTrue(a instanceof FullArticle);
        }

        assertTrue(new FullArticle(
                new Source("cnn", "CNN"),
                "Ralph Ellis, CNN",
                "People line the streets of Boulder to honor slain police Officer Eric Talley - CNN",
                "Boulder, Colorado, continued to mourn fallen Officer Eric Talley on Wednesday.",
                "https://www.cnn.com/2021/03/24/us/officer-talley-procession/index.html",
                "2021-03-24T22:20:12Z",
                "https://cdn.cnn.com/cnnnext/dam/assets/210322232935-officer-eric-talley-headshot-super-tease.jpg",
                "this is content"

        ).equals(parsed.get(1)), "Articles should match");
    }

    // Tests the parser's ability to process a shorter JSON file while still producing valid article objects.
    @Test
    public void testParseShortJson() {
        NewsForParse news = new NewsForParse(NewsSource.FILE, NewsFormat.NEWS_API, NewsForParse.getStringFromFile(new File("inputs/short.json"), logger));
        NewsJsonParser parser = new NewsJsonParser(logger);
        news.accept(parser);

        List<? extends Article> parsed = parser.getArticles();
        assertNotNull(parsed, "Parsed list should exist");
        assertEquals(1, parsed.size(), "Should be 1 article due to null content in 2nd");
        for (Article a : parsed) {
            assertTrue(a instanceof FullArticle);
        }

        assertTrue(new FullArticle(
                new Source("cnn", "CNN"),
                "By <a href=\"/profiles/julia-hollingsworth\">Julia Hollingsworth</a>, CNN",
                "The latest on the coronavirus pandemic and vaccines: Live updates - CNN",
                "The coronavirus pandemic has brought countries to a standstill. Meanwhile, vaccinations have already started in some countries as cases continue to rise. Follow here for the latest.",
                "https://www.cnn.com/world/live-news/coronavirus-pandemic-vaccine-updates-03-24-21/index.html",
                "2021-03-24T22:32:00Z",
                "https://cdn.cnn.com/cnnnext/dam/assets/200213175739-03-coronavirus-0213-super-tease.jpg",
                "A senior European diplomat is urging caution over the use of proposed new rules that would govern exports of Covid-19 vaccines to outside of the EU. The rules were announced by the European Commissio… [+2476 chars]"
        ).equals(parsed.get(0)), "Articles should match");
    }

    // Tests that a simple, correct JSON format is properly parsed into the expected single article object.
    @Test
    public void testParseSimpleJson() {
        NewsForParse news = new NewsForParse(NewsSource.FILE, NewsFormat.SIMPLE, NewsForParse.getStringFromFile(new File("inputs/simple.json"), logger));
        NewsJsonParser parser = new NewsJsonParser(logger);
        news.accept(parser);

        List<? extends Article> parsed = parser.getArticles();
        assertNotNull(parsed, "Parsed list should exist");
        assertEquals(1, parsed.size(), "Should be 1 article");
        assertFalse(parsed.get(0) instanceof FullArticle);

        assertTrue(new Article(
                "Assignment #2",
                "Extend Assignment #1 to support multiple sources and to introduce source processor.",
                "2021-04-16 09:53:23.709229",
                "https://canvas.calpoly.edu/courses/55411/assignments/274503"

        ).equals(parsed.get(0)), "Articles should match");
    }

    // Evaluates the parser's handling of an incorrectly structured JSON passed as a raw string,
    // specifically testing for proper handling of missing JSON wrapping and null handling.
    @Test
    public void testParseBadSimpleJson() {
        NewsForParse news = new NewsForParse(NewsSource.FILE, NewsFormat.SIMPLE, "{\n" +
                "  \"description\": \"Extend Assignment #1 to support multiple sources and to introduce source processor.\",\n" +
                "  \"title\": \"Assignment #2\",\n" +
                "  \"url\": \"https://canvas.calpoly.edu/courses/55411/assignments/274503\"\n" +
                "}");
        NewsJsonParser parser = new NewsJsonParser(logger);
        news.accept(parser);

        List<? extends Article> parsed = parser.getArticles();
        assertNotNull(parsed, "Parsed list should exist");
        assertEquals(0, parsed.size(), "Should be 0 articles due to bad json");
    }

    // Ensures that the list of articles returned by the parser is not modifiable,
    // testing the immutability of the returned collection against unintended external modifications.
    @Test
    public void testArticleModification() {
        NewsForParse news = new NewsForParse(NewsSource.FILE, NewsFormat.NEWS_API, NewsForParse.getStringFromFile(new File("inputs/example.json"), logger));

        NewsJsonParser parser = new NewsJsonParser(logger);
        news.accept(parser);

        List<? extends Article> parsed = parser.getArticles();
        assertNotNull(parsed, "Parsed list should exist");
        assertEquals(10, parsed.size(), "Should be 38 articles");

        parsed.remove(5);
        parsed.set(1, null);

        List<? extends Article> parsed2 = parser.getArticles();
        assertNotNull(parsed2, "Parsed list should exist");
        assertNotNull(parsed2.get(1), "Returned list should not be modifiable");
        assertEquals(10, parsed2.size(), "Should be 38 articles");
    }

    /**
     * Tests the allFieldsFilled method of the Article class to ensure it correctly identifies when one or more fields are null.
     * This test creates an article with a null 'publishedAt' field and checks that allFieldsFilled returns false,
     * indicating that not all fields are complete.
     */
    @Test
    public void testArticleNullFieldDetection(){
        Article a =  new Article(
                "Assignment #2",
                "Extend Assignment #1 to support multiple sources and to introduce source processor.",
                null,
                "https://canvas.calpoly.edu/courses/55411/assignments/274503"

        );

        assertFalse(a.allFieldsFilled());
    }
}
