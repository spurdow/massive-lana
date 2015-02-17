package gtg.virus.gtpr.aaentity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

/**
 * Created by HP on 2/16/2015.
 */
/*@Table(name = "aathemes" , id = "_id")*/
public class AAThemes extends Model{

    @Column()
    public String theme_style;

    @Column()
    public String theme_color;
}
