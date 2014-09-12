package gtg.virus.gtpr.db;

import java.util.List;

public interface IHelper<T> {

	/**
	 * add and replace functions
	 * @param item
	 */
	long add(T item);
	
	
	List<T> list();
	
	
}
