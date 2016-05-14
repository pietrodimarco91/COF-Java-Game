package model;

import java.util.*;

/**
 * 
 */
public class KingCouncil {
	/**
	 * Queue of King Councillors,I used LinkedList Queue type
	 */
	private Queue<Councillor> councillors = new LinkedList<Councillor>();

	/**
	 * Default constructor
	 */
	public KingCouncil() {
		int i;
		for (i = 0; i < 4; i++) {
			Councillor councillor = new Councillor();
			this.councillors.add(councillor);
		}
	}

}