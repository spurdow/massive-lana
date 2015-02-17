package gtg.virus.gtpr.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DavidLuvelleJoseph on 2/18/2015.
 */
public class RemoteQueryEbook {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("entities")
    private EbookEntity entity;

    public RemoteQueryEbook() {
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

    public EbookEntity getEntity() {
        return entity;
    }

    public void setEntity(EbookEntity entity) {
        this.entity = entity;
    }
}
