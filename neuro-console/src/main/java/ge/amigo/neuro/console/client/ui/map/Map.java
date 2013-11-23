package ge.amigo.neuro.console.client.ui.map;

import ge.amigo.neuro.console.client.math.SphericalTimePoint;

import java.util.List;

public interface Map {
	
	static String EDITMODE_NONE = null;
	static String EDITMODE_CREATE_MARKER = "marker";
//	static String EDITMODE_CREATE_POLYLINE = "polyline";
//	static String EDITMODE_CREATE_POLYGON = "polygon";
//	static String EDITMODE_REMOVE = "remove";
	
    /**
  	 * ხატავს რუკას. თუ საჭიროა ქაჩავს სკრიპტს.
  	 * დახატვის მერე იძახებს ქოლბექ ფუნქციას
  	 */
	void draw(Runnable callback);
	
  	/**
  	 * რუკის ცენტრის კოორდინატების დაყენება
  	 */
	void setCenter(SphericalTimePoint p);
	SphericalTimePoint getCenter();
	
  	/**
  	 * რუკის მასშტაბის დაყენება
  	 * 1000 ნიშნავს რუკას რომლის მასშტაბია 1:1000
  	 */
	void setZoom(int zoom);
	int getZoom();
	
	void fitAll();

	/**
	 * ობიექტის დახატვა
	 */
	void add(Mapable m);
	
	/**
	 * ობიექტის წაშლა
	 */
	void remove(Mapable m);

	/**
	 * ობიექტის განახლება
	 */
	void update(Mapable m);
	
	/**
	 * რუკის რედაქტირების რეჟიმის შეცვლა
	 * EDITMODE_NONE, EDITMODE_CREATE_MARKER
	 */
	void setEditMode(String mode);
	
	/**
	 * მარკერების მიღება რუკიდან
	 */
	List<Marker> getMarkers();
	
	/**
	 * რუკაზე გამოსახული ყველა ობიექტის წაშლა
	 */
	void clean();
	
}
