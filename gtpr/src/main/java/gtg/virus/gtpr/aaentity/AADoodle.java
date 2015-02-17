package gtg.virus.gtpr.aaentity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by HP on 2/16/2015.
 */

@Table(name = "aadoodle" , id = "_id")
public class AADoodle extends Model{

    @Column
    public int book_page;

    @Column(name = "book" )
    public AABook book;


}
