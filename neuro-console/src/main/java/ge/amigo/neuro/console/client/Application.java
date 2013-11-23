package ge.amigo.neuro.console.client;

import ge.amigo.neuro.console.client.mvc.AppController;
import ge.amigo.neuro.console.client.mvc.AppEvents;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.google.gwt.core.client.EntryPoint;

public class Application implements EntryPoint {

	@Override
	public void onModuleLoad() {
		Dispatcher dispatcher = Dispatcher.get();
		dispatcher.addController(new AppController());
		dispatcher.dispatch(AppEvents.INIT);
	}
}