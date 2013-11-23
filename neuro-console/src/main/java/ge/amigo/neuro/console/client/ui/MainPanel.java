package ge.amigo.neuro.console.client.ui;

import ge.amigo.neuro.console.client.ui.data.DataTab;
import ge.amigo.neuro.console.client.ui.neuromap.BackpropNetworkTab;
import ge.amigo.neuro.console.client.ui.neuromap.BinaryNeuronTab;
import ge.amigo.neuro.console.client.ui.neuromap.RbfNeuroMapTab;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class MainPanel extends LayoutContainer {
	
	public MainPanel() {
		
		TabPanel tabPanel = new TabPanel();
		setLayout(new FitLayout());
		add(tabPanel);
		
		// RBF ქსელის გვერდი
		tabPanel.add(new RbfNeuroMapTab());

		// ორობითი ნეირონის გვერდი
		tabPanel.add(new BackpropNetworkTab());

		// ორობითი ნეირონის გვერდი
		tabPanel.add(new BinaryNeuronTab());

		// მონაცემთა გვერდი
		tabPanel.add(new DataTab());
	}

}
