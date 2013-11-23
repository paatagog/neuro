	var loaded = false;
	var map = null;
	var polyLine;
	var tmpPolyLine;
	var markers = [];
	var vmarkers = [];
	var editMode = true;
	var vertices;
	var viewVerticesArray;
	
	var initMap = function() {
		mapHolder = "maplc";
		markers = [];
		vmarkers = [];
		
		if (editMode) {
			var mapOptions = {
				zoom: 12,
				center: new google.maps.LatLng(41.72, 44.79, true), 
				mapTypeId: google.maps.MapTypeId.ROADMAP,
				draggableCursor: 'auto',
				draggingCursor: 'move',
				disableDoubleClickZoom: true
			};
			map = new google.maps.Map(document.getElementById(mapHolder), mapOptions);
			google.maps.event.addListener(map, "click", mapLeftClick);
		} else {
			var mapOptions = {
				zoom: 12,
				center: new google.maps.LatLng(41.72, 44.79, true), 
				mapTypeId: google.maps.MapTypeId.ROADMAP,
				draggableCursor: 'auto',
				draggingCursor: 'move',
				disableDoubleClickZoom: true
			};
			map = new google.maps.Map(document.getElementById(mapHolder), mapOptions);
		}
		
		mapHolder = null;
		mapOptions = null;
		
		initPolyline();
		
		drawViewPolylines();
		editPolyline();
	};
	
	function initPolyline() {
		
		var polyOptions = {
			strokeColor: "#3355FF",
			strokeOpacity: 0.8,
			strokeWeight: 4
		};
		var tmpPolyOptions = {
			strokeColor: "#3355FF",
			strokeOpacity: 0.4,
			strokeWeight: 4
		};
		polyLine = new google.maps.Polyline(polyOptions);
		polyLine.setMap(map);
		tmpPolyLine = new google.maps.Polyline(tmpPolyOptions);
		tmpPolyLine.setMap(map);
	};
	
	function mapLeftClick(event) {
		if (event.latLng) {
			if (editMode) {
				var marker = createMarker(event.latLng);
				markers.push(marker);
				if (markers.length != 1) {
					var vmarker = createVMarker(event.latLng);
					vmarkers.push(vmarker);
					vmarker = null;
				}
			}
			var path = polyLine.getPath();
			path.push(event.latLng);
			marker = null;
		}
		event = null;
	};
	
	function addEditedVertex(latLng) {
		var ev = {latLng: latLng};
		mapLeftClick(ev);
	};

	function createMarker(point) {
		var imageNormal = new google.maps.MarkerImage(
			"customjs/square.png",
			new google.maps.Size(11, 11),
			new google.maps.Point(0, 0),
			new google.maps.Point(6, 6)
		);
		var imageHover = new google.maps.MarkerImage(
			"customjs/square_over.png",
			new google.maps.Size(11, 11),
			new google.maps.Point(0, 0),
			new google.maps.Point(6, 6)
		);
		var marker = new google.maps.Marker({
			position: point,
			map: map,
			icon: imageNormal,
			draggable: editMode
		});
		google.maps.event.addListener(marker, "mouseover", function() {
			marker.setIcon(imageHover);
		});
		google.maps.event.addListener(marker, "mouseout", function() {
			marker.setIcon(imageNormal);
		});
		if (editMode) {
			google.maps.event.addListener(marker, "drag", function() {
				for (var m = 0; m < markers.length; m++) {
					if (markers[m] == marker) {
						polyLine.getPath().setAt(m, marker.getPosition());
						moveVMarker(m);
						break;
					}
				}
				m = null;
			});
			google.maps.event.addListener(marker, "click", function() {
				for (var m = 0; m < markers.length; m++) {
					if (markers[m] == marker) {
						marker.setMap(null);
						markers.splice(m, 1);
						polyLine.getPath().removeAt(m);
						removeVMarkers(m);
						break;
					}
				}
				m = null;
			});
		}
		return marker;
	};
	
	function createVMarker(point) {
		var prevpoint = markers[markers.length-2].getPosition();
		var imageNormal = new google.maps.MarkerImage(
			"customjs/square_transparent.png",
			new google.maps.Size(11, 11),
			new google.maps.Point(0, 0),
			new google.maps.Point(6, 6)
		);
		var imageHover = new google.maps.MarkerImage(
			"customjs/square_transparent_over.png",
			new google.maps.Size(11, 11),
			new google.maps.Point(0, 0),
			new google.maps.Point(6, 6)
		);
		var marker = new google.maps.Marker({
			position: new google.maps.LatLng(
				point.lat() - (0.5 * (point.lat() - prevpoint.lat())),
				point.lng() - (0.5 * (point.lng() - prevpoint.lng()))
			),
			map: map,
			icon: imageNormal,
			draggable: editMode
		});
		google.maps.event.addListener(marker, "mouseover", function() {
			marker.setIcon(imageHover);
		});
		google.maps.event.addListener(marker, "mouseout", function() {
			marker.setIcon(imageNormal);
		});
		if (editMode) {
			google.maps.event.addListener(marker, "dragstart", function() {
				for (var m = 0; m < vmarkers.length; m++) {
					if (vmarkers[m] == marker) {
						var tmpPath = tmpPolyLine.getPath();
						tmpPath.push(markers[m].getPosition());
						tmpPath.push(vmarkers[m].getPosition());
						tmpPath.push(markers[m+1].getPosition());
						break;
					}
				}
				m = null;
			});
			google.maps.event.addListener(marker, "drag", function() {
				for (var m = 0; m < vmarkers.length; m++) {
					if (vmarkers[m] == marker) {
						tmpPolyLine.getPath().setAt(1, marker.getPosition());
						break;
					}
				}
				m = null;
			});
			google.maps.event.addListener(marker, "dragend", function() {
				for (var m = 0; m < vmarkers.length; m++) {
					if (vmarkers[m] == marker) {
						var newpos = marker.getPosition();
						var startMarkerPos = markers[m].getPosition();
						var firstVPos = new google.maps.LatLng(
							newpos.lat() - (0.5 * (newpos.lat() - startMarkerPos.lat())),
							newpos.lng() - (0.5 * (newpos.lng() - startMarkerPos.lng()))
						);
						var endMarkerPos = markers[m+1].getPosition();
						var secondVPos = new google.maps.LatLng(
							newpos.lat() - (0.5 * (newpos.lat() - endMarkerPos.lat())),
							newpos.lng() - (0.5 * (newpos.lng() - endMarkerPos.lng()))
						);
						var newVMarker = createVMarker(secondVPos);
						newVMarker.setPosition(secondVPos);//apply the correct position to the vmarker
						var newMarker = createMarker(newpos);
						markers.splice(m+1, 0, newMarker);
						polyLine.getPath().insertAt(m+1, newpos);
						marker.setPosition(firstVPos);
						vmarkers.splice(m+1, 0, newVMarker);
						tmpPolyLine.getPath().removeAt(2);
						tmpPolyLine.getPath().removeAt(1);
						tmpPolyLine.getPath().removeAt(0);
						newpos = null;
						startMarkerPos = null;
						firstVPos = null;
						endMarkerPos = null;
						secondVPos = null;
						newVMarker = null;
						newMarker = null;
						break;
					}
				}
			});
		}
		return marker;
	};
	
	function moveVMarker(index) {
		var newpos = markers[index].getPosition();
		if (index != 0) {
			var prevpos = markers[index-1].getPosition();
			vmarkers[index-1].setPosition(new google.maps.LatLng(
				newpos.lat() - (0.5 * (newpos.lat() - prevpos.lat())),
				newpos.lng() - (0.5 * (newpos.lng() - prevpos.lng()))
			));
			prevpos = null;
		}
		if (index != markers.length - 1) {
			var nextpos = markers[index+1].getPosition();
			vmarkers[index].setPosition(new google.maps.LatLng(
				newpos.lat() - (0.5 * (newpos.lat() - nextpos.lat())), 
				newpos.lng() - (0.5 * (newpos.lng() - nextpos.lng()))
			));
			nextpos = null;
		}
		newpos = null;
		index = null;
	};
	
	var removeVMarkers = function(index) {
		if (markers.length > 0) {//clicked marker has already been deleted
			if (index != markers.length) {
				vmarkers[index].setMap(null);
				vmarkers.splice(index, 1);
			} else {
				vmarkers[index-1].setMap(null);
				vmarkers.splice(index-1, 1);
			}
		}
		if (index != 0 && index != markers.length) {
			var prevpos = markers[index-1].getPosition();
			var newpos = markers[index].getPosition();
			vmarkers[index-1].setPosition(new google.maps.LatLng(
				newpos.lat() - (0.5 * (newpos.lat() - prevpos.lat())),
				newpos.lng() - (0.5 * (newpos.lng() - prevpos.lng()))
			));
			prevpos = null;
			newpos = null;
		}
		index = null;
	};
	
	function addMap(_editMode, _vertices, _viewVerticesArray) {
		editMode = _editMode;
		vertices = _vertices;
		viewVerticesArray = _viewVerticesArray;
		
		if (!googleMap_scriptIsLoaded) {
			var head = document.getElementsByTagName("head")[0];
			var script = document.createElement("script");
			script.setAttribute('src','http://maps.google.com/maps/api/js?sensor=false&callback=initMap');
			script.type = "text/javascript";
			script.onreadystatechange = function () {
				if (this.readyState == 'complete') {
					googleMap_scriptIsLoaded = true;
//					setTimeout("initMap()", 1000);
				}
			};
			script.onload = function() {
				googleMap_scriptIsLoaded = true;
//				setTimeout("initMap()", 1000);
			};
			head.appendChild(script);
		} else {
			initMap();
		}
	}
	
	function returnPolyline() {
		var value = "";
		for ( var i = 0; i < polyLine.getPath().length; i++) {
			value += polyLine.getPath().getAt(i).lat() + "," + polyLine.getPath().getAt(i).lng() + ";"; 
		}
		return value;
	}
	
	function drawViewPolylines() {
		if (viewVerticesArray != null && viewVerticesArray != "") {
			var vert = eval(viewVerticesArray);
			var x;
			for(x=0; x < vert.length; x++) {
				var viewVertices = vert[x];
				if (viewVertices != null && viewVertices != "") {
					var viewVert = eval(viewVertices);
					var y;
					var polOptions = {
							strokeColor: "#7CFC00",
							strokeOpacity: 0.8,
							strokeWeight: 4
						};
					var pol = new google.maps.Polyline(polOptions);
					pol.setMap(map);
					for (y=0; y < viewVert.length; y++) { 
						pol.getPath().push(new google.maps.LatLng(viewVert[y][0], viewVert[y][1]));
					}
				}
			}
		}
	}
	
	function editPolyline() {
		if (vertices != null && vertices != "") {
			var vert = eval(vertices);
			var i;
			for (i=0; i < vert.length; i++) {
				addEditedVertex(new google.maps.LatLng(vert[i][0], vert[i][1]));
			}
		}
	};