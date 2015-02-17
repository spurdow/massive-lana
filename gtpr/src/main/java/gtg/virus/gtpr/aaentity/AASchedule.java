package gtg.virus.gtpr.aaentity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

import java.sql.Time;
import java.util.Date;

/**
 * Created by HP on 2/16/2015.
 */

/*@Table(name = "aaschedule" , id = "_id")*/
public class AASchedule extends Model {
    @Column()
    public Date sched_begdate;

    @Column()
    public Date sched_enddate;

    @Column()
    public Time sched_begtime;

    @Column()
    public Time sched_endtime;

    @Column()
    public int sched_status;
}
