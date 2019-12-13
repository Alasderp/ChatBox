package reddit;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import reddit.response.RedditPost;

import java.io.IOException;

import static org.springframework.http.HttpMethod.GET;

public class RedditService {

    private String redditUrl = "https://www.reddit.com/r/ProgrammerHumor/top.json?limit=1";
    private RestTemplate restTemplate;

    public RedditService(){
        this.restTemplate = new RestTemplate();
    }

    public RedditPost getMostUpvotedPost() throws IOException {

        System.setProperty("http.proxyHost", "107.15.42.4");
        System.setProperty("http.proxyPort", "8080");
        System.setProperty("https.proxyHost", "107.15.42.4");
        System.setProperty("https.proxyPort", "8080");

        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "AlasdairMemeBot");
        headers.add("Content-Type", "text/plain");
        headers.add("Accept", "*/*");

        HttpEntity request = new HttpEntity(headers);

        try {
            return restTemplate.exchange(redditUrl, GET, request, RedditPost.class)
                    .getBody();
        }
        catch(HttpStatusCodeException e){
            System.out.println("\n"+e.getResponseBodyAsString()+"\n");
            throw e;
        }

    }

}
