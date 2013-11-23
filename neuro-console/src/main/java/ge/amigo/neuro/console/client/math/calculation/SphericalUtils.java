package ge.amigo.neuro.console.client.math.calculation;

import ge.amigo.neuro.console.client.math.Point;
import ge.amigo.neuro.console.client.math.SphericalPoint;
import ge.amigo.neuro.console.client.math.SphericalTimePoint;
import ge.amigo.neuro.console.client.math.TimePoint;

import java.util.ArrayList;
import java.util.List;

public class SphericalUtils {
	
	/** earth radius at equator in meters */
	public static final double RADIUS_EQUATOR = 6378137; // equatorial radius in M
	
	public static final double RADIUS_POLAR = 6356752.3; // polar radius in M

	/** circumference of earth at equator in meters */
	public static final double CIRCUMFERENCE_EQUATOR = RADIUS_EQUATOR * 2 * Math.PI; // equatorial circumference in m

	/** meters per degree at equator */
	public static final double M_PER_DEGREE = CIRCUMFERENCE_EQUATOR / 360.0;
	
	/** meters per radian at equator */
	public static final double M_PER_RADIAN = CIRCUMFERENCE_EQUATOR / (2 * Math.PI);
	
	public static double F = (RADIUS_EQUATOR - RADIUS_POLAR) / RADIUS_EQUATOR;
    
	public static double E2 = (2 * F) - Math.pow(F, 2);

	static final double EPSILON = 1E-20;
	
	public static double distance(SphericalPoint p1, SphericalPoint p2) {
		return distance(p1.lon, p1.lat, p2.lon, p2.lat);
	}
	  
	/**
	 * Calculates the distance between two points.
	 * 
	 * @param latitude1 the latitude of the first point given in degrees.
	 * @param longitude1 the longitude of the first point given in degrees.
	 * @param latitude2 the latitude of the second point given in degrees.
	 * @param longitude2 the longitude of the second point given in degrees.
	 * @return the distance in meters.
	 */
	public static double distance(double longitude1, double latitude1, double longitude2, double latitude2) {
		double distance_deg = distanceDegrees(longitude1, latitude1, longitude2, latitude2);
		return (distance_deg * M_PER_DEGREE);
	}

	/**
	 * Calculates the distance between two points.
	 *
	 * @param longitude1 the longitude of the first point given in degrees.
	 * @param latitude1 the latitude of the first point given in degrees.
	 * @param longitude2 the longitude of the second point given in degrees.
	 * @param latitude2 the latitude of the second point given in degrees.
	 * @return the distance in degrees.
	 */
	public static double distanceDegrees(double longitude1, double latitude1,
			double longitude2, double latitude2) {

		double distance_rad = distanceRadians(
				Math.toRadians(longitude1), Math.toRadians(latitude1),
				Math.toRadians(longitude2), Math.toRadians(latitude2));
		
		return (Math.toDegrees(distance_rad));
	}

	/**
	 * Calculates the distance between two points.
	 *
	 * @param longitude1 the longitude of the first point given in radians.
	 * @param latitude1 the latitude of the first point given in radians.
	 * @param longitude2 the longitude of the second point given in radians.
	 * @param latitude2 the latitude of the second point given in radians.
	 * @return the distance in radians.
	 */
	public static double distanceRadians(double longitude1, double latitude1,
			double longitude2, double latitude2) {
	
		double pdiff = Math.sin((latitude2 - latitude1) / 2);
		double ldiff = Math.sin((longitude2 - longitude1) / 2);
		double rval = Math.sqrt((pdiff * pdiff) + Math.cos(latitude2) * Math.cos(latitude1) * (ldiff * ldiff));
		return (2.0 * Math.asin(rval));
	}

	/**
	 * Calculates the course between two points.
	 *
	 * @param longitude1 the longitude of the first point given in degrees.
	 * @param latitude1 the latitude of the first point given in degrees.
	 * @param longitude2 the longitude of the second point given in degrees.
	 * @param latitude2 the latitude of the second point given in degrees.
	 * @return the course in degrees [0,360].
	 */
	public static double courseDegrees(double longitude1, double latitude1,
			double longitude2, double latitude2) {

		double course = Math.toDegrees(courseRadians(
				Math.toRadians(longitude1), Math.toRadians(latitude1),
				Math.toRadians(longitude2), Math.toRadians(latitude2)));

		return (course < 0.0) ? (360.0 + course) : (course);
	}
	  
	/**
	 * Calculates the course between two points.
	 *
	 * @param longitude1 the longitude of the first point given in radians.
	 * @param latitude1 the latitude of the first point given in radians.
	 * @param longitude2 the longitude of the second point given in radians.
	 * @param latitude2 the latitude of the second point given in radians.
	 * @return the course in radians (-pi,pi).
	 */
	public static double courseRadians(double longitude1, double latitude1, double longitude2, double latitude2) {
		
		if (Math.abs(Math.cos(latitude1)) < EPSILON) {
			return (latitude1 > 0) ? (Math.PI) : (0.0); // starting from S pole
		}

		double ldiff = longitude2 - longitude1;
		double cosphi = Math.cos(latitude2);

		return Math.atan2(cosphi * Math.sin(ldiff), 
				(Math.cos(latitude1) * Math.sin(latitude2) - Math.sin(latitude1) * cosphi * Math.cos(ldiff)));
	}
	
