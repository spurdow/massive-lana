package gtg.virus.gtpr.aaentity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by DavidLuvelleJoseph on 2/15/2015.
 */
@Table(name = "aabook" , id = "_id")
public class AABook extends Model{

    @Column()
    public String title;

    @Column()
    public String path;

    @Column()
    public int status;
}
