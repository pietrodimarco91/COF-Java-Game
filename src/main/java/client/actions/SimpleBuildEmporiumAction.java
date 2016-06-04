package client.actions;

/**
 * This class represents the action for building an emporium with a permit tile
 * @author Riccardo
 *
 */
public class SimpleBuildEmporiumAction extends Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The identifier of the chosen permit tile
	 */
	private int id;

	/**
	 * The city where the emporium should be built
	 */
	private String cityName;

	public SimpleBuildEmporiumAction(String type, int permitTileID, String cityName) {
		super(type);
		this.id = permitTileID;
		this.cityName = cityName;
	}
	
	public int getPermitTileID() {
		return this.id;
	}
	
	public String getCityName() {
		return this.cityName;
	}
}
