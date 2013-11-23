package ge.amigo.neuro.console.client.utils;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;


public class Logger {
	
	public static StringBuilder log;
	
	private static String DATE_TIME_FORMAT_STRING = "yyyy.MM.dd HH:mm:ss";
//	private SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat(DATE_TIME_FORMAT_STRING);
	private DateTimeFormat DATE_TIME_FORMAT = DateTimeFormat.getFormat(DATE_TIME_FORMAT_STRING);
	private String className;
	
	public Logger (String className) {
		this.className = className;
	}
	
	public static Logger getLogger(String className) {
		return new Logger(className);
	}
	
	public void info(String message) {
		String text = DATE_TIME_FORMAT.format(new Date()) + ":" + className + ": " + message;
		log.append(text);
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
