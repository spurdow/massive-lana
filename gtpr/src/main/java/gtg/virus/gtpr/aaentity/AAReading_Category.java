package gtg.virus.gtpr.aaentity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by HP on 2/16/2015.
 */

@Table(name = "aareading_category" , id = "id")
public class AAReading_Category extends Model{

    @Column()
    public String category_name;
}
