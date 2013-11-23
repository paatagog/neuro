package ge.amigo.neuro.console.client.ui.map;

import ge.amigo.neuro.console.client.math.SphericalTimePoint;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.google.gwt.user.client.Element;

public class GoogleMapWidget extends LayoutContainer implements Map {
	
	private int[] discreteZooms = new int[] { 160934400, 80467200, 40233600, 18933450, 9466720, 4733360, 2299060, 1192100, 574760, 277470, 139940, 73150, 37030, 18510, 9090, 4540, 2270, 1000};

	private static int callbackNumber = 1;
	
	private static int objectUniqueId = 1;
	
	private static int mapUniqueId = 0;
	
	private int zoom = 10000;
	
	private SphericalTimePoint center = new SphericalTimePoint(41.397, 41.644);
	
	private List<Marker> markers = new ArrayList<Marker>();
	
	private List<Polygon> polygons = new ArrayList<Polygon>();

	private List<Polyline> polylines = new ArrayList<Polyline>();

	private List<InfoWindow> infoWindows = new ArrayList<InfoWindow>();

	public GoogleMapWidget() {
		mapUniqueId ++;
		setId((this.getClass().toString() + Math.random()).replace(".", "").replace(" ", ""));
		registerMapInJs(this.getId(), mapUniqueId);
	}
	
	@Override
	protected void onRender(Element parent, int pos) {
		super.onRender(parent, pos);
	}

	@Override
	public void draw(Runnable callback) {
		int gZoom = 8;
		publishMainFunction(mapUniqueId, callback);
		publishClickCallback(getId(), mapUniqueId);
  		drawMap(getId(), mapUniqueId, center.lat, center.lon, gZoom, "googleMap_runnableCallback" + callbackNumber + "()");
  		callbackNumber = callbackNumber == 1 ? 2 : 1;
  	}

	@Override
	public void setCenter(SphericalTimePoint center) {
		this.center = center;
  		setCenter(getId(), center.lat, center.lon);
  	}

	@Override
	public void setZoom(int zoom) {
		this.zoom = zoom;
		int z = 0;
		int min = Math.abs(discreteZooms[0] - zoom);
		for (int i=1; i < discreteZooms.length; i++) {
			int d = Math.abs(discreteZooms[i] - zoom);
			if (d < min) {
				min = d;
				z = i;
			}
		}
  		setZoom(getId(), z);
  	}
	
	@Override
	public void fitAll() {
		fitAll(getId());
	}

	@Override
	public int getZoom() {
		return zoom;
	}

	@Override
	public SphericalTimePoint getCenter() {
		return center;
	}

	@Override
	public void add(Mapable m) {
		if (m instanceof Marker) {
			Marker ma = (Marker) m;
			if (getMarker(m.getId()) == null) {
				if (ma.getId()==null) {
					ma.setId(getObjectUniqueId());
				}
				markers.add(ma);
				addMarker(getId(), m.getId(), ma.getTitle(), ma.getPosition().lat, ma.getPosition().lon, ma.getIcon(), ma.isEditable(), null, null);
				if (ma.getEventHandler() != null) {
					addMarkerEventHandler(getId(), m.getId());
				}
			}
		} else if (m instanceof Polygon) {
			Polygon p = (Polygon) m;
			if (getPolygon(p.getId()) == null) {
				if (p.getId()==null) {
					p.setId(getObjectUniqueId());
				}
				polygons.add(p);
				addPolygon(getId(), m.getId(), getJavascriptArray(p.getVertices()), adjustOpacity(p.getOpacity()), p.getWidth(), p.getColor(), p.getFillColor(), adjustOpacity(p.getFillOpacity()));
			}
		} else if (m instanceof Polyline) {
			Polyline p = (Polyline) m;
			if (getPolyline(p.getId()) == null) {
				if (p.getId()==null) {
					p.setId(getObjectUniqueId());
				}
				polylines.add(p);
				addPolyline(getId(), m.getId(), getJavascriptArray(p.getVertices()), adjustOpacity(p.getOpacity()), p.getWidth(), p.getColor());
			}
		} else if (m instanceof InfoWindow) {
			InfoWindow i = (InfoWindow) m;
			if (getInfoWindow(i.getId()) == null) {
				if (i.getId()==null) {
					i.setId(getObjectUniqueId());
				}
				infoWindows.add(i);
				addInfoWindow(getId(), m.getId(), i.getContent(), i.getMarker() == null ? null : i.getMarker().getId(), i.getPosition() == null ? 0 : i.getPosition().lat, i.getPosition() == null ? 0 : i.getPosition().lon, i.getMaxWidth());
			}
		}
	}

