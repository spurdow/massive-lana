package gtg.virus.gtpr.aaentity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by HP on 2/16/2015.
 */
@Table(name = "aareading_logs" , id = "_id")
public class AAReading_Logs extends Model {


    @Column()
    public int status;

    @Column(name = "book")
    public AABook book;
}
