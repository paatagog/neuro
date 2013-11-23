package ge.amigo.neuro.console.client.mvc;

import ge.amigo.neuro.console.client.ui.FooterPanel;
import ge.amigo.neuro.console.client.ui.HeaderPanel;
import ge.amigo.neuro.console.client.ui.LocalRegistry;
import ge.amigo.neuro.console.client.ui.MainPanel;
import ge.amigo.neuro.console.client.ui.MenuPanel;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.RootPanel;

public class AppView extends View {

	public static Viewport viewport = new Viewport();
	
	public AppView(AppController appController) {
		super(appController);
	}

	@Override
	protected void handleEvent(AppEvent event) {
		EventType eventType = event.getType();
		if (eventType.equals(AppEvents.INIT)) {
			onInit(event);
		}
	}

	private void onInit(AppEvent event) {
		LocalRegistry.init();

		viewport.setLayout(new FitLayout());
		ContentPanel cp = new ContentPanel();
		cp.setHeaderVisible(false);
		cp.setLayout(new FitLayout());
		cp.setTopComponent(new HeaderPanel());
		cp.setBottomComponent(new FooterPanel());
		
		LayoutContainer mainLc = new LayoutContainer();
		mainLc.setLayout(new BorderLayout());
		BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
		BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 150);
		
		mainLc.add(new MenuPanel(), westData);
		mainLc.add(new MainPanel(), centerData);
		cp.add(mainLc);
		
		viewport.addStyleName("pageBorderLayout");
		viewport.add(cp);

		RootPanel.get().add(viewport);
	}
	
}