	@Override
	public void remove(Mapable m) {
		if (m instanceof Marker) {
			Marker ma = getMarker(m.getId());
			if (ma != null) {
				markers.remove(ma);
				removeMarker(getId(), m.getId());
			}
		} else if (m instanceof Polygon) {
			Polygon p = getPolygon(m.getId());
			if (p != null) {
				polygons.remove(p);
				removePolygon(getId(), m.getId());
			}
		} else if (m instanceof Polyline) {
			Polyline p = getPolyline(m.getId());
			if (p != null) {
				polylines.remove(p);
				removePolyline(getId(), m.getId());
			}
		} else if (m instanceof InfoWindow) {
			InfoWindow i = getInfoWindow(m.getId());
			if (i != null) {
				infoWindows.remove(i);
				removeInfoWindow(getId(), m.getId());
			}
		}
	}

	@Override
	public void update(Mapable m) {
		if (m instanceof Marker) {
			Marker ma = (Marker)(m);
			Marker marker = getMarker(m.getId());
			if (marker != null) {
				marker.setIcon(ma.getIcon());
				marker.setPosition(new SphericalTimePoint(ma.getPosition().lat, ma.getPosition().lon));
				marker.setTitle(ma.getTitle());
				updateMarker(getId(), m.getId(), ma.getTitle(), ma.getPosition().lat, ma.getPosition().lon, ma.getIcon(), ma.isEditable(), null, null);
				removeMarkerEventHandler(getId(), m.getId());
				if (ma.getEventHandler() != null) {
					addMarkerEventHandler(getId(), m.getId());
				}
			}
		} else if (m instanceof Polygon) {
			Polygon p = (Polygon) m;
			Polygon polygon = getPolygon(p.getId());
			if (polygon != null) {
				polygon.setColor(p.getColor());
				polygon.setOpacity(p.getOpacity());
				polygon.setWidth(p.getWidth());
				polygon.setVertices(p.getVertices());				
				updatePolygon(getId(), m.getId(), getJavascriptArray(p.getVertices()), adjustOpacity(p.getOpacity()), p.getWidth(), p.getColor(), p.getFillColor(), adjustOpacity(p.getFillOpacity()));
			}			
		} else if (m instanceof Polyline) {
			Polyline p = (Polyline) m;
			Polyline polyline = getPolyline(p.getId());
			if (polyline != null) {
				polyline.setColor(p.getColor());
				polyline.setOpacity(p.getOpacity());
				polyline.setWidth(p.getWidth());
				polyline.setVertices(p.getVertices());				
				updatePolyline(getId(), m.getId(), getJavascriptArray(p.getVertices()), adjustOpacity(p.getOpacity()), p.getWidth(), p.getColor());
			}
		} if (m instanceof InfoWindow) {
				InfoWindow iw = (InfoWindow)(m);
				InfoWindow infoWind = getInfoWindow(m.getId());
				if (infoWind != null) {
					infoWind.setContent(iw.getContent());
					infoWind.setPosition(new SphericalTimePoint(iw.getPosition() == null ? 0 : iw.getPosition().lat, iw.getPosition() == null ? 0 : iw.getPosition().lon));
					infoWind.setMarker(iw.getMarker());
					infoWind.setMaxWidth(iw.getMaxWidth());
					updateInfoWindow(getId(), m.getId(), iw.getContent(), iw.getMarker() == null ? null : iw.getMarker().getId(), iw.getPosition() == null ? 0 : iw.getPosition().lat, iw.getPosition() == null ? 0 : iw.getPosition().lon, iw.getMaxWidth());
				}
			}
	}
	
