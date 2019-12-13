package rocketChat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import rocketChat.payloads.RocketChatMessage;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import static rocketChat.payloads.request.RocketChatApiPayloads.*;
import static util.Util.toObject;

public class RocketChatClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketChatClient.class);
    private String URL = "wss://WEBSOCKETURL";

    private WebSocketSession socketSession;

    private ArrayList<String> messageList = new ArrayList<String>();
    private ReadWriteLock messageListLock;
    private Lock readMessageListLock;
    private Lock writeMessageListLock;

    public RocketChatClient(){
        this.messageListLock = new ReentrantReadWriteLock();
        this.readMessageListLock = messageListLock.readLock();
        this.writeMessageListLock = messageListLock.writeLock();
    }

    //@PostConstruct
    public WebSocketSession connect() throws InterruptedException, ExecutionException, IOException {
        try {
            WebSocketClient webSocketClient = new StandardWebSocketClient();

            WebSocketSession socketSession = webSocketClient.doHandshake(new TextWebSocketHandler() {

                @Override
                public void handleTextMessage(WebSocketSession session, TextMessage message) {
                    LOGGER.info("received message - " + message.getPayload());
                    evaluateMessage(session, message);
                }

                @Override
                public void afterConnectionEstablished(WebSocketSession session) {
                    LOGGER.info("established connection - " + session);
                }
            }, new WebSocketHttpHeaders(), URI.create(URL)).get();

            socketSession.sendMessage(new TextMessage(CONNECT));
            socketSession.sendMessage(new TextMessage(LOGIN));
            socketSession.sendMessage(new TextMessage(SUBSCRIBE));

            this.socketSession = socketSession;

            return socketSession;

        } catch (Exception e) {
            LOGGER.error("Exception while accessing websockets", e);
            throw e;
        }
    }

    private void evaluateMessage(WebSocketSession session, TextMessage message){
        try {
            RocketChatMessage chatMessage = toObject(message.getPayload().getBytes(), RocketChatMessage.class);

            if ("ping".equals(chatMessage.getMsg())) {
                session.sendMessage(new TextMessage(HEARTBEAT));
            }
            else if("stream-room-messages".equals(chatMessage.getCollection()) && "changed".equals(chatMessage.getMsg())){
                for(int x = 0; x < chatMessage.getFields().getArgs().size(); x++) {
                    this.addMessage(chatMessage.getFields().getArgs().get(x).getMsg());
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void addMessage(String message){
        try{
            this.writeMessageListLock.lock();
            this.messageList.add(message);
        }
        finally {
            this.writeMessageListLock.unlock();
        }
    }

    public ArrayList<String> readMessages(){
        try{
            this.writeMessageListLock.lock();
            ArrayList<String> messageCopy = (ArrayList<String>) this.messageList.clone();
            this.messageList.clear();
            return messageCopy;
        }
        finally {
            this.writeMessageListLock.unlock();
        }
    }

    public WebSocketSession getSocketSession() {
        return socketSession;
    }

    public void setSocketSession(WebSocketSession socketSession) {
        this.socketSession = socketSession;
    }
}

//Sends message at intervals
            /*newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
                try {
                    TextMessage message = new TextMessage("Hello !!");
                    webSocketSession.sendMessage(message);
                    LOGGER.info("sent message - " + message.getPayload());
                } catch (Exception e) {
                    LOGGER.error("Exception while sending a message", e);
                }
            }, 1, 10, TimeUnit.SECONDS);*/
