package ge.amigo.neuro.console.client.math;

public class SphericalPoint {

	/**
	 * Longitude i.e x
	 */
	public double lon;

	/**
	 * Latitude i.e y
	 */
	public double lat;

	/**
	 * Elevation i.e z
	 */
	public double ele;
	
	public SphericalPoint() {
		
	}

	public SphericalPoint(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}
	
	public SphericalPoint(double lat, double lon, double ele) {
		this.lat = lat;
		this.lon = lon;
		this.ele = ele;
	}
 
	public SphericalPoint(String p) {
		init(p, ",");
	}
	
	public SphericalPoint(String p, String separator) {
		init(p, separator);
	}
	
	private void init(String p, String separator) {
		if (p != null) {
			String[] coordinates = p.split(separator);
			lon = Double.parseDouble(coordinates[0].trim());
			lat = Double.parseDouble(coordinates[1].trim());
			if (coordinates.length > 2) {
				ele = Double.parseDouble(coordinates[2].trim());
			}
		}
	}
	

	public String toString() {
		return lat + "," + lon + "," + ele;
	}
}
