package map;

public class Point {
	private int x;
	private int y;
	private int distance;
	
	public Point (int x, int y, int distance) {
		this.x = x;
		this.y = y;
		this.distance = distance;
	}
	
	public void setDistance(int i) {
		distance = i;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public int getX () {
		return x;
	}
	public int getY () {
		return y;
	}
}
