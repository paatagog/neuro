package ge.amigo.neuro.console.client.ui;

import ge.amigo.neuro.console.client.AppConstants;
import ge.amigo.neuro.console.client.Messages;
import ge.amigo.neuro.console.client.resources.Icons;

import com.extjs.gxt.ui.client.Registry;
import com.google.gwt.core.client.GWT;

public class LocalRegistry {
		public static void init() {
			Registry.register(AppConstants.ICONS, GWT.create(Icons.class));
			Registry.register(AppConstants.MESSAGES, GWT.create(Messages.class));
			Registry.register(AppConstants.NEURO_SERVICE,	null);
		}
		
		public static Icons getIcons() {
			return (Icons)Registry.get(AppConstants.ICONS);
		}

		public static Messages getMessages() {
			return (Messages)Registry.get(AppConstants.MESSAGES);
		}

}
