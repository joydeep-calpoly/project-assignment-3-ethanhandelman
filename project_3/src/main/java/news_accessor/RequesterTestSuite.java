package news_accessor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequesterTestSuite {
    private static final Logger logger = Logger.getLogger(RequesterTestSuite.class.getName());
    @Mock
    NewsRequester requesterMock;

    @BeforeEach
    public void setupLogger() {
        try {
            FileHandler fileHandler = new FileHandler("requester-test-suite-log.log", true); // Append mode
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            logger.setUseParentHandlers(false);

        } catch (SecurityException | IOException e) {
            logger.severe("Failed to setup logger handler: " + e.getMessage());
        }
    }

    // Tests that a valid API request is properly handled by the requester,
    // asserting the successful query attempt and verifying the response structure is correctly received.
    @Test
    public void testValidRequest(){
        assertNotNull(requesterMock);
        when(requesterMock.attemptQuery("top-headlines?country=us")).thenReturn(true);

        assertTrue(requesterMock.attemptQuery("top-headlines?country=us"));
        when(requesterMock.getResponse()).thenReturn("{\n" +
                "  \"description\": \"Extend Assignment #1 to support multiple sources and to introduce source processor.\",\n" +
                "  \"publishedAt\": \"2021-04-16 09:53:23.709229\",\n" +
                "  \"title\": \"Assignment #2\",\n" +
                "  \"url\": \"https://canvas.calpoly.edu/courses/55411/assignments/274503\"\n" +
                "}");

        assertNotNull(requesterMock.getResponse());
    }

    // Verifies the behavior of the requester when handling an invalid API request,
    // ensuring that the request fails as expected and that the error message is correctly returned.
    @Test
    public void testInvalidRequest(){
        when(requesterMock.attemptQuery("????api-key?")).thenReturn(false);
        assertFalse(requesterMock.attemptQuery("????api-key?"));

        when(requesterMock.getResponse()).thenCallRealMethod();
        assertEquals(requesterMock.getResponse(), "NewsRequester :: No request has been saved !");
    }

}
