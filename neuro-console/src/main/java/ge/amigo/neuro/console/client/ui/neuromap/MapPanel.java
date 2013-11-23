package ge.amigo.neuro.console.client.ui.neuromap;


import ge.amigo.neuro.console.client.math.SphericalTimePoint;
import ge.amigo.neuro.console.client.math.Utils;
import ge.amigo.neuro.console.client.ui.map.GoogleMapWidget;
import ge.amigo.neuro.console.client.ui.map.Map;
import ge.amigo.neuro.console.client.ui.map.Marker;
import ge.amigo.neuro.console.client.ui.map.Polyline;
import ge.amigo.neuro.console.client.ui.utils.UIUtils;

import java.util.List;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;

public class MapPanel extends LayoutContainer {

	private Map map;
	
	private Button drawButton;
	
	private Button clearButton;
	
	private TextArea mapDataTextArea = new TextArea();
	
	private TextField<String> colorField = new TextField<String>();

	private NumberField widthField = new NumberField();	    

	public static SphericalTimePoint MAP_INITIAL_CENTER = new SphericalTimePoint(41.72, 44.79);
	
	public static final int MAP_INITAL_SCALE = 40000;

	public static final int PATH_DEFAULT_WIDTH = 3;
	
	public static final String PATH_DEFAULT_COLOR = "#FF0000";

	public MapPanel() {

		setStyleAttribute("padding-left", "5px");
		setLayout(new BorderLayout());

		map = new GoogleMapWidget();
		LayoutContainer googleMapContainer = new LayoutContainer(new FitLayout());
		googleMapContainer.add((GoogleMapWidget)map);
		add(googleMapContainer, new BorderLayoutData(LayoutRegion.CENTER));
	

		LayoutContainer filter = new LayoutContainer();
		filter.setLayout(new RowLayout(Orientation.HORIZONTAL));
		filter.setStyleAttribute("padding-top", "5px");
		
		LayoutContainer parameters = new LayoutContainer();
		colorField.setWidth(50);
		colorField.setValue("FF0000");
		widthField.setPropertyEditorType(Integer.class);
		widthField.setValue(5);
		widthField.setWidth(50);
		parameters.add(widthField);
		parameters.add(colorField);
		filter.add(parameters, new RowData(-1, -1, new Margins(0, 5, 0, 0)));		
		
		mapDataTextArea.setWidth(300);
		mapDataTextArea.setHeight(50);
		mapDataTextArea.setValue("1,1,41.72127842,44.73899524,532,2012-08-29T04:22:20Z\n2,1,41.77977086,44.78198091,461,2012-08-29T04:37:36Z");
		filter.add(mapDataTextArea, new RowData(1, -1, new Margins(0, 10, 0, 0)));
		
		drawButton = new Button(UIUtils.getMessage("tab_neuromap_filter_draw"));
		drawButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						List<SphericalTimePoint> sphericalTimePoints = Utils.getSphericalTimePointsFromCsv(mapDataTextArea.getValue());
						if (sphericalTimePoints.size() > 1) {
							String color = (colorField.getValue() != null && ! (colorField.getValue()).trim().equals("") ) ? "#" + colorField.getValue() : "#FF0000"; 
							int width = (widthField.getValue() != null && (widthField.getValue().intValue()) != 0 ) ? widthField.getValue().intValue() : 5; 
							Polyline route = new Polyline(sphericalTimePoints, color, width, 60);
							map.add(route);
						} else if (sphericalTimePoints.size() == 1) {
							Marker marker = new Marker(sphericalTimePoints.get(0), "", "samsaxuri");
							map.add(marker);
						}
					}
				});
		filter.add(drawButton, new RowData(-1, -1, new Margins(0, 10, 0, 0)));
		
		clearButton = new Button(UIUtils.getMessage("tab_neuromap_filter_clear"));
		clearButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						map.clean();
					}
				});
		filter.add(clearButton, new RowData(-1, -1, new Margins(0, 10, 0, 0)));
		add(filter, new BorderLayoutData(LayoutRegion.SOUTH, 55));

		map.draw(new Runnable() {
	
			@Override
			public void run() {
				map.setCenter(MAP_INITIAL_CENTER);
				map.setZoom(MAP_INITAL_SCALE);
			}
			
		});
		
	}
}
