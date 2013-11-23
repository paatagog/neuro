package ge.amigo.neuro.console.client.ui;

import ge.amigo.neuro.console.client.ui.utils.UIUtils;

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;

public class MenuPanel extends LayoutContainer {

	public MenuPanel() {
		setStyleAttribute("border-right", "1px solid #FFFFFF");
		setStyleAttribute("padding-top", "5px");
		setStyleAttribute("padding-right", "5px");
		setStyleAttribute("padding-bottom", "5px");
		setStyleAttribute("padding-left", "5px");
		Label doc = new Label("<b><a href='./data/users_manual.docx'>" + UIUtils.getMessage("documentation") + "</a></b><br/>");
		Label source = new Label("<b><a href='./data/sources.zip'>" + UIUtils.getMessage("source") + "</a></b>");
		add(doc);
		add(source);
	}
}
