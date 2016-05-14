package model;

import java.util.*;

/**
 * Created by Gabriele Bressan on 13/05/16.
 */
public class KingCouncil {
	/**
	 * Queue of King Councillors,I used LinkedList Queue type
	 */
	private Queue<Councillor> councillors = new LinkedList<Councillor>();

	/**
	 * Default constructor that set the king council with random councillor
	 */
	public KingCouncil() {
		int i;
		for (i = 0; i < 4; i++) {
			Councillor councillor = new Councillor();
			this.councillors.add(councillor);
		}
	}

}