	private double adjustOpacity(int opacity) {
		return (double) opacity / 100;
	}
	
	private Marker getMarker(String id) {
		Marker result = null;
		if (markers != null) {
			for (Marker item : markers) {
				if (item.getId().equals(id)) {
					result = item;
					break;
				}
			}
		}		
		return result;	
	}
	
	private Polygon getPolygon(String id) {
		Polygon result = null;
		if (polygons != null) {
			for (Polygon item : polygons) {
				if (item.getId().equals(id)) {
					result = item;
					break;
				}
			}
		}		
		return result;	
	}

	private Polyline getPolyline(String id) {
		Polyline result = null;
		if (polylines != null) {
			for (Polyline item : polylines) {
				if (item.getId().equals(id)) {
					result = item;
					break;
				}
			}
		}		
		return result;	
	}
	
	private InfoWindow getInfoWindow(String id) {
		InfoWindow result = null;
		if (infoWindows != null) {
			for (InfoWindow item : infoWindows) {
				if (item.getId().equals(id)) {
					result = item;
					break;
				}
			}
		}		
		return result;	
	}
	
	private String getJavascriptArray(List<SphericalTimePoint> list) {
		String arr  = "";
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if (list != null && list.size() != 0) {
			for (SphericalTimePoint p : list) {
				sb.append("[" + p.lat + "," + p.lon + "],");
			}
			arr = sb.substring(0, sb.length()-1);
		}
		arr += "]";
		return arr;
	}
	
	private String getObjectUniqueId() {
		return String.valueOf(objectUniqueId++);
	}
	
	private void cleanMapableArray(List<? extends Mapable> list) {
		for (Mapable m : new ArrayList<Mapable> (list) ) {
			remove(m);
		}
	}
	
	@Override
	public void clean() {
		cleanMapableArray(markers);
		cleanMapableArray(polygons);
		cleanMapableArray(polylines);
		cleanMapableArray(infoWindows);
	}
	
	public native void updateInfoWindow(String mapId, String id, String content, String markerId, double lat, double lng, int maxWidth)/*-{
		$wnd.googleMap_updateInfoWindow(mapId, id, content, markerId, lat, lng, maxWidth);
	}-*/;	
	
	public native void addInfoWindow(String mapId, String id, String content, String markerId, double lat, double lng, int maxWidth)/*-{
		$wnd.googleMap_addInfoWindow(mapId, id, content, markerId, lat, lng, maxWidth);
	}-*/;	
	
	public native void removeInfoWindow(String mapId, String id)/*-{
	//alert(mapId + " " + id + " " + content + " " + markerId + " " + lat + " " + lng + " " + maxWidth);
		$wnd.googleMap_removeInfoWindow(mapId, id);
	}-*/;	
	
	public native void updatePolygon(String mapId, String id, String vertices, double opacity, int width, String color, String fillColor, double fillOpacity)/*-{
		$wnd.googleMap_updatePolygon(mapId, id, vertices, opacity, width, color, fillColor, fillOpacity, false);
	}-*/;	
	
	public native void addPolygon(String mapId, String id, String vertices, double opacity, int width, String color, String fillColor, double fillOpacity)/*-{
		$wnd.googleMap_addPolygon(mapId, id, vertices, opacity, width, color, fillColor, fillOpacity, false);
	}-*/;	
	
	public native void removePolygon(String mapId, String id)/*-{
		$wnd.googleMap_removePolygon(mapId, id);
	}-*/;	
	
	public native void updatePolyline(String mapId, String id, String vertices, double opacity, float width, String color)/*-{
		$wnd.googleMap_updatePolyline(mapId, id, vertices, opacity, width, color, false);
	}-*/;	
	
