package com.athenex.configuration;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

import aQute.bnd.annotation.metatype.Meta;

/**
 * @author Marc
 *
 */
@ExtendedObjectClassDefinition(category = "login-post-configuration", generateUI = true, scope = ExtendedObjectClassDefinition.Scope.SYSTEM)
@Meta.OCD(id = "com.athenex.configuration.LoginPostConfiguration", localization = "content/Language", name = "login-post-configuration")
public interface LoginPostConfiguration {

	@Meta.AD(required = false, name = "mitarbeiter-role-name")
	public String mitarbeiterRoleName();

	@Meta.AD(required = false, name = "angehorige-role-name")
	public String angehorigeRoleName();

	@Meta.AD(required = false, deflt = "/web/guest/home", name = "mitarbeiter-landing-page")
	public String mitarbeiterLandingPageURL();

	@Meta.AD(required = false, name = "default-landing-page")
	public String defaultLandingPageURL();
}
