package ge.amigo.neuro.console.client.math;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.i18n.client.DateTimeFormat;

public class Utils {
	
	public static List<Point> getPointsFromGoogleFormat(String pointsString) {
		return getPointsFromGoogleFormat(pointsString, " ", ",");
	}

	public static List<SphericalPoint> getSphericalPoints(String pointsString) {
		return getSphericalPointsFromGoogleFormat(pointsString, " ", ",");
	}

	public static List<Point> getPointsFromGoogleFormat(String pointsString, String separator) {
		return getPointsFromGoogleFormat(pointsString, separator, ",");
	}

	public static List<SphericalPoint> getSphericalPoints(String pointsString, String separator) {
		return getSphericalPointsFromGoogleFormat(pointsString, separator, ",");
	}

	public static List<Point> getPointsFromGoogleFormat(String pointsString, String separator, String pointSeparator) {
		List<Point> points = new ArrayList<Point>();
		if (pointsString != null) {
			String[] pointStrings = pointsString.split(separator);
			for(int i = 0; i < pointStrings.length; i++) {
				points.add(new Point(pointStrings[i], pointSeparator));
			}
		}
		return points;
	}

	public static List<SphericalPoint> getSphericalPointsFromGoogleFormat(String pointsString, String separator, String pointSeparator) {
		List<SphericalPoint> points = new ArrayList<SphericalPoint>();
		if (pointsString != null) {
			String[] pointStrings = pointsString.split(separator);
			for(int i = 0; i < pointStrings.length; i++) {
				points.add(new SphericalPoint(pointStrings[i], pointSeparator));
			}
		}
		return points;
	}
	
	public static List<SphericalPoint> getSphericalPointsFromCsv(String csv) {
		List<SphericalPoint> sphericalPoints = new ArrayList<SphericalPoint> ();
		String[] ps = csv == null ? new String[0] : csv.split("\n"); 
		for (int i = 0; i < ps.length; i++) {
			String[] cs = ps[i].split(",");
			SphericalPoint p = new SphericalPoint();
			p.lat = Double.parseDouble(cs[2]);
			p.lon = Double.parseDouble(cs[3]);
			p.ele = Double.parseDouble(cs[4]);
			sphericalPoints.add(p);
		}
		return sphericalPoints;
		
	}

	public static List<SphericalTimePoint> getSphericalTimePointsFromCsv(String csv) {
		DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss"); 
		List<SphericalTimePoint> sphericalTimePoints = new ArrayList<SphericalTimePoint> ();
		String[] ps = csv == null ? new String[0] : csv.split("\n"); 
		for (int i = 0; i < ps.length; i++) {
			String[] cs = ps[i].split(",");
			SphericalTimePoint p = new SphericalTimePoint();
			p.lat = Double.parseDouble(cs[2]);
			p.lon = Double.parseDouble(cs[3]);
			p.ele = Double.parseDouble(cs[4]);
			cs[5] = cs[5].replace("T", " ").replace("Z", "");
			p.time = dtf.parse(cs[5]);
			sphericalTimePoints.add(p);
		}
		return sphericalTimePoints;
		
	}
}
