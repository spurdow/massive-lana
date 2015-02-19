package gtg.virus.gtpr.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import gtg.virus.gtpr.entities.RemoteBook;

/**
 * Created by DavidLuvelleJoseph on 2/18/2015.
 */
public class EbookEntity {

    @SerializedName("ebooks")
    private List<RemoteBook> ebook;

    public EbookEntity() {
    }

    public List<RemoteBook> getEbook() {
        return ebook;
    }

    public void setEbook(List<RemoteBook> ebook) {
        this.ebook = ebook;
    }
}
