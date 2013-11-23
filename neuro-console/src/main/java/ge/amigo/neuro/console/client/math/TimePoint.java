package ge.amigo.neuro.console.client.math;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

public class TimePoint extends Point {

	public static final String DATE_TIME_FORMAT_STRING = "dd/MM/yyyy HH:mm:ss";
	public static final DateTimeFormat dtf = DateTimeFormat.getFormat(DATE_TIME_FORMAT_STRING);

	public Date time;
	
	public TimePoint() {
		
	}
	
	public TimePoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public TimePoint(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public TimePoint(Date time, double x, double y) {
		this.time = time;
		this.x = x;
		this.y = y;
	}
	
	public TimePoint(Date time, double x, double y, double z) {
		this.time = time;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public String toString() {
		return x + ", " + y + ", " + z + ", " + (time == null ? "" : dtf.format(time));
	}

}
