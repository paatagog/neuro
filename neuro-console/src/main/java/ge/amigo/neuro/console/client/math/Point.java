package ge.amigo.neuro.console.client.math;

public class Point {
	
	public double x;
	
	public double y;

	public double z;
	
	public Point() {
		
	}
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point(String p) {
		init(p, ",");
	}
	
	public Point(String p, String separator) {
		init(p, separator);
	}
	
	private void init(String p, String separator) {
		if (p != null) {
			String[] coordinates = p.split(separator);
			x = Double.parseDouble(coordinates[0].trim());
			y = Double.parseDouble(coordinates[1].trim());
			if (coordinates.length > 2) {
				z = Double.parseDouble(coordinates[2].trim());
			}
		}
	}

	public String toString() {
		return x + "," + y + "," + z;
	}
	
}
