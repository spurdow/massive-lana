package gtg.virus.gtpr.aaentity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DavidLuvelleJoseph on 2/18/2015.
 */
@Table(name = "aaremoteebook" , id = "_id")
public class AARemoteBook extends Model {
    @Column()
    @SerializedName("id")
    public long remoteId;
    @Column()
    @SerializedName("user_id")
    public long remoteUserId;
    @Column()
    @SerializedName("division_id")
    public long remoteDivisionId;
    @Column()
    public String title;
    @Column()
    public String author;
    @Column()
    public String format;
    @Column()
    @SerializedName("filename")
    public String fileName;
    @Column()
    public String path;
    @Column()
    public int status;

    public AARemoteBook() {
        super();
    }

    public AARemoteBook(long remoteId, long remoteUserId, long remoteDivisionId, String title, String author, String format, String fileName, String path, int status) {
        this.remoteId = remoteId;
        this.remoteUserId = remoteUserId;
        this.remoteDivisionId = remoteDivisionId;
        this.title = title;
        this.author = author;
        this.format = format;
        this.fileName = fileName;
        this.path = path;
        this.status = status;
    }

    public static List<AARemoteBook> list(){
        return new Select().from(AARemoteBook.class).execute();
    }
}
