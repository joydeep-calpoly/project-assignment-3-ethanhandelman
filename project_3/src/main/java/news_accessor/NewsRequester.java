package news_accessor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is responsible for making HTTP requests to the News API and managing the responses.
 */
public class NewsRequester {
    private static final String API_KEY = System.getenv("NEWS_API_KEY");
    private boolean success;
    private String result;
    private Logger logger;

    /**
     * Constructs a NewsRequester with a specific logger for logging information and errors.
     *
     * @param logger the Logger object used for logging
     */
    public NewsRequester(Logger logger){
        this.logger = logger;
        success = false;
    }

    /**
     * Attempts to query the News API using the specified parameters and records the result.
     *
     * @param params the parameters for the News API request in URL query string format
     * @return true if the request was successful, false otherwise
     */
    public Boolean attemptQuery(String params){
        String url = "https://newsapi.org/v2/" + params + "&apiKey=" + API_KEY;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET() // GET is default and optional
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Save the response to a file
            result = response.body();
            success = true;
        } catch (IOException | InterruptedException e) {
            logger.log(Level.SEVERE, "Exception occurred while making NewsAPI request: ", e);
        }
        return success;
    }

    /**
     * Retrieves the response from the last successful API request.
     *
     * @return the result of the last API request if successful; otherwise, an error message indicating that no request has been saved.
     */
    public String getResponse(){
        if(success){
            return result;
        }
        return "NewsRequester :: No request has been saved !";
    }

}
