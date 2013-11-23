var googleMap_scriptIsLoaded = false;
var googleMap_scriptIsLoading = false;
var googleMap_option_center_lat = 41.397;
var googleMap_option_center_lng = 41.644;
var googleMap_option_zoom = 8;
var googleMap_instances = new Array();

var googleMap_mapIdPosition = 0;
var googleMap_mapPosition = 1;
var googleMap_mapScriptLoadPosition = 3;
var googleMap_markerClickHandlerPosition = 4;
var googleMap_mapNumberPosition = 5;
var googleMap_mapClickListenerPosition = 6;
var googleMap_markerPosition = 10;
var googleMap_polygonPosition = 11;
var googleMap_polylinePosition = 12;
var googleMap_infoWindowPosition = 13;
var googleMap_latPosition = 20;
var googleMap_lngPosition = 21;
var googleMap_zoomPosition = 22;
var googleMap_editModePosition = 30;
var googleMap_editedPolylinePosition = 31;



/**
 * ახალი რუკის რეგისტრაცია. რუკა პირველ რიგში უნდა დავარეგისტრიროთ და მერე დავხატოთ.
 * @param mapId რუკის იდენტიფიკატორი
 * @param index რუკის ინდექსი
 */
function googleMap_registerMap(mapId, index) {
	googleMap_instances[index] = new Array();
	var conf = googleMap_instances[index];
	conf[googleMap_mapIdPosition] = mapId;
	conf[googleMap_mapNumberPosition] = index;
	conf[googleMap_markerPosition] = new Array();
	conf[googleMap_polygonPosition] = new Array();
	conf[googleMap_polylinePosition] = new Array();
	conf[googleMap_infoWindowPosition] = new Array();
	conf[googleMap_mapClickListenerPosition] = function (event) {
		if (conf[googleMap_editModePosition] == "marker") {
			var markerId = Math.round(Math.random() * 100000);
			googleMap_addMarker(mapId, markerId, "", event.latLng.lat(), event.latLng.lng(), null, true);
			conf[googleMap_editModePosition] = null;
		}
	};
}

/**
 * რუკის დახატვა. რუკის დახატვის წინ მოწმდება, თუ არაა ჯერ ჩამოტვირთული გუგლის აპი, მაშინ ხდება მისი ჩატვირთვა და მერე რუკის დახატვა.
 * @param mapId რუკის უნიკალური იდენტიფიკატორი
 * @param index რუკის ინდექსი
 * @param lat რუკის ცენტრის გრძედი
 * @param lng რუკის ცენტრის განედი
 * @param zoom გადიდება. 0-დან 17-ის ჩათვლით. 17 არის ყველაზე დიდი ზომის რუკა.
 */
function googleMap_draw(mapId, index, lat, lng, zoom) {
	googleMap_instances[index][googleMap_latPosition] = (lat == null) ? googleMap_option_center_lat : lat;
	googleMap_instances[index][googleMap_lngPosition] = (lng == null) ? googleMap_option_center_lng : lng;
	googleMap_instances[index][googleMap_zoomPosition] = (zoom == null) ? googleMap_option_zoom : zoom;
	if (googleMap_scriptIsLoading) {
//		googleMap_monitorScriptLoad(mapId, index);
	} else if (googleMap_scriptIsLoaded) {
		googleMap_drawMap(mapId, index);
	} else {
		googleMap_LoadScript();
	}
}

function googleMap_monitorScriptLoad(mapId, index) {
	if (googleMap_scriptIsLoaded) {
		googleMap_drawMap(mapId, index);
	} else {
		setTimeout("googleMap_monitorScriptLoad(\"" + mapId + "\", " + index +  ")", 1000);
	}
}

function googleMap_LoadScript() {
	googleMap_scriptIsLoading = true;
	var head = document.getElementsByTagName("head")[0];
	var script = document.createElement("script");
	script.type = "text/javascript";
	script.src = "http://maps.googleapis.com/maps/api/js?sensor=true&callback=googleMap_ScriptLoadCallback";
	head.appendChild(script);	
}

