package gtg.virus.gtpr.aaentity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by HP on 2/16/2015.
 */

@Table(name = "aadoodle" , id = "_id")
public class AADoodle extends Model{

    @Column(name = "book_page" , index = true)
    public int book_page;

    @Column()
    public int color;

    @Column()
    public float sx;

    @Column()
    public float sy;

    @Column(name = "book" , index = true )
    public AABook book;


    public static List<AADoodle> list(int book_page , AABook book){
        return new Select().from(AADoodle.class).where("book_page = ? AND book = ?" , book_page , book.getId()).execute();
    }



}
