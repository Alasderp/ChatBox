package rocketChat.payloads.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Args {

    private String _id;
    private String rid;
    private String msg;

    private ts ts;
    private u u;

    private _updatedAt _updatedAt;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public rocketChat.payloads.response.ts getTs() {
        return ts;
    }

    public void setTs(rocketChat.payloads.response.ts ts) {
        this.ts = ts;
    }

    public rocketChat.payloads.response.u getU() {
        return u;
    }

    public void setU(rocketChat.payloads.response.u u) {
        this.u = u;
    }

    public rocketChat.payloads.response._updatedAt get_updatedAt() {
        return _updatedAt;
    }

    public void set_updatedAt(rocketChat.payloads.response._updatedAt _updatedAt) {
        this._updatedAt = _updatedAt;
    }
}
