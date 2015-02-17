package gtg.virus.gtpr.aaentity;

import com.activeandroid.annotation.Column;
/**
 * Created by HP on 2/16/2015.
 */
/*@Table(name = "aatags" , id = "_id")*/
public class AATags {

    @Column()
    public String ebook_id;

    @Column()
    public String tag_keyword;

    @Column()
    public int tag_page;
}
