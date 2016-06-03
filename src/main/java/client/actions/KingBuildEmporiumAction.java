package client.actions;

import java.util.ArrayList;

/**
 * This class represents the action for building an emporium with king's help,
 * after the king's council is satisfied
 * 
 * @author Riccardo
 *
 */
public class KingBuildEmporiumAction extends Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The name of the city where the player wants to build his emporium
	 */
	private String cityName;

	/**
	 * The politic cards colors to satisfy the king's council
	 */
	private ArrayList<String> politicCardColors;

	public KingBuildEmporiumAction(String typeOfAction, int id, String cityName, ArrayList<String> colors) {
		super(typeOfAction, id);
		this.cityName = cityName;
		this.politicCardColors = colors;
	}
	
	public String getCityName() {
		return this.cityName;
	}
	
	public ArrayList<String> getPoliticCardColors() {
		return this.politicCardColors;
	}
}
