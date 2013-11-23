package ge.amigo.neuro.console.client.math;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

public class SphericalTimePoint extends SphericalPoint {
	
	public static final String DATE_TIME_FORMAT_STRING = "dd/MM/yyyy HH:mm:ss";
	public static final DateTimeFormat dtf = DateTimeFormat.getFormat(DATE_TIME_FORMAT_STRING);
	
	public Date time;
	
	public SphericalTimePoint() {
		
	}
	
	public SphericalTimePoint(double lat, double lon) {
		this.lon = lon;
		this.lat = lat;
	}

	public SphericalTimePoint(double lat, double lon, double ele) {
		this.lon = lon;
		this.lat = lat;
		this.ele = ele;
	}

	public SphericalTimePoint(Date time, double lat, double lon) {
		this.lon = lon;
		this.lat = lat;
		this.time = time;
	}

	public SphericalTimePoint(Date time, double lat, double lon, double ele) {
		this.lon = lon;
		this.lat = lat;
		this.ele = ele;
		this.time = time;
	}

	public String toString() {
		return lat + ", " + lon + ", " + ele + ", " + (time == null ? "" : dtf.format(time));
	}
	
	
}
