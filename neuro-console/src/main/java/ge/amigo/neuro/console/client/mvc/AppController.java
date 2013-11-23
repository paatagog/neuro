package ge.amigo.neuro.console.client.mvc;

import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;

public class AppController extends Controller {

	private View appView;
	
	public AppController() {
		registerEventTypes(AppEvents.INIT);
	}
	
	@Override
	public void handleEvent(AppEvent event) {
		forwardToView(appView, event);		
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		appView = new AppView(this);
	}
}