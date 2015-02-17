package gtg.virus.gtpr.aaentity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by HP on 2/16/2015.
 */
@Table(name = "aahighlight" , id = "_id")
public class AAHighlight extends Model {

    @Column()
    public String ebook_id;

    @Column()
    public int highlight_width;

    @Column()
    public int highlight_height;

    @Column()
    public int highlight_page;
}
