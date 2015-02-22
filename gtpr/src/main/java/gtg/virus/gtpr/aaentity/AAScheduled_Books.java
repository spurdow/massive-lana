package gtg.virus.gtpr.aaentity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;

import java.util.Date;
import java.util.List;

/**
 * Created by DavidLuvelleJoseph on 2/14/2015.
 */
/*@Table(name = "aasheduled_books" , id = "_id")*/
public class AAScheduled_Books extends Model {


    @Column()
    public Date dateStarted;

    @Column()
    public Date endDate;

    @Column()
    public int status;

    @Column(name = "book")
    public AABook book;

    public static List<AAScheduled_Books> list() {
        return new Select().from(AAScheduled_Books.class).execute();
    }
}
