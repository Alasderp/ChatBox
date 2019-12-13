package reddit.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Post {

    private String kind;
    private PostData data;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public PostData getData() {
        return data;
    }

    public void setData(PostData data) {
        this.data = data;
    }
}
