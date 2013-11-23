package ge.amigo.neuro.console.client.ui.neuromap;

import ge.amigo.neuro.console.client.math.function.SigmoidFunction;
import ge.amigo.neuro.console.client.math.neuro.BackpropNetwork;
import ge.amigo.neuro.console.client.ui.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;

public class BackpropNetworkPanel extends LayoutContainer {
	
	private static final double LEARNING_RATE_INITIAL_VALUE = 0.1;
	
	private static final double LEARNING_MOMENTUM_INITIAL_VALUE = 0.3;
	
	private static final int LEARNING_CYCLES_INITIAL_VALUE = 100;
	
	/**
	 * უკუგავრცელების ნეირონული ქსელი
	 */
	private BackpropNetwork network = null;
	
	/**
	 * ნეირონული ქსელის შექმნის ღილაკი
	 */
	private Button createButton;

	/**
	 * ნეირონული ქსელის კონფიგურაცია.
	 * 5, 7, 2, 1 ნიშნავს, რომ ნეირუნულ ქსელს აქვს 5 შესასვლელი, პირველი 7 ნეირონიანი ფარული შრე, მეორე 2 ნეირონიანი ფარული შრე და 1 ნეირონიანი გამომავალი შრე.
	 */
	private TextField<String> configField = new TextField<String>();
	
	/**
	 * ნეირონული ქსელის სწავლების სიჩქარე
	 */
	private NumberField learningRateField = new NumberField();
	
	/**
	 * ნეირონული ქსელის სწავლების მომენტი
	 */
	private NumberField learningMomentumField = new NumberField();
	
	/**
	 * მონაცემთა არე
	 */
	private TextArea inputDataArea = new TextArea();

	/**
	 * შედეგების საჩვენებელი ველი
	 */
	private TextArea outputDataArea = new TextArea();
	
	/**
	 * ნეირონული ქსელის დათვალიერების ღილაკი
	 */
	private Button networkViewButton;
	
	/**
	 * მონაცემთა ამოცნობის ღილაკი
	 */
	private Button dataRecognitionButton;	
	
	/**
	 * სწავლების ღილაკი
	 */
	private Button sampleLearnButton;	

	/**
	 * ცდომილების შეფასების ღილაკი
	 */
	private Button errorEstimationButton;	

	/**
	 * საწყისი წონების დაყენების ღილაკი
	 */
	private Button initialWeightsSetButton;	

	/**
	 * ნეირონული ქსელის სწავლების ციკლების რაოდენობა
	 */
	private NumberField learningCycleCountField = new NumberField();

	/**
	 * მონაცემთა ჩვენების მარტივი რეჟიმი
	 */
	private CheckBox dataFormField = new CheckBox();
	
	/**
	 * ნეირონული ქსელის სწავლების მომენტი
	 */
	private NumberField dataRangeField = new NumberField();
	
	public BackpropNetwork getNetwork() {
		return network;
	}

	public void setNetwork(BackpropNetwork network) {
		this.network = network;
	}
	
