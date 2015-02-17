package gtg.virus.gtpr.aaentity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by HP on 2/16/2015.
 */
@Table(name = "aabookmark" , id = "_id")
public class AABookmark extends Model {

    @Column()
    public int bookmark_page;

    @Column()
    public String sentence_sample;

    @Column(name = "book")
    public AABook book;


}
