package com.huoyun.core.bo;

import javax.persistence.MappedSuperclass;
import com.huoyun.core.bo.annotation.BoProperty;

@MappedSuperclass
public abstract class DefaultBusinessObject extends AbstractBusinessObject {

	public DefaultBusinessObject() {
	}

	public DefaultBusinessObject(BusinessObjectFacade boFacade) {
		this.setBoFacade(boFacade);
	}

	@BoProperty(readonly = true, label = I18n_Label_Owner_Code)
	private Long ownerCode;

	@BoProperty(readonly = true, label = I18n_Label_Creator_Code)
	protected Long creatorCode;

	@BoProperty(readonly = true, label = I18n_Label_Updator_Code)
	protected Long updateCode;

	public Long getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(Long ownerCode) {
		this.ownerCode = ownerCode;
	}

	public Long getCreatorCode() {
		return creatorCode;
	}

	public void setCreatorCode(Long creatorCode) {
		this.creatorCode = creatorCode;
	}

	public Long getUpdateCode() {
		return updateCode;
	}

	public void setUpdateCode(Long updateCode) {
		this.updateCode = updateCode;
	}
}