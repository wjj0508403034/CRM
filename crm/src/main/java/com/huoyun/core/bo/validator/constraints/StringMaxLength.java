package com.huoyun.core.bo.validator.constraints;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.exception.BusinessException;

public class StringMaxLength extends StringValidator{

	public StringMaxLength(BusinessObjectFacade boFacade, PropertyMeta propertyMeta,Object propertyValue) {
		super(boFacade, propertyMeta,propertyValue);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void validator() throws BusinessException {
		// TODO Auto-generated method stub
		
	}

}
