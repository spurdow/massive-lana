package gtg.virus.gtpr.aaentity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Date;
import java.util.List;

/**
 * Created by HP on 2/16/2015.
 */
@Table(name = "aareading_plan" , id = "_id")
public class AAReading_Plan extends Model{


    @Column()
    public int readPlan;

    @Column()
    public Date date;

    @Column(name = "book" , index = true, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public AABook book;


    public List<AABook> list(){
        return new Select().from(AABook.class).execute();
    }
}
