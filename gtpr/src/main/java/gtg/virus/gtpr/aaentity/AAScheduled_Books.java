package gtg.virus.gtpr.aaentity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

import java.util.Date;

/**
 * Created by DavidLuvelleJoseph on 2/14/2015.
 */
/*@Table(name = "aasheduled_books" , id = "_id")*/
public class AAScheduled_Books extends Model {

    @Column()
    public String scheduleBookName;

    @Column(name = "book_id" , index = true )
    public long book_id;

    @Column()
    public Date dateStarted;

    @Column()
    public Date endDate;

    @Column()
    public int status;
}
