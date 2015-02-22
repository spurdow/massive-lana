package gtg.virus.gtpr.aaentity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by DavidLuvelleJoseph on 2/15/2015.
 */
@Table(name = "aabook" , id = "_id")
public class AABook extends Model{

    @Column(name = "title")
    public String title;

    @Column(name = "path" , index =  true , unique =  true , onUniqueConflict = Column.ConflictAction.REPLACE)
    public String path;

    @Column(name = "status")
    public int status;

    public static AABook find(String path) {
        Select select = new Select();
        From from =  select.from(AABook.class);
        from = from.where("path = ?" , path);
        AABook book = from.executeSingle();

        return book;//new Select().from(AABook.class).executeSingle();

    }

    public static List<AABook> findAll(){
        return new Select().from(AABook.class).execute();
    }
}
