package ge.amigo.neuro.console.client.ui;

import ge.amigo.neuro.console.client.ui.utils.UIUtils;

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;

public class FooterPanel extends LayoutContainer {

	public FooterPanel () {
		setStyleAttribute("background-color", "#d4e0f3");
		setStyleAttribute("border-top", "1px solid #FFFFFF");

		Label copyrightLabel = new Label(UIUtils.getMessage("copyright"));
		copyrightLabel.setStyleName("copyright");
		copyrightLabel.setStyleAttribute("padding-left", "10px");
		add(copyrightLabel);
		setHeight(20);
	}
}
