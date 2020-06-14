
package com.athenex.lifecycle.loginpreaction;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import com.athenex.configuration.LoginPostConfiguration;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.LifecycleAction;
import com.liferay.portal.kernel.events.LifecycleEvent;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.struts.LastPath;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

/**
 * @author Marc
 *
 */
@Component(configurationPid = "com.athenex.configuration.LoginPostConfiguration", immediate = true, property = {
		"key=login.events.post" }, service = LifecycleAction.class)
public class LoginPostAction implements LifecycleAction {

	@Override
	public void processLifecycleEvent(LifecycleEvent lifecycleEvent) throws ActionException {

		HttpServletRequest request = lifecycleEvent.getRequest();
		HttpServletResponse response = lifecycleEvent.getResponse();

		User user = null;
		try {
			user = PortalUtil.getUser(request);
			sendRedirect(request, response, user);
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	private void sendRedirect(HttpServletRequest request, HttpServletResponse response, User user) {

		String angehorigeRoleName = _loginPostConfiguration.angehorigeRoleName();
		if (logger.isDebugEnabled()) {
			logger.debug("angehorigeRoleName=" + angehorigeRoleName);
		}
		if (Validator.isNotNull(angehorigeRoleName)) {
			boolean hasAngehorigeRole = hasUserRole(user, angehorigeRoleName);
			if (hasAngehorigeRole) {
				String friendlyURL = getAngehorigeFriendlyURL(user);
				if (logger.isDebugEnabled()) {
					logger.debug("friendlyURL=" + friendlyURL);
				}
				if (Validator.isNotNull(friendlyURL)) {
					setPath(request, friendlyURL);
					return;
				}
			}
		}

		String mitarbeiterRoleName = _loginPostConfiguration.mitarbeiterRoleName();
		if (logger.isDebugEnabled()) {
			logger.debug("mitarbeiterRoleName=" + mitarbeiterRoleName);
		}
		if (Validator.isNotNull(mitarbeiterRoleName)) {
			boolean hasMitarbeiterRole = hasUserRole(user, mitarbeiterRoleName);
			if (hasMitarbeiterRole) {
				String mitarbeiterSite = _loginPostConfiguration.mitarbeiterLandingPageURL();
				if (logger.isDebugEnabled()) {
					logger.debug("mitarbeiterSite=" + mitarbeiterSite);
				}
				if (Validator.isNotNull(mitarbeiterSite)) {
					setPath(request, mitarbeiterSite);
					return;
				}
			}
		}

		String defaultLandingPageURL = _loginPostConfiguration.defaultLandingPageURL();
		if (Validator.isNotNull(defaultLandingPageURL)) {
			setPath(request, defaultLandingPageURL);
		}
	}

	private String getAngehorigeFriendlyURL(User user) {

		try {
			List<Group> siteGroups = user.getSiteGroups();
			String pathFriendlyURLPublic = PortalUtil.getPathFriendlyURLPublic();
			String pathFriendlyURLPrivateGroup = PortalUtil.getPathFriendlyURLPrivateGroup();
			if (logger.isDebugEnabled()) {
				logger.debug("pathFriendlyURLPublic=" + pathFriendlyURLPublic + ", pathFriendlyURLPrivateGroup="
						+ pathFriendlyURLPrivateGroup);
			}
			for (Group group : siteGroups) {
				if (group.isGuest() || !group.isSite() || !group.isActive()) {
					continue;
				}
				String friendlyURL = group.getFriendlyURL();
				return group.hasPublicLayouts() ? pathFriendlyURLPublic + friendlyURL
						: pathFriendlyURLPrivateGroup + friendlyURL;
			}

		} catch (PortalException e) {
			logger.error(e, e);
		}

		return null;
	}

	private void setPath(HttpServletRequest request, String path) {
		LastPath lastPath = new LastPath(StringPool.BLANK, path);
		request.getSession().setAttribute(WebKeys.LAST_PATH, lastPath);
	}

	private Boolean hasUserRole(User user, String roleName) {
		List<Role> roles = user.getRoles();
		if (roles != null && !roles.isEmpty()) {
			for (Role role : roles) {

				if (role.getName().equals(roleName)) {
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	@Activate
	@Modified
	protected void activate(Map<Object, Object> properties) {
		_loginPostConfiguration = ConfigurableUtil.createConfigurable(LoginPostConfiguration.class, properties);
	}

	private volatile LoginPostConfiguration _loginPostConfiguration;

	private static Log logger = LogFactoryUtil.getLog(LoginPostAction.class);
}