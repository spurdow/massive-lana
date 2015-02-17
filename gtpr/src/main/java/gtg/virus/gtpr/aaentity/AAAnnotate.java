package gtg.virus.gtpr.aaentity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by HP on 2/16/2015.
 */
@Table(name = "aaanotate" , id = "_id")
public class AAAnnotate extends Model{

    @Column()
    public String ebook_id;

    @Column()
    public int annotate_width;

    @Column()
    public int annotate_height;

    @Column()
    public int annotate_page;
}