	public native void addPolyline(String mapId, String id, String vertices, double opacity, float width, String color)/*-{
		$wnd.googleMap_addPolyline(mapId, id, vertices, opacity, width, color, false);
	}-*/;	
	
	public native void removePolyline(String mapId, String id)/*-{
		$wnd.googleMap_removePolyline(mapId, id);
	}-*/;	

	public native void updateMarker(String mapId, String id, String title, double lat, double lng, String icon, boolean editable, Integer dx, Integer dy)/*-{
	$wnd.googleMap_updateMarker(mapId, id, title, lat, lng, icon, editable, dx, dy);
}-*/;	
	

	public native void addMarker(String mapId, String id, String title, double lat, double lng, String icon, boolean editable, Integer dx, Integer dy)/*-{
	$wnd.googleMap_addMarker(mapId, id, title, lat, lng, icon, editable, dx, dy);
	}-*/;	
	
	public native void addMarkerEventHandler(String mapId, String id)/*-{
		$wnd.googleMap_registerMarkerClickHandler(mapId, id);
	}-*/;	
	
	public native void removeMarkerEventHandler(String mapId, String id)/*-{
		$wnd.googleMap_unregisterMarkerClickHandler(mapId, id);
	}-*/;	

	public native void removeMarker(String mapId, String id)/*-{
		$wnd.googleMap_removeMarker(mapId, id);
	}-*/;	

	private void clickCallback(String eventSource) {
		Marker m = getMarker(eventSource);
		if (m != null && m.getEventHandler() != null) {
			m.getEventHandler().run();
		}
	}
	
	private native void publishClickCallback(String mapId, int id) /*-{
	    var _this = this;
	    $wnd.googleMap_instances[id][4] = function (eventSource) {	_this.@ge.amigo.neuro.console.client.ui.map.GoogleMapWidget::clickCallback(Ljava/lang/String;)(eventSource); }
	}-*/;

	private native void publishMainFunction(int id, Runnable runnable) /*-{
	    $wnd.googleMap_instances[id][3] = function () {	runnable.@java.lang.Runnable::run()(); }
	}-*/;
	
	private native void registerMapInJs(String mapId, int id) /*-{
		$wnd.googleMap_registerMap(mapId, id);
	}-*/; 
	
	public native void setZoom(String mapId, int z)/*-{
		$wnd.googleMap_setZoom(mapId, z);
	}-*/;
	
	public native void fitAll(String mapId)/*-{
		$wnd.googleMap_fitAll(mapId);
	}-*/;

	public native void setCenter(String mapId, double lat, double lng)/*-{
		$wnd.googleMap_setCenter(mapId, lat, lng);
	}-*/;
	
	public native void drawMap(String mapId, int mapIndex, double lat, double lng, int zoom, String callback)/*-{
		$wnd.googleMap_draw(mapId, mapIndex, lat, lng, zoom, callback);
	}-*/;

	public native void drawMap(String mapId, int mapIndex, double lat, double lng, int zoom)/*-{
		$wnd.googleMap_draw(mapId, mapIndex, lat, lng, zoom);
	}-*/;

	public native void setEditMode(String mapId, String mode)/*-{
		$wnd.googleMap_setEditMode(mapId, mode);
	}-*/;

	public native String getMarkers(String mapId)/*-{
		return $wnd.googleMap_getMarkers(mapId);
	}-*/;

	@Override
	public void setEditMode(String mode) {
		setEditMode(getId(), mode);		
	}

	@Override
	public List<Marker> getMarkers() {
		List<Marker> markers = new ArrayList<Marker> ();
		String str = getMarkers(getId());
		if (str != null) {
			String [] marks = str.split(":");
			for (String mark : marks) {
				if (!mark.equals("")) {
					String [] o = mark.split(";");
					Marker m = new Marker();
					m.setId(o[0]);
					m.setTitle(o[1]);
					m.setPosition(new SphericalTimePoint(Double.parseDouble(o[2]), Double.parseDouble(o[3])));
					markers.add(m);
				}
			}
		}
		return markers;
	}
}
