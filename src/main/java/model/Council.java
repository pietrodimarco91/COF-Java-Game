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
	private Queue<Councillor> councillors = new LinkedList<Councillor>();

	/**
	 * Number of councillors inside each Council.
	 */
	private static final int COUNCILLORS_PER_COUNCIL = 4;

	/**
	 * Default constructor: each Council is initially created taking 4 random
	 * Councillors from the CouncillorsPool.
	 */
	public Council() {
		for (int i = 0; i < COUNCILLORS_PER_COUNCIL; i++) {
			CouncillorsPool.shuffle();
			councillors.add(CouncillorsPool.getCouncillor());
		}
	}

	/**
	 * Adds a councillor in the queue of the specified color, from the Councillors Pool.
	 */
	public void addCouncillor(String color) {
		councillors.add(CouncillorsPool.getCouncillor(color));
	}

	/**
	 * Removes a councillor from the queue and places it inside the Councillors Pool.
	 */
	public void removeCouncillor() {
		CouncillorsPool.addCouncillor(councillors.remove());
	}

	/**
	 * @return Queue of councillor used in region to check council satisfaction
	 */
	public Queue<Councillor> getCouncillors() {// Return the arraylist of councillors of this council
		return this.councillors;
	}
	
	public String toString() {
		Iterator<Councillor> iterator = councillors.iterator();
		String string="";
		while(iterator.hasNext()) {
			string+=iterator.next().toString()+"\n";
		}
		return string;
	}

}