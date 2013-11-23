package ge.amigo.neuro.console.client.ui.map;

import ge.amigo.neuro.console.client.math.SphericalTimePoint;

import java.util.List;

/**
 * მრავალკუთხედი
 */
public class Polygon extends Mapable {
	
	public Polygon() {
		super();
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

	/**
	 * ფონის ფერი
	 */
	private String fillColor;

	/**
	 * ფონის გამჭვირვალობა. 100 ნიშნავს სრულიად გამჭვირვალეს, ხოლო 0 გაუმჭვირვალეს
	 */
	private int fillOpacity;
	
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

	public String getFillColor() {
		return fillColor;
	}

	public void setFillColor(String fillColor) {
		this.fillColor = fillColor;
	}

	public int getFillOpacity() {
		return fillOpacity;
	}

	public void setFillOpacity(int fillOpacity) {
		this.fillOpacity = fillOpacity;
	}
	
}
