package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * This class represents the pool of the Councillors, where each councillor stay
 * when it's not inside a Council.
 * 
 * @author Riccardo
 *
 */
public class CouncillorsPool {
	/**
	 * This attribute stores an ArrayList of the Councillors inside the pool.
	 * Notice that this attribute is static, as we need to keep always the same
	 * pool of Councillors. This allows to instantiate a CouncillorsPool when
	 * needed, but the pool will be always the same.
	 */
	private static ArrayList<Councillor> pool;

	/**
	 * This number is fixed and represents the number of councillors for each
	 * match, 24 councillors divided in 6 colors (4 councillors / each color).
	 * Please notice that a Councillor cannot be MULTICOLOR.
	 */
	private static final int NUMBER_OF_COUNCILLORS = 24;

	/**
	 * The constructor initializes the pool of the Councillors by making exactly
	 * 4 councillors for each of the 6 colors.
	 */
	public CouncillorsPool() {
		pool=new ArrayList<Councillor>();
		int councillorsPerColor = NUMBER_OF_COUNCILLORS / (CouncillorColors.values().length - 1);
		String color;
		Councillor councillor;
		ArrayList<String> councillorColors = CouncillorColors.getCouncillorColors();
		Iterator<String> iterator = councillorColors.iterator();
		while (iterator.hasNext()) {
			color = iterator.next();
			for (int i = 0; i < councillorsPerColor; i++) {
				councillor = new Councillor(color);
				pool.add(councillor);
			}
		}
	}

	/**
	 * This method returns a Councillor of the specified color from the pool. If
	 * there isn't any, it returns null.
	 * 
	 * @param color
	 *            the color of the desired councillor
	 * @return A councillor of the specified color, if there is one, null
	 *         otherwise.
	 */
	public static Councillor getCouncillor(String color) {
		Iterator<Councillor> iterator = pool.iterator();
		Councillor councillor;
		while (iterator.hasNext()) {
			councillor = iterator.next();
			if (councillor.getColor().equals(color)) {
				pool.remove(councillor);
				return councillor;
			}
		}
		return null;
	}
	
	/**
	 * This method allows to check if there is a councillor of the specified color in the councillor pool.
	 * @param color the color of the councillor to look for
	 * @return true if there is a councillor of the specified color, false otherwise
	 */
	public static boolean checkPresenceOfCouncillor(String color) {
		Iterator<Councillor> iterator = pool.iterator();
		while(iterator.hasNext()) {
			if(iterator.next().getColor().equals(color))
				return true;
		}
		return false;
		
	}

	/**
	 * This method returns the FIRST Councillor inside the pool. If the
	 * extraction should be made randomly, the shuffle() method should be
	 * invoked first.
	 * 
	 * @return The FIRST councillor of the pool.
	 */
	public static Councillor getCouncillor() {
		Councillor councillor = pool.remove(0);
		return councillor;
	}

	/**
	 * This method adds the specified councillor to the pool of councillors
	 * 
	 * @param councillor
	 *            the councillor to put inside the pool
	 */
	public static void addCouncillor(Councillor councillor) {
		pool.add(councillor);
	}

	/**
	 * This method shuffles the pool of Councillors. This is useful at the
	 * beginning of the match to initialize the Councils, to randomize the
	 * extraction of the councillors from the pool.
	 */
	public static void shuffle() {
		Collections.shuffle(pool);
	}
	
	public static String poolStatus() {
		Iterator<Councillor> iterator = pool.iterator();
		String string="";
		Councillor councillor;
		while(iterator.hasNext()) {
			councillor=iterator.next();
			string+=councillor.toString()+"\n";
		}
		return string;
	}
}
