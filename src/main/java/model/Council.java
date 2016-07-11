package model;


import java.util.ArrayList;
import java.io.Serializable;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import exceptions.CouncillorNotFoundException;

/**
 * Created by Gabriele Bressan on 13/05/16.
 * Abstract class used for create a council
 */
public abstract class Council implements Serializable {

	/**
	 * Variable used for serialization
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Queue of councillors,I used LinkedList Queue type
	 */
	private Queue<Councillor> councillors;

	/**
	 * Number of councillors inside each Council.
	 */
	private static final int COUNCILLORS_PER_COUNCIL = 4;
	
	protected CouncillorsPool pool;

	/**
	 * Default constructor: each Council is initially created taking 4 random
	 * Councillors from the CouncillorsPool.
	 */
	public Council(CouncillorsPool pool) {
		this.pool=pool;
		councillors = new LinkedList<Councillor>();
		for (int i = 0; i < COUNCILLORS_PER_COUNCIL; i++) {
			pool.shuffle();
			councillors.add(pool.getCouncillor());
		}
	}

	/**
	 * Adds a councillor in the queue of the specified color, from the Councillors Pool.
	 * @throws CouncillorNotFoundException if there isn't a councillor of the specified color
	 */
	public void addCouncillor(String color) throws CouncillorNotFoundException {
		councillors.add(pool.getCouncillor(color));
	}

	/**
	 * Removes a councillor from the queue and places it inside the Councillors Pool.
	 */
	public void removeCouncillor() {
		pool.addCouncillor(councillors.remove());
	}

	/**
	 * @return Queue of councillor used in region to check council satisfaction
	 */
	public Queue<Councillor> getCouncillors() {// Return the arraylist of councillors of this council
		return this.councillors;
	}
	
	/**
	 * @param politicCards
	 *            of the player
	 * @return number of councillors satisfied
	 */
	public ArrayList<String> numberOfCouncillorsSatisfied(ArrayList<String> politicCards) {
		Iterator<Councillor> iterationCouncillors = this.councillors.iterator();
		Councillor councillor;
		ArrayList<String> tempArrayList = new ArrayList<String>(politicCards);
		ArrayList<String> cardSatisfied = new ArrayList<String>();
		while (iterationCouncillors.hasNext()) {
			boolean councillorsSatisfied = false;
			councillor = iterationCouncillors.next();

			for (int i = 0; i < tempArrayList.size() && !councillorsSatisfied; i++) {
				if (councillor.getColor().equals(tempArrayList.get(i))) {
					councillorsSatisfied = true;
					cardSatisfied.add(tempArrayList.get(i));
					tempArrayList.remove(i);
				}

			}

		}

		if (cardSatisfied.size() < 4) {
			for (int i = 0; i < tempArrayList.size(); i++) {
				if (tempArrayList.get(i).equals("MULTICOLOR"))
					cardSatisfied.add("MULTICOLOR");
			}
		}

		return cardSatisfied;
	}
	
	public String toString() {
		Iterator<Councillor> iterator = councillors.iterator();
		String string="";
		while(iterator.hasNext()) {
			string+=iterator.next().toString()+" ";
		}
		return string;
	}
	

}