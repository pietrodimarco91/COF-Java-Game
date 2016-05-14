package model;



import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 
 */
public class Council {

	/**
	 * Queue of councillors,I used LinkedList Queue type
	 */
	private Queue<Councillor> councillors = new LinkedList();

	/**
	 * Default constructor
	 */
	public Council() {
		int i;
		for (i = 0; i < 4; i++) {
			Councillor councillor = new Councillor();
			this.councillors.add(councillor);
		}

	}

	public void addCouncillor() {
		Councillor councillor = new Councillor();
		this.councillors.add(councillor);
	}

	public void removeCoucillor() { // Remove a councillor
		this.councillors.poll();
	}

	public Queue<Councillor> getCouncillors() {// Return a councillor
		return this.councillors;
	}

}