package com.huoyun.locale.impl;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import com.huoyun.locale.LocaleService;

public class LocaleServiceImpl implements LocaleService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(LocaleServiceImpl.class);

	public LocaleServiceImpl(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	private MessageSource messageSource;

	@Override
	public String getMessage(String key) {
		try {
			return this.messageSource.getMessage(key, null, Locale.CHINA);
		} catch (Exception ex) {
			LOGGER.warn("Not found {} string in localization file", key);
		}
		return null;
	}

	@Override
	public String getErrorMessage(String errorCode) {
		return this.getMessage("ErrorCode_" + errorCode);
	}

}
