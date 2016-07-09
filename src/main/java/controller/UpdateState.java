package controller;

import java.io.Serializable;
import java.util.List;

import model.Board;
import model.ItemOnSale;
import model.Market;
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

	private List<Player> players;

	private String message;

	private List<ItemOnSale> itemsOnSale;
	
	public UpdateState(Board board) {
		this.header=UpdateStateTags.BOARD.toString();
		this.board=board;
	}
	
	public UpdateState(Player player) {
		this.header=UpdateStateTags.PLAYER_UPDATE.toString();
		this.player=player;
	}
	
	public UpdateState(List<Player> players) {
		this.header=UpdateStateTags.PLAYERS.toString();
		this.players=players;
	}
	
	public UpdateState(Market market) {
		this.header=UpdateStateTags.MARKET.toString();
		this.itemsOnSale = market.getItemsOnSale();
	}
	
	public String getHeader() {
		return this.header;
	}
	
	public Board getBoard() {
		return this.board;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public List<ItemOnSale> getItemsOnSale() {
		return this.itemsOnSale;
	}
	
	public List<Player> getPlayers() {
		return this.players;
	}
	
	public String getMessage() {
		return this.message;
	}
}
