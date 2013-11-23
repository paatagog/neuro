package ge.amigo.neuro.console.client.ui.utils;

import ge.amigo.neuro.console.client.Messages;
import ge.amigo.neuro.console.client.ui.LocalRegistry;

import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.InfoConfig;

public class UIUtils {
	
	private static Messages messages;
	
	public static final String DATE_PATTERN = "dd/MM/yyyy";
	public static final String DATETIME_PATTERN = "dd/MM/yyyy HH:mm:ss";
	
	public static String getMessage(String key) {
		if (messages == null) {
			messages = LocalRegistry.getMessages();  
		}
		return messages == null ? key : messages.getString(key);
	}
	
	public static String getMessage(String key, String... params) {
		if (messages == null) {
			messages = LocalRegistry.getMessages();
		}
		if (params == null || params.length == 0) {
			return getMessage(key);
		}
		return messages == null ? key : Format.substitute(messages.getString(key), (Object[]) params);
	}
	
	public static boolean isEmpty(String str) {
		return str == null || "".equals(str);
	}
	
	private static int getDelayTime(String message) {
		int delay = 0;
		if (message != null && !message.equals("")) {
			delay = (message.length() / 10) * 2000;
		}		
		return delay < 3000 ? 3000 : ( delay > 12000 ? 12000 : delay);
		
	}

	public static void note(String message) {
		note(null, message, getDelayTime(message), 0, 0);
	}

	public static void note(String title, String message) {
		int delay = getDelayTime(message);
		note(title, message, delay, 0, 0);
	}

	public static void note(String title, String message, int delay, int width, int height) {
		delay = delay == 0 ? getDelayTime(message) : delay;
		title = title == null ? getMessage("note") : title;
		InfoConfig ic = new InfoConfig(title, message);
		ic.display = delay;
		ic.width = width == 0 ? 250 : width;
		ic.height = height == 0 ? 90 : height;
		Info.display(ic);
	}
	

}
