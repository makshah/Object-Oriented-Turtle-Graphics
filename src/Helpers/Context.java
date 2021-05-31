package Helpers;

import java.awt.geom.Point2D;
import java.util.Hashtable;

import Turtle.Turtle;

public class Context {

	private Hashtable<String, Integer> values;
	private Hashtable<String, Point2D> points;
	private Turtle t;

	public Context() {
		values = new Hashtable<>();
		points = new Hashtable<>();
		t = new Turtle();
	}

	public void setTurtle(Turtle t) {
		this.t = t;
	}

	public Turtle getTurtle() {
		return t;
	}

	public int getValue(String key) {
		if (values.get(key) != null)
			return values.get(key);
		else {
			return Integer.MIN_VALUE;
		}
	}

	public Point2D getPoint(String key){
		if(points.get(key) != null)
			return points.get(key);
		else
			return null;
	}

	public void setValue(String key, int value) {
		values.put(key, value);
	}

	public void setPoint(String key, Point2D point) {
		points.put(key, point);
	}

	public void removeValues() {
		values.clear();
	}

	public void removePoints() {
		points.clear();
	}
}
