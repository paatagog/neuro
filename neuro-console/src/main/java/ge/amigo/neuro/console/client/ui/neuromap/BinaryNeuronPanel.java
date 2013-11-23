package ge.amigo.neuro.console.client.ui.neuromap;

import ge.amigo.neuro.console.client.math.calculation.VectorUtils;
import ge.amigo.neuro.console.client.math.neuro.BinaryNeuron;
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
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;

public class BinaryNeuronPanel extends LayoutContainer {
	
	/**
	 * ორობითი ნეირონი
	 */
	private BinaryNeuron neuron;
	
	/**
	 * ნეირონის შესასვლელების ოდენობა
	 */
	private NumberField inputCountField = new NumberField();
	
	/**
	 * ნეირონის შექმნის ღილაკი
	 */
	private Button createButton;
	
	/**
	 * ნეირონის წონები
	 */
	private TextField<String> weightsField = new TextField<String>();

	/**
	 * ნეირონის წონების დაყენების ღილაკი
	 */
	private Button assignWeightsButton;

	/**
	 * არხების შეცდომის ალბათობები
	 */
	private TextField<String> probField = new TextField<String>();
	
	/**
	 * არხების შეცდომის ალბათობების მინიჭების ღილაკი
	 */
	private Button channelErrorButton;

	/**
	 * ნეირონის ჩვენების ღილაკი
	 */
	private Button neuronLogButton;

	/**
	 * ნეირონის ოპტიმალური წონების დადგენის ღილაკი
	 */
	private Button neuroOptimalWeightsButton;
	
	/**
	 * ნეირონის შეცდომის ალბათობის გამოთვლის ღილაკი
	 */
	private Button calculateProbOfErrorButton;
	
	/**
	 * ექვივალენტობის არის დადგენის ღილაკი
	 */
	private Button calculateRadiosOfEquivalencyButton;
	
	/**
	 * შედეგების საჩვენებელი ველი
	 */
	private TextArea outputDataArea = new TextArea();
	
