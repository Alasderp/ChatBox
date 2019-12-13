package rocketChat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import reddit.RedditService;
import reddit.response.PostData;
import reddit.response.RedditPost;
import rocketChat.payloads.RocketChatMessage;
import rocketChat.payloads.request.Attachment;
import rocketChat.payloads.request.AttachmentField;
import rocketChat.payloads.request.Params;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.ExecutionException;

import static util.Util.toJson;


@SpringBootApplication
@EnableScheduling
public class RocketChatIntegration {

    private static WebSocketSession socketSession;

    public static void main(String[] args) {
        SpringApplication.run(RocketChatIntegration.class, args);

        RocketChatClient rocketChatClient = new RocketChatClient();
        try {
            socketSession = rocketChatClient.connect();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Run at 10 past 3 each day
    //@Scheduled(cron = "0 0/10 15 * * *")

    //Run at 4 every day
    //@Scheduled(cron = "0 0 16 * * *")

    //Run every 10 seconds
    @Scheduled(cron = "*/10 * * * * *")
    public void shitPostMeme() {

        try {

            RedditService redditService = new RedditService();

            RocketChatMessage rocketMessage = new RocketChatMessage();
            rocketMessage.setMsg("method");
            rocketMessage.setMethod("sendMessage");
            rocketMessage.setId("Alasdair");

            Params param = new Params();

            //PARAM ID MUST BE UNIQUE WHEN SENDING MESSAGES
            SecureRandom random = new SecureRandom();
            byte bytes[] = new byte[20];
            random.nextBytes(bytes);
            Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
            String token = encoder.encodeToString(bytes);

            param.set_id(token);
            param.setRid("y7PbFA8iyaiGZXXDo");
            param.setMsg("It's time for Channels Meme'o'Clock");

            RedditPost post = redditService.getMostUpvotedPost();

            param = addImageUrl(param, post);

            ArrayList<Params> params = new ArrayList<>();
            params.add(param);

            rocketMessage.setParams(params);

            socketSession.sendMessage(new TextMessage(toJson(rocketMessage)));

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private Params addImageUrl(Params param, RedditPost post){

        PostData postData = post.getData().getChildren().get(0).getData();

        Attachment attachment = new Attachment();
        attachment.setAuthor_name("Meme-Bot");
        //Image Title
        attachment.setTitle("Meme of the Day");
        //Description beneath title
        attachment.setText("Greetings! I am Meme-Bot. I will take the most highly upvoted post from /r/programmerHumour " +
                "for that day and post it here daily.");
        attachment.setImage_url(postData.getUrl());

        AttachmentField attachmentField = new AttachmentField();
        //Title Below the image
        attachmentField.setTitle(postData.getTitle());
        //Displayed at bottom of image(description or something)
        attachmentField.setValue("https://www.reddit.com"+postData.getPermalink());

        attachment.setFields(Arrays.asList(attachmentField));

        param.setAttachments(Arrays.asList(attachment));

        return param;
    }

}
