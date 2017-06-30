package com.huoyun.thirdparty.idp.impl;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
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

	private static final Logger LOGGER = LoggerFactory
			.getLogger(IdpClientImpl.class);

	private final static String HTTP_Header_X_VIA = "X-VIA";
	private final static String HTTP_Header_X_SERVER_TOKEN = "X-SERVER-TOKEN";

	private IdpHostConfiguration idpHostConfig;
	private RestClientFactory restClientFactory;

	public IdpClientImpl(RestClientFactory restClientFactory,
			IdpHostConfiguration idpHostConfig) {
		this.restClientFactory = restClientFactory;
		this.idpHostConfig = idpHostConfig;
	}

	@Override
	public void changePassword(Long userId, String oldPassword,
			String newPassword) throws BusinessException {
		LOGGER.info("Change password ...");
		ChangePasswordParam param = new ChangePasswordParam();
		param.setUserId(userId);
		param.setOldPassword(oldPassword);
		param.setNewPassword(newPassword);
		RestResponse restResponse = this.restClientFactory.post(
				idpHostConfig.getDomain(),
				this.getRequestEndPoint("users/changePassword"), param,
				this.getDefaultHeaders());
		if (restResponse.getStatusCode() == 200) {
			LOGGER.info("Change password successfully.");
			return;
		}

		LOGGER.info("Change password failed.");
		if (restResponse.getStatusCode() == 400) {
			IdpErrorResponse idpError = restResponse
					.toEntity(IdpErrorResponse.class);
			throw idpError.idpException();
		}

		throw new BusinessException(EmployeeErrorCodes.Change_Password_Error);
	}

	@Override
	public CreateUserResponse createUser(CreateUserParam createUserParam)
			throws BusinessException {
		RestResponse restResponse = this.restClientFactory.post(
				idpHostConfig.getDomain(), this.getRequestEndPoint("users"),
				createUserParam, this.getDefaultHeaders());
		if (restResponse.getStatusCode() == 200) {
			LOGGER.info("Create user successfully.");
			return restResponse.toEntity(CreateUserResponse.class);
		}

		LOGGER.info("Create user failed.");
		if (restResponse.getStatusCode() == 400) {
			IdpErrorResponse idpError = restResponse
					.toEntity(IdpErrorResponse.class);
			throw idpError.idpException();
		}

		throw new BusinessException(EmployeeErrorCodes.Create_User_Error);
	}

	@Override
	public CreateUserResponse updateUser(UpdateUserParam param)
			throws BusinessException {
		RestResponse restResponse = this.restClientFactory.patch(
				idpHostConfig.getDomain(), this.getRequestEndPoint("users"),
				param, this.getDefaultHeaders());
		if (restResponse.getStatusCode() == 200) {
			LOGGER.info("Update user successfully.");
			return restResponse.toEntity(CreateUserResponse.class);
		}

		LOGGER.info("Update user failed.");
		if (restResponse.getStatusCode() == 400) {
			IdpErrorResponse idpError = restResponse
					.toEntity(IdpErrorResponse.class);
			throw idpError.idpException();
		}

		throw new BusinessException(EmployeeErrorCodes.Update_User_Error);
	}

	@Override
	public void deleteUser(DeleteUserParam deleteUserParam)
			throws BusinessException {
		RestResponse restResponse = this.restClientFactory.delete(
				idpHostConfig.getDomain(), this.getRequestEndPoint("users"),
				deleteUserParam, this.getDefaultHeaders());
		if (restResponse.getStatusCode() == 200) {
			LOGGER.info("Delete user successfully.");
			return;
		}

		LOGGER.info("Delete user failed.");
		if (restResponse.getStatusCode() == 400) {
			IdpErrorResponse idpError = restResponse
					.toEntity(IdpErrorResponse.class);
			throw idpError.idpException();
		}

		throw new BusinessException(EmployeeErrorCodes.Delete_User_Error);
	}

	private String getRequestEndPoint(String url) {
		return this.idpHostConfig.getPrefix() + url;
	}

	private Header[] getDefaultHeaders() {
		Header[] headers = new Header[2];
		headers[0] = new BasicHeader(HTTP_Header_X_VIA, "CRM");
		headers[1] = new BasicHeader(HTTP_Header_X_SERVER_TOKEN,
				this.idpHostConfig.getServiceToken());
		return headers;
	}

}
