package com.athenex.configuration;

import org.osgi.service.component.annotations.Component;

import com.liferay.configuration.admin.category.ConfigurationCategory;

/**
 * @author Marc
 *
 */
@Component(service = ConfigurationCategory.class)
public class AthenexConfigurationCategory implements ConfigurationCategory {

	private static final String CATEGORY_ICON = "cog";
	private static final String CATEGORY_KEY = "login-post-configuration";
	private static final String CATEGORY_SECTION = "athenex-configuration";

	@Override
	public String getCategoryIcon() {
		return CATEGORY_ICON;
	}

	@Override
	public String getCategoryKey() {
		return CATEGORY_KEY;
	}

	@Override
	public String getCategorySection() {
		return CATEGORY_SECTION;
	}
}
