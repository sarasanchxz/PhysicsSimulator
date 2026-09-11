package simulator.model;

public interface Observable<T> { // nueva XXXXXXX
	
	void addObserver(T o);
	void removeObserver(T o);

}
