package com.athenex.language.resource.bundle;

import com.liferay.portal.kernel.language.UTF8Control;
import org.osgi.service.component.annotations.Component;

import java.util.Enumeration;
import java.util.ResourceBundle;

@Component(property = { "language.id=de_DE" }, service = ResourceBundle.class)
public class DE_DE_ResourceBundle extends ResourceBundle {

	private final ResourceBundle resourceBundle = ResourceBundle.getBundle("content.Language_de_DE",
			UTF8Control.INSTANCE);

	@Override
	protected Object handleGetObject(String key) {
		return resourceBundle.getObject(key);
	}

	@Override
	public Enumeration<String> getKeys() {
		return resourceBundle.getKeys();
	}
}