package client.actions;

import java.util.ArrayList;

/**
 * This class represents the action for buying a permit tile after the specified council is satisfied
 * @author Riccardo
 *
 */
public class BuyPermitTileAction extends Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The name of the region of the council to satisfy
	 */
	private String region;

	/**
	 * the color of the politic cards to use to satisfy the council
	 */
	private ArrayList<String> politicCardColors;

	/**
	 * the slot of the permit tile deck to choose from
	 */
	private int slot;

	public BuyPermitTileAction(String typeOfAction, String region, ArrayList<String> politicCardColors,
			int slot) {
		super(typeOfAction);
		this.region = region;
		this.politicCardColors = politicCardColors;
		this.slot = slot;
	}

	public String getRegion() {
		return this.region;
	}

	public ArrayList<String> getPoliticCardColors() {
		return this.politicCardColors;
	}

	public int getSlot() {
		return this.slot;
	}
}
