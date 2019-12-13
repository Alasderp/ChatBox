package rocketChat.payloads.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Fields {

    private String eventName;

    private List<Args> args;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public List<Args> getArgs() {
        return args;
    }

    public void setArgs(List<Args> args) {
        this.args = args;
    }
}
