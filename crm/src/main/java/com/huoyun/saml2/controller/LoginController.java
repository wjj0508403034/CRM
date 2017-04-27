package com.huoyun.saml2.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.huoyun.login.LoginProcessor;
import com.huoyun.saml2.EndpointsConstatns;
import com.huoyun.saml2.configuration.SAML2Configuration;
import com.huoyun.saml2.configuration.SAML2SPConfigurationCustom;
import com.huoyun.saml2.configuration.SAML2SPConfigurationFactory;
import com.sap.security.saml2.commons.SAML2Principal;
import com.sap.security.saml2.lib.bindings.HTTPPostBinding;
import com.sap.security.saml2.lib.common.SAML2Utils;
import com.sap.security.saml2.sp.sso.SAML2Authentication;

@Controller
@RequestMapping(value = "/saml2")
public class LoginController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private SAML2SPConfigurationFactory saml2SPConfigurationFactory;

	@Autowired
	private ApplicationContext context;

	private SAML2Authentication saml2Authentication = SAML2Authentication.getInstance();

	@RequestMapping(value = "/sp/acs", method = RequestMethod.POST)
	public String acs(@RequestParam(HTTPPostBinding.SAML_RESPONSE) String samlResponse,
			@RequestParam(HTTPPostBinding.SAML_RELAY_STATE) String relayState,
			@RequestParam(EndpointsConstatns.SSO_SESSION_INDEX) String sessionIndex, HttpServletRequest req,
			HttpServletResponse res) {
		SAML2Configuration saml2Configuration = saml2SPConfigurationFactory.getSAML2Configuration();
		SAML2SPConfigurationCustom spConfiguration = saml2SPConfigurationFactory.createSAML2SPConfiguration();
		String acs = this.saml2SPConfigurationFactory.getAcsUrl(req);
		try {
			SAML2Principal saml2Principal = saml2Authentication.validateResponse(spConfiguration,
					SAML2Utils.decodeBase64AsString(samlResponse), null, acs);
			changeSessionId(req);

			LOGGER.info("SSO authorization succeed for user {}.", saml2Principal.getName());

			req.getSession().setAttribute(EndpointsConstatns.SSO_USER_SESSION_ATTR, saml2Principal.getName());
			req.getSession().setAttribute(EndpointsConstatns.SSO_PRINCIPAL_SESSION_ATTR, saml2Principal);
			req.getSession().setAttribute(EndpointsConstatns.SSO_SESSION_INDEX, sessionIndex);

			this.context.getBean(LoginProcessor.class).process(Long.getLong(saml2Principal.getName()));
			if (!StringUtils.isEmpty(relayState)) {
				if (!StringUtils.endsWithIgnoreCase(relayState, "/sp/logout")
						&& !StringUtils.endsWithIgnoreCase(relayState, "/sp/slo")) {
					return "redirect:" + relayState;
				}
			}

			return "redirect:" + saml2Configuration.getHomePage();
		} catch (Exception ex) {
			LOGGER.error("Login occur error", ex);
			return "redirect: loginerror";
		}
	}

	/*
	 * Change session id in order to prevent 'session-fixation' attacks
	 */
	private HttpSession changeSessionId(HttpServletRequest req) {
		HttpSession session = req.getSession(false);

		if (session == null) {
			return req.getSession();
		}

		Map<String, Object> sessionValues = new HashMap<String, Object>();

		Enumeration<String> keys = session.getAttributeNames();

		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			Object value = session.getAttribute(key);
			sessionValues.put(key, value);
		}

		session.invalidate();

		session = req.getSession();

		Set<Map.Entry<String, Object>> entrys = sessionValues.entrySet();
		for (Map.Entry<String, Object> entry : entrys) {
			session.setAttribute(entry.getKey(), entry.getValue());
		}

		return session;
	}
}
