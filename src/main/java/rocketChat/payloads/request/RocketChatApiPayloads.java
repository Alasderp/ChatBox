package rocketChat.payloads.request;

public class RocketChatApiPayloads {

    public static String CONNECT = "{\n" +
            "    \"msg\": \"connect\",\n" +
            "    \"version\": \"1\",\n" +
            "    \"support\": [\"1\"]\n" +
            "}";

    public static String LOGIN = "{\n" +
            "    \"msg\": \"method\",\n" +
            "    \"method\": \"login\",\n" +
            "    \"id\":\"Alasdair\",\n" +
            "    \"params\":[\n" +
            "        {\n" +
            "            \"user\": { \"username\": \"USERNAME\" },\n" +
            "            \"password\": {\n" +
            "                \"digest\": \"PASSWORD\",\n" +
            "                \"algorithm\":\"sha-256\"\n" +
            "            }\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    public static String SUBSCRIBE = "{\n" +
            "    \"msg\": \"sub\",\n" +
            "    \"id\": \"AlasdairChatRoom\",\n" +
            "    \"name\": \"stream-room-messages\",\n" +
            "    \"params\":[\n" +
            "        \"y7PbFA8iyaiGZXXDo\",\n" +
            "        true\n" +
            "    ]\n" +
            "}";

    public static String HEARTBEAT = "{\"msg\":\"pong\"}";



}
