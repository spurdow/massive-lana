package gtg.virus.gtpr.db;

import java.util.List;

public interface IForeignQuery<T> {

    List<T> list(long foreign_key_id);
}
