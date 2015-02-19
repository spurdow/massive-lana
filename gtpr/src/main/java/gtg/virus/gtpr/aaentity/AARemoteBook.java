package gtg.virus.gtpr.aaentity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by DavidLuvelleJoseph on 2/18/2015.
 */
@Table(name = "aaremoteebook" , id = "_id")
public class AARemoteBook extends Model {
    @Column(name = "remote_id" , index =  true , unique =  true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long remoteId;
    @Column()
    public long remoteUserId;
    @Column()
    public long remoteDivisionId;
    @Column()
    public String title;
    @Column()
    public String author;
    @Column()
    public String format;
    @Column()
    public String fileName;
    @Column()
    public String path;
    @Column()
    public int status;

    public AARemoteBook() {

    }


    public static List<AARemoteBook> list(){
        return new Select().from(AARemoteBook.class).execute();
    }
}
