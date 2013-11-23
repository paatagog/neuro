package ge.amigo.neuro.console.client.ui.data;

import ge.amigo.neuro.console.client.math.Point;
import ge.amigo.neuro.console.client.math.SphericalPoint;
import ge.amigo.neuro.console.client.math.SphericalTimePoint;
import ge.amigo.neuro.console.client.math.TimePoint;
import ge.amigo.neuro.console.client.math.Utils;
import ge.amigo.neuro.console.client.math.calculation.SphericalUtils;
import ge.amigo.neuro.console.client.ui.utils.UIUtils;

import java.util.List;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;

public class ConvertPanel extends LayoutContainer {
	
	private TextArea outputTextArea = new TextArea();
	
	private TextArea dataTextArea = new TextArea();
	
	Button processButton;
	
	Button convertTimePointDataButton;
	
	public ConvertPanel() {
		
		setStyleAttribute("padding-right", "5px");

		LayoutContainer controlPanel = new LayoutContainer();
		
		controlPanel.setLayout(new BorderLayout());
		controlPanel.setStyleAttribute("padding-top", "5px");
		
		LayoutContainer filter = new LayoutContainer();
		filter.setLayout(new RowLayout(Orientation.HORIZONTAL));
		
		processButton = new Button(UIUtils.getMessage("tab_data_filter_processGoogle"));
		processButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						List<SphericalPoint> sphericalPoints = Utils.getSphericalPoints(dataTextArea.getValue(), " ");
						StringBuilder sb = new StringBuilder();
						List<Point> points = SphericalUtils.getFlatCoordinates(sphericalPoints);
						for (Point point : points) {
							sb.append(point + "; ");
						}
						outputTextArea.setValue(sb.toString());
					}
				});
		filter.add(processButton, new RowData(-1, -1, new Margins(0, 10, 0, 0)));
		
		convertTimePointDataButton = new Button(UIUtils.getMessage("tab_data_filter_processGarmin"));
		convertTimePointDataButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						List<SphericalTimePoint> sphericalTimePoints = Utils.getSphericalTimePointsFromCsv(dataTextArea.getValue());
						StringBuilder sb = new StringBuilder();
						List<TimePoint> points = SphericalUtils.getFlatTimeCoordinates(sphericalTimePoints);
						for (TimePoint point : points) {
							sb.append(point + ";\n");
						}
						outputTextArea.setValue(sb.toString());
					}
				});
		filter.add(convertTimePointDataButton, new RowData(-1, -1, new Margins(0, 10, 0, 0)));
		
		controlPanel.add(filter, new BorderLayoutData(LayoutRegion.NORTH, 25));
		controlPanel.add(outputTextArea, new BorderLayoutData(LayoutRegion.CENTER));
		

		setLayout(new BorderLayout());
		add(dataTextArea, new BorderLayoutData(LayoutRegion.NORTH));
		add(controlPanel, new BorderLayoutData(LayoutRegion.CENTER));		
	}
}
