package Turtle;
import java.awt.geom.Point2D;

public class Turtle {
	private Point2D point;
	private int angle;

	public Turtle() {
		point = new Point2D.Double(0.0, 0.0);
		angle = 0;
	}

	public void move(int distance) {
		double radian = Math.PI * ((double) angle / 180);
		double deltaX = Math.cos(radian) * distance;
		double deltaY = Math.sin(radian) * distance;
		point.setLocation(point.getX()+deltaX, point.getY()+deltaY);
	}

	public void turn(int angle) {
		this.angle = this.angle+angle;
	}

	public double direction() {
		return angle;
	}

	public Point2D location() {
		return point;
	}

	public double distanceTo(Point2D labelPoint){
		return Math.sqrt((point.getX()- labelPoint.getX())*(point.getX()- labelPoint.getX()) + (point.getY()- labelPoint.getY())*(point.getY()- labelPoint.getY()));
	}

	public double bearingTo(Point2D labelPoint){
		return Math.toDegrees(Math.atan((labelPoint.getY() - point.getY())/(labelPoint.getX() - point.getX())));
	}
}