	public BinaryNeuronPanel () {
		
		setStyleAttribute("padding-right", "5px");

		// ნეირონული ქსელის კონფიგურაცია
		LayoutContainer neuronConfig = new LayoutContainer();
		neuronConfig.setLayout(new RowLayout(Orientation.HORIZONTAL));
		neuronConfig.setWidth("100%");
		neuronConfig.setHeight(40);

		LayoutContainer lc = new LayoutContainer();
		Label inputCountLabel = new Label(UIUtils.getMessage("tab_binaryNeuron_neuroPanel_inputCount"));
		inputCountField.setWidth(100);
		inputCountField.setPropertyEditorType(Integer.class);
		inputCountField.setValue(3);
		lc.add(inputCountLabel);
		lc.add(inputCountField);
		neuronConfig.add(lc, new RowData(-1, -1, new Margins(0, 20, 0, 0)));
		
		createButton = new Button(UIUtils.getMessage("tab_binaryNeuron_neuroPanel_createNeuronButton"));
		createButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				StringBuilder sb = new StringBuilder();
				int n = (inputCountField.getValue() != null && (inputCountField.getValue().intValue()) != 0 ) ? inputCountField.getValue().intValue() : 3; 
				List<Integer> inputs = new ArrayList <Integer> ();
				for (int i = 0; i < n; i++) {
					inputs.add(0);
				}
				neuron = new BinaryNeuron();
				neuron.setInputs(inputs);
				sb.append(neuron.toString());
				outputDataArea.setValue(sb.toString());
			}
		});
		
		neuronConfig.add(createButton, new RowData(-1, -1, new Margins(15, 20, 0, 0)));
		
		// წონების მინიჭება
		LayoutContainer weightsConfig = new LayoutContainer();
		weightsConfig.setLayout(new RowLayout(Orientation.HORIZONTAL));
		weightsConfig.setWidth("100%");
		weightsConfig.setHeight(40);
		weightsConfig.setStyleAttribute("padding-top", "10px");

		lc = new LayoutContainer();
		Label weightsLabel = new Label(UIUtils.getMessage("tab_binaryNeuron_neuroPanel_weights"));
		weightsLabel.setWidth(100);
		weightsField.setValue("5, 6.8, 7.2");
		lc.add(weightsLabel);
		lc.add(weightsField);
		weightsConfig.add(lc, new RowData(-1, -1, new Margins(0, 20, 0, 0)));
		
		assignWeightsButton = new Button(UIUtils.getMessage("tab_binaryNeuron_neuroPanel_weightsButton"));
		assignWeightsButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				StringBuilder sb = new StringBuilder();
				List<Double> weights = VectorUtils.getVector(weightsField.getValue(), ",");
				neuron.setWeights(weights);
				sb.append(neuron.toString());
				outputDataArea.setValue(sb.toString());
			}
		});
		
		weightsConfig.add(assignWeightsButton, new RowData(-1, -1, new Margins(15, 20, 0, 0)));
		

		// შეცდომის ალბათობების მინიჭება
		LayoutContainer channelErrorConfig = new LayoutContainer();
		channelErrorConfig.setLayout(new RowLayout(Orientation.HORIZONTAL));
		channelErrorConfig.setWidth("100%");
		channelErrorConfig.setHeight(40);
		channelErrorConfig.setStyleAttribute("padding-top", "10px");

		lc = new LayoutContainer();
		Label errorsLabel = new Label(UIUtils.getMessage("tab_binaryNeuron_neuroPanel_channelError"));
		errorsLabel.setWidth(100);
		probField.setValue("0.1, 0.2, 0.7");
		lc.add(errorsLabel);
		lc.add(probField);
		channelErrorConfig.add(lc, new RowData(-1, -1, new Margins(0, 20, 0, 0)));
		
		channelErrorButton = new Button(UIUtils.getMessage("tab_binaryNeuron_neuroPanel_channelErrorButton"));
		channelErrorButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				StringBuilder sb = new StringBuilder();
				List<Double> errors = VectorUtils.getVector(probField.getValue(), ",");
				neuron.setChannelErrors(errors);
				sb.append(neuron.toString());
				outputDataArea.setValue(sb.toString());
			}
		});
		
		channelErrorConfig.add(channelErrorButton, new RowData(-1, -1, new Margins(15, 20, 0, 0)));
		
		// ნეირონის კონტროლი
		LayoutContainer neuronControlConfig = new LayoutContainer();
		neuronControlConfig.setLayout(new RowLayout(Orientation.HORIZONTAL));
		neuronControlConfig.setWidth("100%");
		neuronControlConfig.setHeight(40);
		neuronControlConfig.setStyleAttribute("padding-top", "10px");

		lc = new LayoutContainer();
		
		neuronLogButton = new Button(UIUtils.getMessage("tab_binaryNeuron_neuroPanel_neuronLogButton"));
		neuronLogButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				StringBuilder sb = new StringBuilder();
				sb.append(neuron.toString());
				outputDataArea.setValue(sb.toString());
			}
		});
		
		neuronControlConfig.add(neuronLogButton, new RowData(-1, -1, new Margins(15, 20, 0, 0)));
		
		// შეცომის ალბათობების გამოთვლა
		calculateProbOfErrorButton = new Button(UIUtils.getMessage("tab_binaryNeuron_neuroPanel_neuronErrorButton"));
		calculateProbOfErrorButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				StringBuilder sb = new StringBuilder();
				sb.append("probability of error: ").append(neuron.getProbabilityOfError());
				outputDataArea.setValue(sb.toString());
			}
		});
		
		neuronControlConfig.add(calculateProbOfErrorButton, new RowData(-1, -1, new Margins(15, 20, 0, 0)));
		
		// ოპტიმალური წონების დადგენა
		neuroOptimalWeightsButton = new Button(UIUtils.getMessage("tab_binaryNeuron_neuroPanel_neuronOptimalWeightsButton"));
		neuroOptimalWeightsButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				StringBuilder sb = new StringBuilder();
				sb.append("optimal weights: ");
				List <Double> optimalWeights = new ArrayList <Double> ();
				int k = 1;
				for (int i = 0; i < neuron.getChannelErrors().size(); i++) {
					optimalWeights.add(k * Math.log((1-neuron.getChannelErrors().get(i)) / neuron.getChannelErrors().get(i)));
				}
				
				for (int i = 0; i < neuron.getChannelErrors().size(); i++) {
					sb.append(optimalWeights.get(i) + " ");
				}
				outputDataArea.setValue(sb.toString());
			}
		});
		
		neuronControlConfig.add(neuroOptimalWeightsButton, new RowData(-1, -1, new Margins(15, 20, 0, 0)));
		
		// ექვივალენტობის რადიუსის დადგენა
		calculateRadiosOfEquivalencyButton = new Button(UIUtils.getMessage("tab_binaryNeuron_neuroPanel_neuronEquivalentRadiusButton"));
		calculateRadiosOfEquivalencyButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				StringBuilder sb = new StringBuilder();
				sb.append("equivalency radius: " + neuron.getEquivalencyRadius());
				outputDataArea.setValue(sb.toString());
			}
		});
		
		neuronControlConfig.add(calculateRadiosOfEquivalencyButton, new RowData(-1, -1, new Margins(15, 20, 0, 0)));
		
		outputDataArea.setHeight(280);
		outputDataArea.setWidth("100%");

		add(neuronConfig);
		add(weightsConfig);
		add(channelErrorConfig);
		add(neuronControlConfig);
		add(outputDataArea);
		
		createButton.fireEvent(Events.Select);
		
	}

	
}
