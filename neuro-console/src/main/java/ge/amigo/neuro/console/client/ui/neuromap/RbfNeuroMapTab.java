package ge.amigo.neuro.console.client.ui.neuromap;

import ge.amigo.neuro.console.client.ui.utils.UIUtils;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;

public class RbfNeuroMapTab extends TabItem {
	
	public RbfNeuroMapTab() {
		setLayout(new BorderLayout());
		setText(UIUtils.getMessage("tab_neuromap"));
		setStyleAttribute("padding-top", "5px");
		setStyleAttribute("padding-right", "5px");
		setStyleAttribute("padding-bottom", "5px");
		setStyleAttribute("padding-left", "5px");

		add(new RbfNeuroPanel(), new BorderLayoutData(LayoutRegion.WEST, 600));
		add(new MapPanel(), new BorderLayoutData(LayoutRegion.CENTER));
		
	}
}
