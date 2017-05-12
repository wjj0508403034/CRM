package com.huoyun.core.bo;

import java.util.List;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.core.bo.ext.UserEntity;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.exception.BusinessException;

@MappedSuperclass
public abstract class AbstractBusinessObjectImpl extends AbstractBusinessObject implements ExtensibleBusinessObject {

	public AbstractBusinessObjectImpl() {
	}

	public AbstractBusinessObjectImpl(BusinessObjectFacade boFacade) {
		this.setBoFacade(boFacade);

		if (this.boFacade != null) {
			this.userEntity = this.boFacade.getExtensionService().createDynamicEntity(this.boMeta);
		}
	}

	@Transient
	private UserEntity userEntity;

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

	@Override
	public UserEntity getUserEntity() {
		return this.userEntity;
	}

	@Override
	public void setPropertyValue(String propertyName, Object propertyValue) throws BusinessException {
		PropertyMeta propMeta = this.boMeta.getPropertyMeta(propertyName);
		if (propMeta.isCustomField()) {
			if (this.userEntity == null) {
				this.userEntity = this.boFacade.getExtensionService().load(this, this.boMeta);
			}

			if (this.userEntity == null) {
				this.userEntity = this.boFacade.getExtensionService().createDynamicEntity(this.boMeta);
				this.boFacade.getExtensionService().persist(this);
			}
			this.userEntity.setPropertyValue(propertyName, propertyValue);
			return;
		}

		if (propMeta.getNodeMeta() != null) {
			List<BusinessObject> listProp = (List<BusinessObject>) this.getPropertyValue(propertyName);
			this.setNodeValue(propMeta, listProp, (List) propertyValue);
			return;
		}

		super.setPropertyValue(propertyName, propertyValue);
	}

	private void setNodeValue(PropertyMeta propMeta, List<BusinessObject> listProp, List nodeValues) {
		listProp.clear();
		BoMeta boMeta = this.boFacade.getMetadataRepository().getBoMeta(propMeta.getNodeMeta().getNodeClass());
		ObjectMapper mapper = new ObjectMapper();
		for (Object nodeValue : nodeValues) {
			BusinessObject bo = mapper.convertValue(nodeValue, propMeta.getNodeMeta().getNodeClass());
			listProp.add(bo);
		}

	}

	@Override
	public Object getPropertyValue(String propertyName) throws BusinessException {
		PropertyMeta propMeta = this.boMeta.getPropertyMeta(propertyName);
		if (propMeta.isCustomField()) {
			if (this.userEntity == null) {
				this.userEntity = this.boFacade.getExtensionService().load(this, this.boMeta);
			}

			if (this.userEntity != null) {
				return this.userEntity.getPropertyValue(propertyName);
			}

			return null;
		}

		return super.getPropertyValue(propertyName);
	}

	@Override
	protected final void onCreate() {
		this.boRepository.flush();
		this.boFacade.getExtensionService().persist(this);
	}
}
