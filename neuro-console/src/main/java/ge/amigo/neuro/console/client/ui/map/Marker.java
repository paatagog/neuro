package ge.amigo.neuro.console.client.ui.map;

import ge.amigo.neuro.console.client.math.SphericalTimePoint;

/**
 * მარკერი
 */
public class Marker extends Mapable {
	
	public Marker() {
		super();
	}
	
	public Marker(SphericalTimePoint position, String icon, String title) {
		this();
		this.position = position;
		this.icon = icon;
		this.title = title;
	}
	
	/**
	 * სათაური
	 */
	private String title;

	/**
	 * მდებარეობა
	 */
	private SphericalTimePoint position;
	
	/**
	 * ხატულა
	 */
	private String icon;

	public SphericalTimePoint getPosition() {
		return position;
	}

	public void setPosition(SphericalTimePoint position) {
		this.position = position;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
