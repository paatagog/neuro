package ge.amigo.neuro.console.client.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * ტექსტის ფორმატერი ლოგერისთვის
 */
public class SimpleFormatter extends Formatter {
	
    Date dat = new Date();
    private final static String format = "{0,date} {0,time}";
    private MessageFormat formatter;

    private Object args[] = new Object[1];

    private String lineSeparator = "\n";

    public static void setLoggerFile(Logger logger, String fileName) {
		try {
			Formatter formatterTxt = new SimpleFormatter();
			FileHandler logFile = new FileHandler(fileName);
			logFile.setFormatter(formatterTxt);
			logFile.setEncoding(null);
			logger.addHandler(logFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }
    
    /**
     * Format the given LogRecord.
     * @param record the log record to be formatted.
     * @return a formatted log record
     */
    public synchronized String format(LogRecord record) {
	StringBuffer sb = new StringBuffer();
	// Minimize memory allocations here.
	dat.setTime(record.getMillis());
	args[0] = dat;
	StringBuffer text = new StringBuffer();
	if (formatter == null) {
	    formatter = new MessageFormat(format);
	}
	formatter.format(args, text, null);
	sb.append(text);
	sb.append(" ");
	if (record.getSourceClassName() != null) {	
	    sb.append(record.getSourceClassName());
	} else {
	    sb.append(record.getLoggerName());
	}
	if (record.getSourceMethodName() != null) {	
	    sb.append(" ");
	    sb.append(record.getSourceMethodName());
	}
	String message = formatMessage(record);
	sb.append(record.getLevel().getLocalizedName());
	sb.append(": ");
	sb.append(message);
	sb.append(lineSeparator);
	if (record.getThrown() != null) {
	    try {
	        StringWriter sw = new StringWriter();
	        PrintWriter pw = new PrintWriter(sw);
	        record.getThrown().printStackTrace(pw);
	        pw.close();
		sb.append(sw.toString());
	    } catch (Exception ex) {
	    }
	}
	return sb.toString();
    }
}
