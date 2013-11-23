package ge.amigo.neuro.console.client.ui.data;

import ge.amigo.neuro.console.client.ui.utils.UIUtils;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.Window;

public class DataTab extends TabItem {

	public DataTab() {
		setLayout(new BorderLayout());
		setText(UIUtils.getMessage("tab_data"));
		setStyleAttribute("padding-top", "5px");
		setStyleAttribute("padding-right", "5px");
		setStyleAttribute("padding-bottom", "5px");
		setStyleAttribute("padding-left", "5px");
		ContentPanel html = new ContentPanel();
		html.setBorders(false);
		html.setHeaderVisible(false);
		html.setUrl("http://" + Window.Location.getHost() + Window.Location.getPath().substring(0, Window.Location.getPath().lastIndexOf("/") + 1) + "data/data.html");
		add(html, new BorderLayoutData(LayoutRegion.CENTER));
		add(new ConvertPanel(), new BorderLayoutData(LayoutRegion.WEST, 500));
	}
}
