package rocketChat.payloads.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AttachmentField {

    @JsonProperty(value="short")
    private boolean shortField;
    private String title;
    private String value;

    public boolean isShortField() {
        return shortField;
    }

    public void setShortField(boolean shortField) {
        this.shortField = shortField;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
