package model;

import java.io.Serializable;

/**
 * This class represents a Councillor. Each councillor is instantiated at the
 * beginning of the match.
 */
public class Councillor implements Serializable {
	/**
	 * Variable used for serialization
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Color of the Councillor
	 */
	private String color;

	/**
	 * Each councillor is created with its own color. Notice that each
	 * councillor is created at the beginning of the match and kept inside the
	 * CouncillorsPool, if it isn't inside a Council.
	 */
	public Councillor(String color) {
		this.color = color;
	}

	/**
	 * @return String Returns the color of councillor
	 */

	public String getColor() {
		return this.color;
	}
	
	public String toString() {
		return color;
	}
}