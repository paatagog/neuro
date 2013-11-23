package ge.amigo.neuro.console.client.ui.neuromap;

import ge.amigo.neuro.console.client.math.calculation.VectorUtils;
import ge.amigo.neuro.console.client.math.neuro.DistanceNetwork;
import ge.amigo.neuro.console.client.math.neuro.DistanceNeuron;
import ge.amigo.neuro.console.client.ui.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;

public class RbfNeuroPanel extends LayoutContainer {
	
	private DistanceNetwork network;

	private NumberField neuronCountField = new NumberField();	    

	private NumberField inputCountField = new NumberField();
	
	private NumberField neuronVisibilityField = new NumberField();
	
	private TextArea inputDataArea = new TextArea();

	private NumberField neuronNumberField = new NumberField();

	private TextArea outputDataArea = new TextArea();
	
	private Button fullLogButton;
	
	private Button createButton;

	public RbfNeuroPanel() {
		
		setStyleAttribute("padding-right", "5px");
		
		// ნეირონული ქსელის კონფიგურაცია
		LayoutContainer networkConfig = new LayoutContainer();
		networkConfig.setLayout(new RowLayout(Orientation.HORIZONTAL));
		networkConfig.setWidth("100%");
		networkConfig.setHeight(40);
		
		LayoutContainer lc = new LayoutContainer();
		Label neuronCountLabel = new Label(UIUtils.getMessage("tab_neuromap_neuroPanel_neuronCount"));
		neuronCountField.setWidth(100);
		neuronCountField.setPropertyEditorType(Integer.class);
		neuronCountField.setValue(3);
		lc.add(neuronCountLabel);
		lc.add(neuronCountField);
		networkConfig.add(lc, new RowData(-1, -1, new Margins(0, 20, 0, 0)));

		lc = new LayoutContainer();
		Label inputCountLabel = new Label(UIUtils.getMessage("tab_neuromap_neuroPanel_inputCount"));
		inputCountField.setWidth(100);
		inputCountField.setPropertyEditorType(Integer.class);
		inputCountField.setValue(3);
		lc.add(inputCountLabel);
		lc.add(inputCountField);
		networkConfig.add(lc, new RowData(-1, -1, new Margins(0, 20, 0, 0)));
		
		lc = new LayoutContainer();
		Label neuronVisibilityLabel = new Label(UIUtils.getMessage("tab_neuromap_neuroPanel_neuronVisibility"));
		neuronVisibilityField.setWidth(100);
		neuronVisibilityField.setPropertyEditorType(Double.class);
		neuronVisibilityField.setValue(Math.sqrt(27));
		lc.add(neuronVisibilityLabel);
		lc.add(neuronVisibilityField);
		networkConfig.add(lc, new RowData(-1, -1, new Margins(0, 20, 0, 0)));

		createButton = new Button(UIUtils.getMessage("tab_neuromap_neuroPanel_createNetworkButton"));
		createButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				StringBuilder sb = new StringBuilder();
				int m = (neuronCountField.getValue() != null && (neuronCountField.getValue().intValue()) != 0 ) ? neuronCountField.getValue().intValue() : 3; 
				//int n = (inputCountField.getValue() != null && (inputCountField.getValue().intValue()) != 0 ) ? inputCountField.getValue().intValue() : 3; 
				double visibility = neuronVisibilityField.getValue() != null ? neuronVisibilityField.getValue().doubleValue() : 0;
				network = new DistanceNetwork();
				List<DistanceNeuron> neuronLayer = new ArrayList<DistanceNeuron>();
				for (int i = 0; i < m; i++) {
					DistanceNeuron neuron = new DistanceNeuron();
					neuronLayer.add(neuron);
				}
				network.setDistanceLayer(neuronLayer);
				network.setNeuronVisibility(visibility);
				sb.append(network.toString());
				outputDataArea.setValue(sb.toString());
			}
		});
		
		networkConfig.add(createButton, new RowData(-1, -1, new Margins(15, 20, 0, 0)));
		
		// ნეირონული ქსელის კონტროლი
		LayoutContainer networkControl = new LayoutContainer();
		networkControl.setLayout(new RowLayout(Orientation.HORIZONTAL));
		networkControl.setWidth("100%");
		networkControl.setHeight(170);
		networkControl.setStyleAttribute("padding-top", "10px");
		
		lc = new LayoutContainer();
		Label inputDataLabel = new Label(UIUtils.getMessage("tab_neuromap_neuroPanel_inputData"));
		inputDataArea.setWidth(350);
		inputDataArea.setHeight(150);
		inputDataArea.setValue("1, 2, 3");
		lc.add(inputDataLabel);
		lc.add(inputDataArea);
		networkControl.add(lc, new RowData(-1, -1, new Margins(0, 20, 0, 0)));

		Button decisionButton = new Button(UIUtils.getMessage("tab_neuromap_neuroPanel_networkDescsion"));
		decisionButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				List<Double> sample = VectorUtils.getVector(inputDataArea.getValue(), ",");
				StringBuilder sb = new StringBuilder();
				if (network != null) {
					network.applyInputs(sample);
					sb.append(network.getOutNumber());
				} else {
					sb.append("network does not exist!");
				}
				outputDataArea.setValue(sb.toString());
			}
		});
		networkControl.add(decisionButton, new RowData(-1, -1, new Margins(15, 20, 0, 0)));
		
		Button learnButton = new Button(UIUtils.getMessage("tab_neuromap_neuroPanel_learn"));
		learnButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				List<Double> sample = VectorUtils.getVector(inputDataArea.getValue(), ",");
				if (network != null) {
					network.learn(sample, false);
					fullLogButton.fireEvent(Events.Select);
				} else {
					outputDataArea.setValue("network does not exist!");
				}
			}
		});
		networkControl.add(learnButton, new RowData(-1, -1, new Margins(15, 20, 0, 0)));

		// ნეირონული ქსელის ლოგირება
		LayoutContainer networkLog = new LayoutContainer();
		networkLog.setLayout(new RowLayout(Orientation.HORIZONTAL));
		networkLog.setWidth("100%");
		networkLog.setHeight(50);
		networkLog.setStyleAttribute("padding-top", "10px");
		
		lc = new LayoutContainer();
		Label neuronNumberLabel = new Label(UIUtils.getMessage("tab_neuromap_neuroPanel_neuronNumber"));
		neuronNumberField.setWidth(150);
		lc.add(neuronNumberLabel);
		lc.add(neuronNumberField);
		networkLog.add(lc, new RowData(-1, -1, new Margins(0, 20, 0, 0)));

		Button weightLog = new Button(UIUtils.getMessage("tab_neuromap_neuroPanel_weightLog"));
		weightLog.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				StringBuilder sb = new StringBuilder();
				sb.append("Not yet implemented!");
				outputDataArea.setValue(sb.toString());
			}
		});
		networkLog.add(weightLog, new RowData(-1, -1, new Margins(15, 20, 0, 0)));
		
		fullLogButton = new Button(UIUtils.getMessage("tab_neuromap_neuroPanel_fullLog"));
		fullLogButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				StringBuilder sb = new StringBuilder();
				if (network == null) {
					sb.append("network does not exist!");
				} else {
					int neuronNumber = (neuronNumberField.getValue() != null) ? neuronNumberField.getValue().intValue() : -1; 
					if (neuronNumber == -1) {
						sb.append(network.toString());
					} else {
						if (network.getDistanceLayer() != null && network.getDistanceLayer().size() > neuronNumber) {
							sb.append("neuron " + neuronNumber + ": \n");
							sb.append(network.getDistanceLayer().get(neuronNumber).toString());
						} else {
							sb.append("Neuron number " + neuronNumber + " does not exist!");
						}
					}
				}
				outputDataArea.setValue(sb.toString());
			}
		});
		networkLog.add(fullLogButton, new RowData(-1, -1, new Margins(15, 20, 0, 0)));
		
		outputDataArea.setHeight(280);
		outputDataArea.setWidth("100%");
		
		add(networkConfig);
		add(networkControl);
		add(networkLog);
		add(outputDataArea);
		
		createButton.fireEvent(Events.Select);
	}	

}
