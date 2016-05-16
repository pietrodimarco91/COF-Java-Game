package model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Gabriele Bressan on 13/05/16.
 */
public abstract class Council {

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

	/**
	 * Add councillor in queue
	 */
	public void addCouncillor() {
		Councillor councillor = new Councillor();
		this.councillors.add(councillor);
	}
	/**
	 * Remove councillor in queue
	 */
	public void removeCoucillor() { // Remove a councillor
		this.councillors.poll();
	}
	/**
	 * @return Queue of councillor used in region to check council satisfaction 
	 */
	public Queue<Councillor> getCouncillors() {// Return a councillor
		return this.councillors;
	}

}