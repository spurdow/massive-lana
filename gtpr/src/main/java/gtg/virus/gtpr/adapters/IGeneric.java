package gtg.virus.gtpr.adapters;

public interface IGeneric<T> {
	/**
	 * to be retrieve as a generic object
	 * @param position
	 * @return
	 */
	T getObject(int position);
}
