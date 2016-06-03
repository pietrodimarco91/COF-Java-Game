package client.actions;

/**
 * This class represents the move to Elect a councillor in a specified region
 * 
 * @author Riccardo
 *
 */
public class ElectCouncillorAction extends Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The region of the desired council
	 */
	private String region;

	/**
	 * The color of the councillor that the player wants to elect
	 */
	private String councillorColor;

	public ElectCouncillorAction(String typeOfAction, String region, String color) {
		super(typeOfAction);
		this.region = region;
		this.councillorColor = color;
	}

	public String getRegion() {
		return this.region;
	}

	public String getColor() {
		return this.councillorColor;
	}
}
