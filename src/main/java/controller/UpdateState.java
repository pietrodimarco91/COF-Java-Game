package controller;

import java.io.Serializable;
import java.util.List;

import model.Board;
import model.City;
import model.Council;

/**
 * This class is used to update the GUI: it incapsules all the necessary
 * informations after an event that occurs on the server.
 * The kind of the update is stored in the header field.
 * 
 * @author Riccardo
 *
 */
public class UpdateState implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This field stores the type of the update
	 */
	private String header;
	
	private Board board;
	
	private Player player;
	
	private City city;
	
	private List<Player> players;
	
	private Council council;
	
	private String message;
	
	public UpdateState(Board board) {
		this.header=UpdateStateTags.BOARD.toString();
		this.board=board;
	}
	
	public UpdateState(Council council) {
		this.header=UpdateStateTags.COUNCIL.toString();
		this.council=council;
	}
	
	public UpdateState(Player player) {
		this.header=UpdateStateTags.PLAYER_UPDATE.toString();
		this.player=player;
	}
	
	public UpdateState(City city) {
		this.header=UpdateStateTags.CITY.toString();
		this.city=city;
	}
	
	public UpdateState(List<Player> players) {
		this.header=UpdateStateTags.PLAYERS.toString();
		this.players=players;
	}
	
	public UpdateState(String message) {
		this.header=UpdateStateTags.MESSAGE.toString();
		this.message=message;
	}
	
	public String getHeader() {
		return this.header;
	}
	
	public Board getBoard() {
		return this.board;
	}
	
	public City getCity() {
		return this.city;
	}
	
	public Council getCouncil() {
		return this.council;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public List<Player> getPlayers() {
		return this.players;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	
	
	
	

}
