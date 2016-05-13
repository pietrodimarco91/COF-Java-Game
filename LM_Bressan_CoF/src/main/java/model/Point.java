package model;

import java.awt.geom.Point2D;
import java.util.*;

/**
 * This class represents the coordinates of the city inside the map through the
 * (x,y) coordinates system.
 */
public class Point extends Point2D {

	/**
	 * The x coordinate of the point
	 */
	private int x;

	/**
	 * The y coordinate of the point
	 */
	private int y;

	/**
	 * Default constructor
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public double getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setLocation(double arg0, double arg1) {
		// TODO Auto-generated method stub

	}

}