	public BackpropNetworkPanel () {
		
		setStyleAttribute("padding-right", "5px");
		
		// ნეირონული ქსელის კონფიგურაცია
		LayoutContainer networkConfig = new LayoutContainer();
		networkConfig.setLayout(new RowLayout(Orientation.HORIZONTAL));
		networkConfig.setWidth("100%");
		networkConfig.setHeight(40);

		// შესასვლელების, შრეების და ნეირონების რაოდენობა და კონფიგურაცია
		LayoutContainer lc = new LayoutContainer();
		Label inutCountLabel = new Label(UIUtils.getMessage("tab_backpropNetwork_networkConfig"));
		configField.setWidth(100);
		configField.setValue("3, 7, 1");
		lc.add(inutCountLabel);
		lc.add(configField);
		networkConfig.add(lc, new RowData(-1, -1, new Margins(0, 20, 0, 0)));
		
		// სწავლების სიჩქარე
		lc = new LayoutContainer();
		Label learningRateLabel = new Label(UIUtils.getMessage("tab_backpropNetwork_learningRate"));
		learningRateField.setWidth(100);
		learningRateField.setPropertyEditorType(Double.class);
		learningRateField.setValue(LEARNING_RATE_INITIAL_VALUE);
		lc.add(learningRateLabel);
		lc.add(learningRateField);
		networkConfig.add(lc, new RowData(-1, -1, new Margins(0, 20, 0, 0)));
		
		// სწავლების მომენტი
		lc = new LayoutContainer();
		Label learningMomentumLabel = new Label(UIUtils.getMessage("tab_backpropNetwork_learningMomentum"));
		learningMomentumField.setWidth(100);
		learningMomentumField.setPropertyEditorType(Double.class);
		learningMomentumField.setValue(LEARNING_MOMENTUM_INITIAL_VALUE);
		lc.add(learningMomentumLabel);
		lc.add(learningMomentumField);
		networkConfig.add(lc, new RowData(-1, -1, new Margins(0, 20, 0, 0)));
		
		createButton = new Button(UIUtils.getMessage("tab_backpropNetwork_createNetworkButton"));
		createButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				StringBuilder sb = new StringBuilder();
				if (configField.getValue() == null || configField.getValue().equals("")) {
					configField.setValue("3, 7, 1");
				}
				if (learningRateField.getValue() == null) {
					learningRateField.setValue(LEARNING_RATE_INITIAL_VALUE);
				}
				if (learningMomentumField.getValue() == null) {
					learningMomentumField.setValue(LEARNING_MOMENTUM_INITIAL_VALUE);
				}
				
				String[] arr = configField.getValue().replaceAll(" ","").split(",");
				network = new BackpropNetwork();
				List<Integer> counts = new ArrayList<Integer>();
				for (int i = 1; i < arr.length; i++) {
					counts.add(Integer.parseInt(arr[i]));
				}
				network.init(Integer.parseInt(arr[0]), counts, new SigmoidFunction(1));
				
				sb.append(network.toString());
				outputDataArea.setValue(sb.toString());
			}
		});
		
		networkConfig.add(createButton, new RowData(-1, -1, new Margins(15, 20, 0, 0)));
		
		// მონაცემთა არე
		LayoutContainer dataArea = new LayoutContainer();
		dataArea.setLayout(new RowLayout(Orientation.HORIZONTAL));
		dataArea.setWidth("100%");
		dataArea.setHeight(170);
		dataArea.setStyleAttribute("padding-top", "10px");
		
		lc = new LayoutContainer();
		Label inputDataLabel = new Label(UIUtils.getMessage("tab_backpropNetwork_inputDataArea"));
		inputDataArea.setWidth("100%");
		inputDataArea.setHeight(150);
		inputDataArea.setValue("");
		lc.add(inputDataLabel);
		lc.add(inputDataArea);
		lc.setWidth("100%");
		dataArea.add(lc, new RowData(-1, -1, new Margins(0, 20, 0, 0)));
		
		// გამოსახვის რეჟიმები
		LayoutContainer dataFormArea = new LayoutContainer();
		dataFormArea.setLayout(new RowLayout(Orientation.HORIZONTAL));
		dataFormArea.setWidth("100%");
		dataFormArea.setHeight(40);
		dataFormArea.setStyleAttribute("padding-top", "10px");

		// შესასვლელების, შრეების და ნეირონების რაოდენობა და კონფიგურაცია
		lc = new LayoutContainer();
		lc.setStyleAttribute("text-align", "left");
		Label dataFormLabel = new Label(UIUtils.getMessage("tab_backpropNetwork_dataForm"));
		dataFormField.setValue(Boolean.TRUE);
		lc.add(dataFormLabel);
		lc.add(dataFormField);
		dataFormArea.add(lc, new RowData(-1, -1, new Margins(0, 20, 0, 0)));
		
		// შესასვლელების, შრეების და ნეირონების რაოდენობა და კონფიგურაცია
		lc = new LayoutContainer();
		Label dataRangeLabel = new Label(UIUtils.getMessage("tab_backpropNetwork_dataRange"));
		dataRangeField.setValue(100);
		lc.add(dataRangeLabel);
		lc.add(dataRangeField);
		dataFormArea.add(lc, new RowData(-1, -1, new Margins(0, 20, 0, 0)));

		// ღილაკების არე 1
		LayoutContainer buttonArea1 = new LayoutContainer();
		buttonArea1.setLayout(new RowLayout(Orientation.HORIZONTAL));
		buttonArea1.setWidth("100%");
		buttonArea1.setHeight(40);
		buttonArea1.setStyleAttribute("padding-top", "10px");
		
		networkViewButton = new Button(UIUtils.getMessage("tab_backpropNetwork_networkViewButton"));
		networkViewButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				StringBuilder sb = new StringBuilder();
				if (network != null) {
					sb.append(network.toString());
					outputDataArea.setValue(sb.toString());
				}
			}
		});
		
		buttonArea1.add(networkViewButton, new RowData(-1, -1, new Margins(15, 20, 0, 0)));

		dataRecognitionButton = new Button(UIUtils.getMessage("tab_backpropNetwork_recognitionButton"));
		dataRecognitionButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				StringBuilder sb = new StringBuilder();
				if (network != null) {
					String[] samplesArray = inputDataArea.getValue().replaceAll(" ", "").split("\n");
					for (int s = 0; s < samplesArray.length; s++) {
						String[] inputsArray = samplesArray[s].split(",");
						List<Double> inputs = new ArrayList<Double>();
						for (int i = 0; i < inputsArray.length; i++) {
							inputs.add(deScaleToDataRange(Double.parseDouble(inputsArray[i])));
						}
						List<Double> outputs = network.applyInputs(inputs);
						sb.append(samplesArray[s]).append(";");
						for (int i = 0; i < outputs.size(); i++) {
							sb.append(!isSimpleMode() ? outputs.get(i) : scaleToDataRange(network.getLayers().get(network.getLayers().size() - 1).get(i).getNet())).append(" ");
						}
						sb.append("\n");
					}
					outputDataArea.setValue(sb.toString());
				}
			}
		});
		
		buttonArea1.add(dataRecognitionButton, new RowData(-1, -1, new Margins(15, 20, 0, 0)));
		
		errorEstimationButton = new Button(UIUtils.getMessage("tab_backpropNetwork_errorEstimationButton"));
		errorEstimationButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				StringBuilder sb = new StringBuilder();
				if (network != null) {
					String[] samplesArray = inputDataArea.getValue().replaceAll(" ", "").split("\n");
					double averageEnergy = 0;
					for (int s = 0; s < samplesArray.length; s++) {
						String[] sample = samplesArray[s].split(";");
						
						String[] inputsArray = sample[0].split(",");
						List<Double> inputs = new ArrayList<Double>();
						for (int i = 0; i < inputsArray.length; i++) {
							inputs.add(deScaleToDataRange(Double.parseDouble(inputsArray[i])));
						}
						
						
						String[] targetsArray = sample[1].split(",");
						List<Double> targets = new ArrayList<Double>();
						for (int i = 0; i < targetsArray.length; i++) {
							double target = Double.parseDouble(targetsArray[i]);
							targets.add(!isSimpleMode() ? target : network.getLayers().get(network.getLayers().size() - 1).get(i).getActivationFunction().valueAt(deScaleToDataRange(target)));
							
						}
						
						List<Double> outputs = network.applyInputs(inputs);
						
						double energy = 0;
						for (int i = 0; i < outputs.size(); i++) {
							double e = targets.get(i).doubleValue() - outputs.get(i).doubleValue();
							energy += e * e;
						}
						energy /= 2;
						averageEnergy += energy;
						
						sb.append(samplesArray[s]).append(";");
						for (int i = 0; i < outputs.size(); i++) {
							sb.append(!isSimpleMode() ? outputs.get(i) : scaleToDataRange(network.getLayers().get(network.getLayers().size() - 1).get(i).getNet())).append(" ");
						}
						sb.append("\n");
					}
					averageEnergy /= samplesArray.length;
					outputDataArea.setValue("Average squared error energy: " + averageEnergy + "\n" + sb.toString());
				}
			}
		});
		
		buttonArea1.add(errorEstimationButton, new RowData(-1, -1, new Margins(15, 20, 0, 0)));

		initialWeightsSetButton = new Button(UIUtils.getMessage("tab_backpropNetwork_initialWeightsSetButton"));
		initialWeightsSetButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				StringBuilder sb = new StringBuilder();
				if (network != null) {
					List<List<List<Double>>> weights = null; 
					if (inputDataArea.getValue() != null) {
						weights = new ArrayList<List<List<Double>>>();
						String[] layersArray = inputDataArea.getValue().replaceAll(" ", "").split("\n");
						for (int l = 0; l < layersArray.length; l++) {
							List<List<Double>> layer = new ArrayList<List<Double>>();
							String[] neuronsArray = layersArray[l].split(";");
							for (int n = 0; n < neuronsArray.length; n++) {
								String[]  weightsArray = neuronsArray[n].split(",");
								List<Double> w = new ArrayList<Double> ();
								for (int i = 0; i < weightsArray.length; i++) {
									w.add(Double.parseDouble(weightsArray[i]));
								}
								layer.add(w);
							}
							weights.add(layer);
						}
					}				
					network.setWeightsAndBias(weights);
					sb.append(network.toString());
					outputDataArea.setValue(sb.toString());
				}
			}
		});
		
		buttonArea1.add(initialWeightsSetButton, new RowData(-1, -1, new Margins(15, 20, 0, 0)));

		// ღილაკების არე 2
		LayoutContainer buttonArea2 = new LayoutContainer();
		buttonArea2.setLayout(new RowLayout(Orientation.HORIZONTAL));
		buttonArea2.setWidth("100%");
		buttonArea2.setHeight(40);
		buttonArea2.setStyleAttribute("padding-top", "10px");
		
		// სწავლების ციკლების რაოდენობა
		lc = new LayoutContainer();
		Label learningCycleCountLabel = new Label(UIUtils.getMessage("tab_backpropNetwork_learningCycleCount"));
		learningCycleCountField.setWidth(100);
		learningCycleCountField.setPropertyEditorType(Integer.class);
		learningCycleCountField.setValue(LEARNING_CYCLES_INITIAL_VALUE);
		lc.add(learningCycleCountLabel);
		lc.add(learningCycleCountField);
		buttonArea2.add(lc, new RowData(-1, -1, new Margins(0, 20, 0, 0)));
		
		
		sampleLearnButton = new Button(UIUtils.getMessage("tab_backpropNetwork_sampleLearnButton"));
		sampleLearnButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				StringBuilder sb = new StringBuilder();
				if (network != null) {
					String[] samplesArray = inputDataArea.getValue().replaceAll(" ", "").split("\n");
					List<List<Double>> sampleInputs = new ArrayList<List<Double>> ();
					List<List<Double>> sampleTargets = new ArrayList<List<Double>> ();
					for (int s = 0; s < samplesArray.length; s++) {
						String[] sample = samplesArray[s].split(";");
						
						String[] inputsArray = sample[0].split(",");
						List<Double> inputs = new ArrayList<Double>();
						for (int i = 0; i < inputsArray.length; i++) {
							inputs.add(deScaleToDataRange(Double.parseDouble(inputsArray[i])));
						}
						
						
						String[] targetsArray = sample[1].split(",");
						List<Double> targets = new ArrayList<Double>();
						for (int i = 0; i < targetsArray.length; i++) {
							double target = Double.parseDouble(targetsArray[i]);
							targets.add(!isSimpleMode() ? target : network.getLayers().get(network.getLayers().size() - 1).get(i).getActivationFunction().valueAt(deScaleToDataRange(target)));
						}
						
						sampleInputs.add(inputs);
						sampleTargets.add(targets);

					}
					int learningCycleCount = learningCycleCountField.getValue() != null ? learningCycleCountField.getValue().intValue() : LEARNING_CYCLES_INITIAL_VALUE;
					for (int count = 0; count < learningCycleCount; count ++) {
						for (int s = 0; s < samplesArray.length; s++) {
							
							network.learn(sampleInputs.get(s), sampleTargets.get(s));
	
							List<Double> outputs = network.getOut();
							sb.append(samplesArray[s]).append(";");
							for (int i = 0; i < outputs.size(); i++) {
								sb.append(!isSimpleMode() ? outputs.get(i) : scaleToDataRange(network.getLayers().get(network.getLayers().size() - 1).get(i).getNet())).append(" ");
							}
							sb.append("\n");
						}
					}
					outputDataArea.setValue(sb.toString());
				}
			}
		});
		
		buttonArea2.add(sampleLearnButton, new RowData(-1, -1, new Margins(15, 20, 0, 0)));
		
		// შედეგების არე
		LayoutContainer outputArea = new LayoutContainer();
		outputArea.setLayout(new RowLayout(Orientation.HORIZONTAL));
		outputArea.setWidth("100%");
		outputArea.setHeight(170);
		outputArea.setStyleAttribute("padding-top", "10px");
		
		lc = new LayoutContainer();
		Label outputDataLabel = new Label(UIUtils.getMessage("tab_backpropNetwork_outputArea"));
		outputDataArea.setWidth("100%");
		outputDataArea.setHeight(150);
		outputDataArea.setValue("");
		lc.add(outputDataLabel);
		lc.add(outputDataArea);
		lc.setWidth("100%");
		outputArea.add(lc, new RowData(-1, -1, new Margins(0, 20, 0, 0)));
		

		add(networkConfig);
		add(dataArea);		
		add(dataFormArea);		
		add(buttonArea1);		
		add(buttonArea2);		
		add(outputArea);		
	}
	
	/**
	 * მონაცემთა სკალირებული წარმოდგენა.
	 * ეს ფუნქცია განკუთვნილია მხოლოდ შემავალი და გამომავალი მონაცემების წარმოსადგენად მოცემულ დიაპაზონში.
	 * სიგმოიდური ფუნქცია "მუშაობს" -4, +4 ინტერვალში, რაც საკმაოდ მოუხერხებელია მონაცემთა აღქმის თვალსაზრისით.
	 * ამიტომ მოცემულ სიდიდეს ეს ფუნქცია "გაწელავს" მითითებულ რიცხვამდე.
	 * @param net რიცხვი რომლის კალირებაც გვსურს.
	 * @return სიდიდე რომელიც იქნება მითითებულ (dataRangeField ველში) დიაპაზონში. მაგალითად, თუ ველში წერია 100, ჩვენი სიდიდე, რომელიც თავდაპირველად იყო -4, +4 დიაპაზონში, იქნება -100, +100 დიაპაზონში.
	 */
	private Double scaleToDataRange(double net) {
		double scaled = net;
		if (isSimpleMode() && dataRangeField.getValue() != null) {
			scaled = scaled / 4 * dataRangeField.getValue().intValue();
		}		
		return scaled;
	}
	
	/**
	 * სკლაკურებული სიდიდის უკან დაბრუნება საწყის დიაპაზონში
	 */
	private Double deScaleToDataRange(double x) {
		double deScaled = x;
		if (isSimpleMode() && dataRangeField.getValue() != null) {
			deScaled = deScaled / dataRangeField.getValue().intValue() * 4;
		}		
		return deScaled;
	}

	private boolean isSimpleMode() {
		return dataFormField.getValue() != null && dataFormField.getValue().booleanValue(); 
	}

}
