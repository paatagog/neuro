package ge.amigo.neuro.console.client.ui.map;

/**
 * ობიექტი რომელიც შეიძლება დაიხატოს რუკაზე
 */
public class Mapable {
	
	private static int uniqueId = 1;
	
	private Runnable eventHandler;
	
	private boolean editable;
	
	private String getUniqueId() {
		return String.valueOf(uniqueId++);
	}
	
	public Mapable () {
		id = getUniqueId();
	}

	/**
	 * იდენტიფიკატორი
	 */
	private String id;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public Runnable getEventHandler() {
		return eventHandler;
	}

	public void setEventHandler(Runnable eventHandler) {
		this.eventHandler = eventHandler;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isEditable() {
		return editable;
	}
	
}