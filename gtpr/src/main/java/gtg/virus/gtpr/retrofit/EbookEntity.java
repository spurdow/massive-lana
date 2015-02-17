package gtg.virus.gtpr.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import gtg.virus.gtpr.aaentity.AARemoteBook;

/**
 * Created by DavidLuvelleJoseph on 2/18/2015.
 */
public class EbookEntity {

    @SerializedName("ebooks")
    private List<AARemoteBook> ebook;

    public EbookEntity() {
    }

    public List<AARemoteBook> getEbook() {
        return ebook;
    }

    public void setEbook(List<AARemoteBook> ebook) {
        this.ebook = ebook;
    }
}
