package gtg.virus.gtpr.aaentity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by DavidLuvelleJoseph on 2/15/2015.
 */
@Table(name = "schedulebook" , id = "_id")
public class AAScheduledBook extends Model {

    @Column()
    public String path;

    @Column()
    public Date startDate;

    @Column
    public Date endDate;

    @Column()
    public int status;
}
