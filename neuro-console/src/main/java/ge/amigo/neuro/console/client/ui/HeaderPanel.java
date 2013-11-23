package ge.amigo.neuro.console.client.ui;

import ge.amigo.neuro.console.client.resources.Icons;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.google.gwt.user.client.ui.Image;

public class HeaderPanel extends LayoutContainer {

	private Icons icons = LocalRegistry.getIcons();

	public HeaderPanel () {
		setStyleAttribute("background-color", "#d4e0f3");
		setStyleAttribute("border-bottom", "1px solid #FFFFFF");
		setStyleAttribute("padding-top", "2px");
		setStyleAttribute("padding-left", "5px");
		Image headerLogo = new Image(icons.headerLogo());
		add(headerLogo);
		setHeight(36);
	}
}
