package com.huoyun.core.bo;

import com.huoyun.exception.BusinessException;

public interface BusinessObject {

	public Long getId();

	void init() throws BusinessException;

	void create() throws BusinessException;

	void update() throws BusinessException;

	void delete() throws BusinessException;

	void setPropertyValue(String propertyName, Object propertyValue) throws BusinessException;

	void setBoFacade(BusinessObjectFacade boFacade);

	Object getPropertyValue(String propertyName) throws BusinessException;
}
