package ge.amigo.neuro.console.client.ui.map;

import ge.amigo.neuro.console.client.math.SphericalTimePoint;

public class InfoWindow extends Mapable {

	private String content;
	
	private Marker marker;
	
	private SphericalTimePoint position;
	
	private int maxWidth;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Marker getMarker() {
		return marker;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	public SphericalTimePoint getPosition() {
		return position;
	}

	public void setPosition(SphericalTimePoint position) {
		this.position = position;
	}

	public int getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}
	
	
}
