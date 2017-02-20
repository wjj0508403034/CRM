package com.huoyun.core.bo;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class LiteBusinessObject extends AbstractBusinessObject {

	public LiteBusinessObject() {
	}

	public LiteBusinessObject(BusinessObjectFacade boFacade) {
		this.setBoFacade(boFacade);
	}

}
