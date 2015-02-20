package gtg.virus.gtpr.aaentity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by HP on 2/16/2015.
 */
@Table(name = "aabookmark" , id = "_id")
public class AABookmark extends Model {

    @Column(name = "page" , index = true, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public int bookmark_page;

    @Column()
    public String sentence_sample;

    @Column(name = "book")
    public AABook book;


    public static AABookmark findBookMark(int page , AABook book){
        return new Select().from(AABookmark.class).where("page = ? AND book = ? " , page , book.getId()).executeSingle();
    }

    public static AABookmark removeBookMark(int page , AABook book){
        return new Delete().from(AABookmark.class).where("page = ? AND book = ? " , page , book.getId()).executeSingle();
    }

    public static List<AABookmark> loadAll() {
        return new Select().from(AABookmark.class).execute();
    }
}
