package rocketChat.payloads.request;

import java.util.List;

public class Attachment {

    private String text;
    private String author_name;
    private String title;
    private String image_url;

    private List<AttachmentField> fields;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public List<AttachmentField> getFields() {
        return fields;
    }

    public void setFields(List<AttachmentField> fields) {
        this.fields = fields;
    }
}
