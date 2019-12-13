package reddit.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {

    private String modhash;
    private String dist;
    private List<Post> children;

    public String getModhash() {
        return modhash;
    }

    public void setModhash(String modhash) {
        this.modhash = modhash;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public List<Post> getChildren() {
        return children;
    }

    public void setChildren(List<Post> children) {
        this.children = children;
    }
}
