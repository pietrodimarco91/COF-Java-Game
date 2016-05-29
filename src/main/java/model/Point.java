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
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public void setLocation(double arg0, double arg1) {

	}
	
	/*public boolean equals(Point2D point) {
		if(this.x==point.getX()&&this.y==point.getY())
			return true;
		else 
			return false;
	}*/

}