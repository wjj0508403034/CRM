package com.huoyun.thirdparty.idp.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huoyun.business.employee.EmployeeErrorCodes;
import com.huoyun.exception.BusinessException;
import com.huoyun.restclient.RestClientFactory;
import com.huoyun.restclient.RestResponse;
import com.huoyun.thirdparty.idp.IdpClient;
import com.huoyun.thirdparty.idp.IdpErrorResponse;
import com.huoyun.thirdparty.idp.IdpHostConfiguration;

public class IdpClientImpl implements IdpClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(IdpClientImpl.class);

	private IdpHostConfiguration idpHostConfig;
	private RestClientFactory restClientFactory;

	public IdpClientImpl(RestClientFactory restClientFactory, IdpHostConfiguration idpHostConfig) {
		this.restClientFactory = restClientFactory;
		this.idpHostConfig = idpHostConfig;
	}

	@Override
	public void changePassword(Long userId, String oldPassword, String newPassword) throws BusinessException {
		LOGGER.info("Change password ...");
		ChangePasswordParam param = new ChangePasswordParam();
		param.setUserId(userId);
		param.setOldPassword(oldPassword);
		param.setNewPassword(newPassword);
		RestResponse restResponse = this.restClientFactory.POST(idpHostConfig.getDomain(),
				this.getRequestEndPoint("changePassword"), param);
		if (restResponse.getStatusCode() == 200) {
			LOGGER.info("Change password successfully.");
			return;
		}

		LOGGER.info("Change password failed.");
		if (restResponse.getStatusCode() == 400) {
			IdpErrorResponse idpError = restResponse.toEntity(IdpErrorResponse.class);
			throw idpError.idpException();
		}

		throw new BusinessException(EmployeeErrorCodes.Change_Password_Error);
	}

	private String getRequestEndPoint(String url) {
		return this.idpHostConfig.getPrefix() + url;
	}

}
