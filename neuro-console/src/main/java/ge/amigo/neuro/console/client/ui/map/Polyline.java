package ge.amigo.neuro.console.client.ui.map;

import ge.amigo.neuro.console.client.math.SphericalTimePoint;

import java.util.List;

/**
 * ტეხილი
 */
public class Polyline extends Mapable {
	
	public Polyline() {
		super();
	}
	
	public Polyline(List<SphericalTimePoint> vertices, String color, int width, int opacity) {
		this();
		this.vertices = vertices;
		this.color = color;
		this.width = width;
		this.opacity = opacity;
	}

	/**
	 * წვეროები
	 */
	private List<SphericalTimePoint> vertices;
	
	/**
	 * ფერი
	 */
	private String color;
	
	/**
	 * ხაზის სისქე
	 */
	private int width;
	
	/**
	 * გამჭვირვალობა. 100 ნიშნავს სრულიად გამჭვირვალეს, ხოლო 0 გაუმჭვირვალეს
	 */
	private int opacity;

	public List<SphericalTimePoint> getVertices() {
		return vertices;
	}

	public void setVertices(List<SphericalTimePoint> vertices) {
		this.vertices = vertices;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getOpacity() {
		return opacity;
	}

	public void setOpacity(int opacity) {
		this.opacity = opacity;
	}
}