package gtg.virus.gtpr.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DavidLuvelleJoseph on 2/10/2015.
 */


public class RemoteLogin {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("entities")
    private Entity entity;

    public RemoteLogin() {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
