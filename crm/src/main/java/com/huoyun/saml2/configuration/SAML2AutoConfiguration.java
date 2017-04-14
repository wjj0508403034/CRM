package com.huoyun.saml2.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.huoyun.saml2.SAMLProperties;
import com.sap.security.saml2.cfg.exceptions.SAML2ConfigurationException;

@Configuration
public class SAML2AutoConfiguration {

	@Bean
	public SAML2SPConfigurationFactory saml2SPConfigurationFactory(
			SAMLProperties samlProperties) throws SAML2ConfigurationException {
		return new SAML2SPConfigurationFactory(samlProperties);
	}
}