function googleMap_ScriptLoadCallback() {
	googleMap_scriptIsLoaded = true;
	googleMap_scriptIsLoading = false;
	var i;
	for (i = 0; i < googleMap_instances.length; i++) {
		if (googleMap_instances[i] != undefined && googleMap_instances[i] != null) {
			googleMap_drawMap(googleMap_instances[i][0], i);
		}
	}
}

function googleMap_drawMap(mapId, id) {
	setTimeout("googleMap_drawMapDelayed(\"" + mapId + "\", " + id + ")", 100);
}

function googleMap_drawMapDelayed(mapId, id) {
	var mapOptions = {
		center: new google.maps.LatLng(googleMap_instances[id][googleMap_latPosition], googleMap_instances[id][googleMap_lngPosition]),
		zoom: googleMap_instances[id][googleMap_zoomPosition],
		mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	var container = document.getElementById(mapId);
	if (container != null) {
		var map = new google.maps.Map(container, mapOptions);
		googleMap_instances[id][googleMap_mapPosition] = map;
		google.maps.event.addListener(map, 'click', googleMap_instances[id][googleMap_mapClickListenerPosition]);
		setTimeout("googleMap_instances[" + id + "][" + googleMap_mapScriptLoadPosition + "]()", 50);
	}
}

function googleMap_markerToString(marker) {
	var s = "";
	s += marker[0] + ";";
	s += marker[1].getTitle() + ";";
	s += marker[1].getPosition().lat() + ";";
	s += marker[1].getPosition().lng() + "";
	return s;
}

function googleMap_polylineToString(polyline) {
	var s = "";
	s += polyline[0] + " ";
	s += polyline[1].strokeColor + " ";
	s += polyline[1].strokeWeight + " ";
	s += polyline[1].strokeOpacity + " ";
	// s += polyline[1].getPath() + " ";
	return s;
}

function googleMap_dumpCache() {
	var i;
	var s = "";
	for (i=0; i<googleMap_instances.length; i++) {
		if (googleMap_instances[i] != null && googleMap_instances[i] != undefined) {
			s += "mapId=" + googleMap_instances[i][googleMap_mapIdPosition] + "; \n";
			var markers = googleMap_instances[i][googleMap_markerPosition];
			var j;
			s += "markers(";
			for (j=0; j < markers.length; j++) {
				s += "(" + googleMap_markerToString(markers[j]) + ")";
			}
			s += ")\n";
			s += "polylines(";
			var polylines = googleMap_instances[i][googleMap_polylinePosition];
			for (j=0; j < polylines.length; j++) {
				s += "(" + googleMap_polylineToString(polylines[j]) + ")";
			}
			s += ")";
			s += "\n";
		}
	}
	alert(s);
}

function googleMap_getMapConfig(mapId) {
	var inst = null;
	var i;
	for (i=0; i < googleMap_instances.length; i++) {
		if (googleMap_instances[i] != null && googleMap_instances[i] != undefined && googleMap_instances[i][googleMap_mapIdPosition] == mapId) {
			inst = googleMap_instances[i];
			break;
		}
	}
	return inst;
}

function googleMap_getRegisteredMap(mapId) {
	var map = null;
	var config = googleMap_getMapConfig(mapId);
	if (config != null) {
		map = config[googleMap_mapPosition];
	}
	return map;
}

function googleMap_getConfigFromArray(arr, id) {
	var o = null;
	if (arr != null) {
		var i;
		for (i=0; i < arr.length; i++) {
			if (arr[i][0] == id) {
				o = arr[i];
				break;
			}
		}
	}
	return o;
}

function googleMap_getRegisteredObjectConfig(mapId, oId) {
	var o = null;
	var c = googleMap_getMapConfig(mapId);
	if (c != null) {
		o = googleMap_getConfigFromArray(c[googleMap_markerPosition], oId);
		if (o == null) {
			o = googleMap_getConfigFromArray(c[googleMap_polygonPosition], oId);
		}
		if (o == null) {
			o = googleMap_getConfigFromArray(c[googleMap_polylinePosition], oId);
		}
		if (o == null) {
			o = googleMap_getConfigFromArray(c[googleMap_infoWindowPosition], oId);
		}
	}
	return o;
}

function googleMap_getObjectFromArray(arr, id) {
	var o = null;
	var c = googleMap_getConfigFromArray(arr, id);
	if (c != null) {
		o = c[1];
	}
	return o;
}

function googleMap_getRegisteredObject(mapId, oId) {
	var o = null;
	var c = googleMap_getMapConfig(mapId);
	if (c != null) {
		o = googleMap_getObjectFromArray(c[googleMap_markerPosition], oId);
		if (o == null) {
			o = googleMap_getObjectFromArray(c[googleMap_polygonPosition], oId);
		}
		if (o == null) {
			o = googleMap_getObjectFromArray(c[googleMap_polylinePosition], oId);
		}
		if (o == null) {
			o = googleMap_getObjectFromArray(c[googleMap_infoWindowPosition], oId);
		}
	}
	return o;
}

/**
 * რუკის ცენტრის დაყენებვა
 * @param mapId რუკის უნიკალური იდენტიფიკატორი
 * @param lat რუკის ცენტრის გრძედი
 * @param lng რუკის ცენტრის განედი
 */
function googleMap_setCenter(mapId, lat, lng) {
	googleMap_option_center_lat = lat;
	googleMap_option_center_lng = lng;
	var inst = googleMap_getRegisteredMap(mapId);
	if (inst != null) {
		inst.setCenter(new google.maps.LatLng(lat, lng));
	}
}

/**
 * რუკის გადიდების დაყენება
 * @param mapId რუკის უნიკალური იდენტიფიკატორი
 * @param zoom გადიდება. 0-დან 17-ის ჩათვლით. 17 არის ყველაზე დიდი ზომის რუკა.
 */
function googleMap_setZoom(mapId, zoom) {
	googleMap_option_zoom = zoom;
	var inst = googleMap_getRegisteredMap(mapId);
	if (inst != null) {
		inst.setZoom(zoom);
	}
}

function googleMap_fitAll(mapId) {
	var mapConfig = googleMap_getMapConfig(mapId);
	
	var bounds = new google.maps.LatLngBounds();
	var polylines = mapConfig[googleMap_polylinePosition];
	for (i = 0; i < polylines.length; i++) {
		var polylinePoints = polylines[i][1].getPath().getArray();
		for (j = 0; j < polylinePoints.length; j++) {
			bounds.extend(polylinePoints[j]);
		}
	}
	
	var markers = mapConfig[googleMap_markerPosition];
	for (i = 0; i < markers.length; i++) {
		bounds.extend(markers[i][1].position);
	}
	
	mapConfig[googleMap_mapPosition].fitBounds(bounds);
}


/**
 * რუკაზე არსებული მარკერისთვის მაუსის დაჭერის ფუნქციის დანიშვნა.
 * @param mapId რუკის უნიკალური იდენტიფიკატორი
 * @param markerId მარკერის უნიკალური იდენტიფიკატორი
 */
function googleMap_registerMarkerClickHandler(mapId, markerId) {
	var mapConfig = googleMap_getMapConfig(mapId);
	if (mapConfig != null) {
		var mark = googleMap_getRegisteredObject(mapId, markerId);
		if (mark != null) {
			google.maps.event.addListener(mark, 'click', function() {
				mapConfig[googleMap_markerClickHandlerPosition](markerId);
			});
		}
	}
}

/**
 * რუკაზე არსებული მარკერისთვის მაუსის დაჭერის ფუნქციის გაუქმება.
 * @param mapId რუკის უნიკალური იდენტიფიკატორი
 * @param markerId მარკერის უნიკალური იდენტიფიკატორი
 */
function googleMap_unregisterMarkerClickHandler(mapId, markerId) {
	var mapConfig = googleMap_getMapConfig(mapId);
	if (mapConfig != null) {
		var mapInst = mapConfig[googleMap_mapPosition];
		var mark = googleMap_getRegisteredObject(mapId, markerId);
		if (mark != null) {
			google.maps.event.clearListeners(mark, 'click');
		}
	}
}

/**
 * რუკაზე არსებული ტეხილის რედაქტირების პროცესის დაწყება. რედაქტირების პროცესის დროს ტეხილი გამოკვეთილი უნდა იყოს.
 * @param mapId რუკის უნიკალური იდენტიფიკატორი
 * @param polId ტეხილის უნიკალური იდენტიფიკატორი
 * TODO
 */
function googleMap_startEditPolyline(mapId, polId){
	var mapConf = googleMap_getMapConfig(mapId);
	if (mapConf[googleMap_editedPolylinePosition] != undefined && mapConf[googleMap_editedPolylinePosition] != null) {
		// TODO unedit polyline and other objects
	}
	
	var polConf = googleMap_getRegisteredObjectConfig(mapId, polId);
	mapConf[googleMap_editedPolylinePosition] = polConf;
	var pol = polConf[1];
	polConf[2] = new Array();
	if (pol != null) {
		var vertices = pol.getPath().getArray();
		var i;
		for (i=0; i<vertices.length; i++) {
			var vertex = vertices[i];
			var point = new google.maps.LatLng(vertex.lat(), vertex.lng());
			var p = new google.maps.Point(5, 5);
			var markerImage = new google.maps.MarkerImage("images\\polylineVertex.png", null, null, p, null);
			var marker = new google.maps.Marker({
				position: point,
				icon: markerImage,
				draggable: true
			});
			marker.setMap(googleMap_getRegisteredMap(mapId));
			polConf[2].push(marker);
		}
	}
	mapConf[googleMap_editedPolylinePosition] = polConf;
}

/**
 * რუკაზე არსებული ტეხილის რედაქტირების პროცესის დასრულება.
 * @param mapId რუკის უნიკალური იდენტიფიკატორი
 * @param polId ტეხილის უნიკალური იდენტიფიკატორი
 */
function googleMap_stopEditPolyline(mapId, polId){
	var polConf = googleMap_getRegisteredObjectConfig(mapId, polId);
	var pol = polConf[1];
	var mapConf = googleMap_getMapConfig(mapId);
	if (polConf[2] != null) {
		var i;
		for (i=0; i<polConf[2].length; i++) {
			polConf[2][i].setMap(null);
		}
	}
	polConf[2] = new Array();
}

/**
 * რუკაზე არსებული მრავალკუთხედის რედაქტორება
 * @param mapId რუკის უნიკალური იდენტიფიკატორი
 * @param polId მრავალკუთხედის უნიკალური იდენტიფიკატორი
 * @param vertices მრავალკუთხედის წვეროები. მაგალითად [[44.41, 41.33], [44.42, 41.31], [44.47, 41.30], [44.41, 41.33]] ბოლო წერტილი საწყისს უნდა ემთხვეოდეს.
 * @param opacity მრავალკუთხედის გამჭვირვალობა. 0 არის სრულიად გამჭვირვალე, 100 კი გაუმჭვირვალე
 * @param width მრავალკუთხედის წირის სისქე
 * @param color მრავალკუთხედის წირის ფერი
 * @param fillColor მრავალკუთხედის არის ფერი
 * @param fillOpacity მრავალკუთხედის არის გამჭვირვალობა. 0 არის სრულიად გამჭვირვალე, 100 კი გაუმჭვირვალე
 * @param editable არის თუ არა რედაქტირებადი
 */
function googleMap_updatePolygon(mapId, polId, vertices, opacity, width, color, fillColor, fillOpacity, editable) {
	var pol = googleMap_getRegisteredObject(mapId, polId);
	if (pol != null) {
		var coordinates = new Array();
		if (vertices != null && vertices != "") {
			var vert = eval(vertices);
			var i;
			for (i=0; i < vert.length; i++) {
				coordinates.push(new google.maps.LatLng(vert[i][0], vert[i][1]));
			}		
		}
		var options = {
			paths: coordinates,
			strokeColor: color == null ? "#000000" : color,
			strokeOpacity: opacity == null ? 1 : opacity,
			strokeWeight: width,
			fillColor: fillColor == null ? "#0000FF" : fillColor,
			fillOpacity: fillOpacity == null ? 0.3 : fillOpacity,
			editable: editable == null ? false : editable
		};

		pol.setOptions(options);
	}
}

/**
 * რუკაზე მრავალკუთხედის დამატება
 * @param mapId რუკის უნიკალური იდენტიფიკატორი
 * @param polId მრავალკუთხედის უნიკალური იდენტიფიკატორი
 * @param vertices მრავალკუთხედის წვეროები. მაგალითად [[44.41, 41.33], [44.42, 41.31], [44.47, 41.30], [44.41, 41.33]] ბოლო წერტილი საწყისს უნდა ემთხვეოდეს.
 * @param opacity მრავალკუთხედის გამჭვირვალობა. 0 არის სრულიად გამჭვირვალე, 100 კი გაუმჭვირვალე
 * @param width მრავალკუთხედის წირის სისქე
 * @param color მრავალკუთხედის წირის ფერი
 * @param fillColor მრავალკუთხედის არის ფერი
 * @param fillOpacity მრავალკუთხედის არის გამჭვირვალობა. 0 არის სრულიად გამჭვირვალე, 100 კი გაუმჭვირვალე
 * @param editable არის თუ არა რედაქტირებადი
 */
function googleMap_addPolygon(mapId, polId, vertices, opacity, width, color, fillColor, fillOpacity, editable) {
	var mapConfig = googleMap_getMapConfig(mapId);
	if (mapConfig != null) {
		var coordinates = new Array();
		if (vertices != null && vertices != "") {
			var vert = eval(vertices);
			var i;
			for (i=0; i < vert.length; i++) {
				coordinates.push(new google.maps.LatLng(vert[i][0], vert[i][1]));
			}		
		}
		var options = {
			paths: coordinates,
			strokeColor: color == null ? "#000000" : color,
			strokeOpacity: opacity == null ? 1 : opacity,
			strokeWeight: width,
			fillColor: fillColor == null ? "#0000FF" : fillColor,
			fillOpacity: fillOpacity == null ? 0.3 : fillOpacity,
			editable: editable == null ? false : editable
		};

		var polygon = new google.maps.Polygon(options);

		polygon.setMap(mapConfig[googleMap_mapPosition]);
		var polArray = new Array();
		polArray.push(polId);
		polArray.push(polygon);
		mapConfig[googleMap_polygonPosition].push(polArray);
	}
}

/**
 * მრავალკუთხედის წაშლა რუკიდან
 * @param mapId რუკის უნიკალური იდენტიფიკატორი
 * @param polId მრავალკუთხედის უნიკალური იდენტიფიკატორი
 */
function googleMap_removePolygon(mapId, polId) {
	var pol = googleMap_getRegisteredObject(mapId, polId);
	pol.setMap(null);
	var mapConfig = googleMap_getMapConfig(mapId);
	if (mapConfig != null) {
		googleMap_arrayRemoveById(mapConfig[googleMap_polygonPosition], polId);
	}
}

/**
 * რუკაზე არსებული ტეხილის რედაქტირება
 * @param mapId რუკის უნიკალური იდენტიფიკატორი
 * @param polId ტეხილის უნიკალური იდენტიფიკატორი
 * @param vertices ტეხილის წვეროები. მაგალითად [[44.41, 41.33], [44.42, 41.31], [44.47, 41.30]]
 * @param opacity ტეხილის გამჭვირვალობა. 0 არის სრულიად გამჭვირვალე, 100 კი გაუმჭვირვალე
 * @param width ტეხილის მონაკვეთების სისქე
 * @param color ტეხილის ფერი
 * @param editable არის თუ არა რედაქტირებადი
 */
function googleMap_updatePolyline(mapId, polId, vertices, opacity, width, color, editable) {
	var pol = googleMap_getRegisteredObject(mapId, polId);
	if (pol != null) {
		var coordinates = new Array();
		if (vertices != null && vertices != "") {
			var vert = eval(vertices);
			var i;
			for (i=0; i < vert.length; i++) {
				coordinates.push(new google.maps.LatLng(vert[i][0], vert[i][1]));
			}		
		}
		
		var options = {
			path: coordinates,
			strokeColor: color == null ? "#000000" : color,
			strokeOpacity: opacity == null ? 1 : opacity,
			strokeWeight: width,
			editable: editable == null ? false : editable
		};

		pol.setOptions(options);
	}
}

/**
 * რუკაზე ტეხილის დამატება
 * @param mapId რუკის უნიკალური იდენტიფიკატორი
 * @param polId ტეხილის უნიკალური იდენტიფიკატორი
 * @param vertices ტეხილის წვეროები. მაგალითად [[44.41, 41.33], [44.42, 41.31], [44.47, 41.30]]
 * @param opacity ტეხილის გამჭვირვალობა. 0 არის სრულიად გამჭვირვალე, 100 კი გაუმჭვირვალე
 * @param width ტეხილის მონაკვეთების სისქე
 * @param color ტეხილის ფერი
 * @param editable არის თუ არა რედაქტირებადი
 */
function googleMap_addPolyline(mapId, polId, vertices, opacity, width, color, editable) {
	var mapConfig = googleMap_getMapConfig(mapId);
	if (mapConfig != null) {
		var coordinates = new Array();
		if (vertices != null && vertices != "") {
			var vert = eval(vertices);
			var i;
			for (i=0; i < vert.length; i++) {
				coordinates.push(new google.maps.LatLng(vert[i][0], vert[i][1]));
			}		
		}
		var options = {
			path: coordinates,
			strokeColor: color == null ? "#000000" : color,
			strokeOpacity: opacity == null ? 1 : opacity,
			strokeWeight: width,
			editable: editable == null ? false : editable
		};

		var polyline = new google.maps.Polyline(options);

		polyline.setMap(mapConfig[googleMap_mapPosition]);
		var polArray = new Array();
		polArray.push(polId);
		polArray.push(polyline);
		mapConfig[googleMap_polylinePosition].push(polArray);
	}
}


/**
 * ტეხილის წაშლა რუკიდან
 * @param mapId რუკის უნიკალური იდენტიფიკატორი
 * @param polId ტეხილის უნიკალური იდენტიფიკატორი
 */
function googleMap_removePolyline(mapId, polId) {
	var pol = googleMap_getRegisteredObject(mapId, polId);
	pol.setMap(null);
	var mapConfig = googleMap_getMapConfig(mapId);
	if (mapConfig != null) {
		googleMap_arrayRemoveById(mapConfig[googleMap_polylinePosition], polId);
	}
}

/**
 * რუკაზე არსებული მარკერის რედაქტირება
 * @param mapId რუკის უნიკალური იდენტიფიკატორი
 * @param markerId მარკერის უნიკალური იდენტიფიკატორი
 * @param title მარკერის სათაური. ტექსტი გამოჩნდება მაუსის მიყვანისას.
 * @param lat მარკერის გრძედი
 * @param lng მარკერის განედი
 * @param icon მარკერის ხატულა. <code>null</code>-ის შემთხვევაში სტანდარტული მარკერი გამოჩნდება.
 * @param editable არის თუ არა რედაქტირებადი
 * @param dx მარკერის ხატულას წაძვრა ჰორიზონტალური მიმართულებით (გადაეცით <code>null</code> თუ ავტომატურად გსურთ ამ პარამეტრის დაყენება)
 * @param dy მარკერის ხატულას წაძვრა ვერტიკალური მიმართულებით (გადაეცით <code>null</code> თუ ავტომატურად გსურთ ამ პარამეტრის დაყენება)
 */
function googleMap_updateMarker(mapId, markerId, title, lat, lng, icon, editable, dx, dy) {
	var mark = googleMap_getRegisteredObject(mapId, markerId);
	var p = null;
	if (dx != undefined && dx != null && dy != undefined && dy != null) {
		p = new google.maps.Point(dx, dy);
	}
	var image  = (icon == "" || icon == null || icon == undefined) ? "images\\marker.png" : icon;
	var markerImage = new google.maps.MarkerImage (image, null, null, p);
	if (mark != null) {
		mark.setPosition(new google.maps.LatLng(lat, lng));
		mark.setTitle(title);
		mark.setIcon(markerImage);
		mark.setDraggable(editable);
	}
}

/**
 * რუკაზე მარკერის დამატება
 * @param mapId რუკის უნიკალური იდენტიფიკატორი
 * @param markerId მარკერის უნიკალური იდენტიფიკატორი
 * @param title მარკერის სათაური. ტექსტი გამოჩნდება მაუსის მიყვანისას.
 * @param lat მარკერის გრძედი
 * @param lng მარკერის განედი
 * @param icon მარკერის ხატულა. <code>null</code>-ის შემთხვევაში სტანდარტული მარკერი გამოჩნდება.
 * @param editable არის თუ არა რედაქტირებადი
 * @param dx მარკერის ხატულას წაძვრა ჰორიზონტალური მიმართულებით (გადაეცით <code>null</code> თუ ავტომატურად გსურთ ამ პარამეტრის დაყენება)
 * @param dy მარკერის ხატულას წაძვრა ვერტიკალური მიმართულებით (გადაეცით <code>null</code> თუ ავტომატურად გსურთ ამ პარამეტრის დაყენება)
 */
function googleMap_addMarker(mapId, markerId, title, lat, lng, icon, editable, dx, dy) {
//	alert("mapId=" + mapId + " markerId=" + markerId + " lat=" + lat + " lng=" + lng + " icon=" + icon);
	var mapConfig = googleMap_getMapConfig(mapId);
	if (mapConfig != null) {
		var point = new google.maps.LatLng(lat,lng);
		var p = null;
		if (dx != undefined && dx != null && dy != undefined && dy != null) {
			p = new google.maps.Point(dx, dy);
		}
		var image  = (icon == "" || icon == null || icon == undefined) ? "images\\marker.png" : icon;
		var markerImage = new google.maps.MarkerImage (image, null, null, p, null);
		var marker = new google.maps.Marker({
			position: point,
			title: title,
			icon: markerImage,
			draggable: editable
		});
		marker.setMap(mapConfig[googleMap_mapPosition]);
		var markArray = new Array();
		markArray.push(markerId);
		markArray.push(marker);
		mapConfig[googleMap_markerPosition].push(markArray);
	}
}

/**
 * მარკერის წაშლა რუკიდან
 * @param mapId რუკის უნიკალური იდენტიფიკატორი
 * @param markerId მარკერის უნიკალური იდენტიფიკატორი
 */
function googleMap_removeMarker(mapId, markerId) {
	var mark = googleMap_getRegisteredObject(mapId, markerId);
	mark.setMap(null);
	var mapConfig = googleMap_getMapConfig(mapId);
	if (mapConfig != null) {
		googleMap_arrayRemoveById(mapConfig[googleMap_markerPosition], markerId);
	}
}

/**
 * რუკაზე საინფორმაციო დაფის რედაქტირება
 * @param mapId საინფორმაციო დაფის უნიკალური იდენტიფიკატორი
 * @param windowId საინფორმაციო დაფის უნიკალური იდენტიფიკატორი.
 * @param content საინფორმაციო დაფის ტექსტი.
 * @param markerId ამჟამად არ გამოიყენება, საჭიროა რეალიზაცია 
 * @param lat საინფორმაციო დაფის ადგილმდებარეობის გრძედი. 
 * @param lng საინფორმაციო დაფის ადგილმდებარეობის განედი.
 * @param maxWidth დაფის მაქსიმალური სიგანე. თუ ამ პარამეტრს არ გადავცემთ, სიგანე ავტომატურად განისაზღვრება შიგთავსის მიხედვით. ამჟამად არ გამოიყენება და საჭიროა მისი რეალიზაცია.
 */
function googleMap_updateInfoWindow(mapId, windowId, content, markerId, lat, lng, maxWidth) {
	var iw = googleMap_getRegisteredObject(mapId, windowId);
	if (iw != null) {
		iw.setPosition(new google.maps.LatLng(lat, lng));
		iw.setContent(content);
		//iw.setMaxWidth(maxWidth);
	}
}

/**
 * რუკაზე საინფორმაციო დაფის დამატება
 * @param mapId საინფორმაციო დაფის უნიკალური იდენტიფიკატორი
 * @param windowId საინფორმაციო დაფის უნიკალური იდენტიფიკატორი.
 * @param content საინფორმაციო დაფის ტექსტი.
 * @param markerId მარკერის უნიკალური იდენტიფიკატორი. თუ მარკერი მითითებული არაა, მაშინ საინფორმაციო ფანჯარა მითითებულ კოორდინატებზე იქნება მიბმული. 
 * როცა მარკერი მითითებულია, დაფა მარკერს დაყვება ავტომატურად.
 * @param lat საინფორმაციო დაფის ადგილმდებარეობის გრძედი
 * @param lng საინფორმაციო დაფის ადგილმდებარეობის განედი
 * @param maxWidth დაფის მაქსიმალური სიგანე. თუ ამ პარამეტრს არ გადავცემთ, სიგანე ავტომატურად განისაზღვრება შიგთავსის მიხედვით.
 */
function googleMap_addInfoWindow(mapId, windowId, content, markerId, lat, lng, maxWidth) {
	var mapConfig = googleMap_getMapConfig(mapId);
	if (mapConfig != null) {
		var options = {
			content: content,
			position: new google.maps.LatLng(lat,lng),
			maxWidth: maxWidth == null ? 150 : maxWidth
		};
		var infoWindow = new google.maps.InfoWindow(options);		

		var infoArray = new Array();
		infoArray.push(windowId);
		infoArray.push(infoWindow);
		mapConfig[googleMap_infoWindowPosition].push(infoArray);		
		
		var marker = googleMap_getRegisteredObject(mapId, markerId);
		if (marker != null) {
			infoWindow.open(mapConfig[googleMap_mapPosition], marker);
		} else {
			infoWindow.open(mapConfig[googleMap_mapPosition]);
		}
	}
}

/**
 * საინფორმაციო დაფის წაშლა რუკიდან
 * @param mapId რუკის უნიკალური იდენტიფიკატორი
 * @param markerId საინფორმაციო დაფის უნიკალური იდენტიფიკატორი
 */
function googleMap_removenfoWindow(mapId, windowId) {
	var iw = googleMap_getRegisteredObject(mapId, windowId);
	iw.colse();
	var mapConfig = googleMap_getMapConfig(mapId);
	if (mapConfig != null) {
		googleMap_arrayRemoveById(mapConfig[googleMap_infoWindowPosition], windowId);
	}
}

/**
 * რუკის რედაქტირების რეჟიმის დაყენება
 * @param mapId რუკის უნიკალური იდენტიფიკატორი
 * @param mode რედაქტირების რეჟიმი. შესაძლო მნიშვნელობებია:
 * <ul>
 * <li> "none" - არ ხდება რედაქტირება. ამ მნიშვნელობის გადაცემისას წყდება ყველანაირი რედაქტირების პროცესი. 
 * <p>აღსანიშნავია, რომ ამ რეჟიმში რედაქტირების პროცესის კვლავ წამოწყება შესაძლებელია, თუ რომელიმე ობიექტი არის რედაქტირებადი. ამ დროს რედაქტირების რეჟიმი შეიცვლება შესაბამისი ობიექტის რედაქტირების პროცესით.
 * <li>"marker" - მარკერის დამატების პროცესი. ამ დროს რუკაზე მაუსის დაჭერისას ახალი მარკერი ემატება რუკას. ამის მერე რუკის რედაქტირების რეჟიმი ავტომატურად გადადის  "none"-ზე.
 * <ul>
 */
function googleMap_setEditMode(mapId, mode) {
	var c = googleMap_getMapConfig(mapId);
	if (c != null) {
		c[googleMap_editModePosition] = mode;
		if (mode == "none" || mode == undefined || mode == null) {
//			var markers = c[googleMap_markerPosition];
//			var i;
//			for (i=0; i<markers.length; i++) {
//				markers[i][1].setDraggable(false);
//			}
		} else if (mode == "marker") {
//			googleMap_setEditMode(mapId, null);
//			c[googleMap_editModePosition] = mode;
		}		
	}	
}

/**
 * რუკაზე არსებული მარკერების სიის მიღება
 * @param mapId რუკის უნიკალური იდენტიფიკატორი
 * @return რუკაზე არსებული მარკერების სია
 */
function googleMap_getMarkers(mapId) {
	var res = null;
	var c = googleMap_getMapConfig(mapId);
	if (c != null) {
		var markers = c[googleMap_markerPosition];
		var i;
		for (i=0; i<markers.length; i++) {
			res += googleMap_markerToString(markers[i]);
			if (i != markers.length) {
				res += ":";
			}
		}	
	}	
	return res;
}

function googleMap_arrayRemoveById(arr, id) {
	if (arr != null) {
		var i;
		for (i=0; i<arr.length; i++) {
			if (arr[i][0] == id) {
				arr.splice(i, 1);
				break;
			}
		}
	}
}