    public static double distanceToSegment(SphericalPoint p1, SphericalPoint p2, SphericalPoint p3) {
    	return distanceToSegment(p1.lon, p1.lat, p2.lon, p2.lat, p3.lon, p3.lat);
    }
    
    public static double distanceToSegment(double lon1, double lat1, double lon2, double lat2, double lon, double lat) {
		
    	double[] coords = getFlatCoordinates(new double[] {lon1, lat1, lon2, lat2, lon, lat});
    	
    	double x1 = coords[0];
    	double y1 = coords[1]; 
    	double x2 = coords[2];
    	double y2 = coords[3];
    	double x = coords[4];
    	double y = coords[5];
    	
    	double[] pxy = getPxy(x, y, x1, y1, x2, y2);
    	if (pxy[0] < Math.max(x1, x2) && pxy[0] > Math.min(x1, x2) || pxy[1] < Math.max(y1, y2) && pxy[1] > Math.min(y1, y2)) {
    		return distanceEuclid(x, y, pxy[0], pxy[1]);
    	} else {
    		double d1 = distanceEuclid(x, y, x1, y1);
    		double d2 = distanceEuclid(x, y, x2, y2);
    		return d1 < d2 ? d1 : d2;
    	}
    }
    
    public static double distanceEuclid(double x1, double y1, double x2, double y2) {
    	return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
    
	/**
     * ბრტყელი (x,y) კოორდინატის მიღება ლოკალური აპროქსიმაციის შედეგად (http://williams.best.vwh.net/avform.htm#flat)
     */
	public static List<TimePoint> getFlatTimeCoordinates(List<SphericalTimePoint> sphericalCoords) {

		double[] sphericalArray = new double[sphericalCoords.size() * 2];
    	for (int i = 0; i < sphericalCoords.size(); i++) {
    		sphericalArray[2 * i] = sphericalCoords.get(i).lon;
    		sphericalArray[2 * i + 1] = sphericalCoords.get(i).lat;
    	}
		
		double[] coordArray = getFlatCoordinates(sphericalArray);
		
		List<TimePoint> points = new ArrayList<TimePoint> ();
		
    	for (int i = 0; i < coordArray.length / 2; i++) {
    		TimePoint point = new TimePoint(sphericalCoords.get(i).time, coordArray[2 * i], coordArray[2 * i + 1], sphericalCoords.get(i).ele);
    		points.add(point);
    	}
    	
    	return points;
	}

	/**
     * ბრტყელი (x,y) კოორდინატის მიღება ლოკალური აპროქსიმაციის შედეგად (http://williams.best.vwh.net/avform.htm#flat)
     */
    public static List<Point> getFlatCoordinates(List<SphericalPoint> sphericalCoords) {
    	
    	double[] sphericalArray = new double[sphericalCoords.size() * 2];
    	for (int i = 0; i < sphericalCoords.size(); i++) {
    		sphericalArray[2 * i] = sphericalCoords.get(i).lon;
    		sphericalArray[2 * i + 1] = sphericalCoords.get(i).lat;
    	}
		
		double[] coordArray = getFlatCoordinates(sphericalArray);
		
		List<Point> points = new ArrayList<Point> ();
		
    	for (int i = 0; i < coordArray.length / 2; i++) {
    		Point point = new Point(coordArray[2 * i], coordArray[2 * i + 1], sphericalCoords.get(i).ele);
    		points.add(point);
    	}
    	
    	return points;
    }
    
    /**
     * ბრტყელი (x,y) კოორდინატის მიღება ლოკალური აპროქსიმაციის შედეგად (http://williams.best.vwh.net/avform.htm#flat)
     */
    public static double[] getFlatCoordinates(double[] coords) {
    	
    	for (int i = 0; i < coords.length; i++) {
    		coords[i] =  Math.toRadians(coords[i]);
    	}
		
		double r1 = RADIUS_EQUATOR * (1 - E2) / Math.pow(1 - E2 * Math.pow(Math.sin(coords[1]), 2), 3./2.);
		double r2 = RADIUS_EQUATOR / Math.sqrt(1 - E2 * Math.pow(Math.sin(coords[1]), 2));

		for (int i = 1; i < coords.length / 2; i++) {
			coords[i * 2] = r2 * Math.cos(coords[1])* (coords[i * 2] - coords[0]);
			coords[i * 2 + 1] = r1 * (coords[i * 2 + 1] - coords[1]);
		}
		
		coords[0] = 0;
		coords[1] = 0;
		
    	return coords;
    }
    
	public static double[] getPxy (double x, double y, double x1, double y1, double x2, double y2) {
		
		double l = x1 - x2;
		double m = y1 - y2;
		double p = x - x1;
		double q = y - y1;
		
		return new double[] {x - (m * p - l * q) / (m * m + l * l) * m, y + (m * p - l * q) / ( m * m + l * l) * l};
	}

}
