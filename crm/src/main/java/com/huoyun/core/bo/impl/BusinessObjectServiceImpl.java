package com.huoyun.core.bo.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;

import com.huoyun.core.bo.BoErrorCode;
import com.huoyun.core.bo.BusinessObject;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.BusinessObjectMapper;
import com.huoyun.core.bo.BusinessObjectService;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.query.BoSpecification;
import com.huoyun.core.bo.query.CriteriaFactory;
import com.huoyun.core.bo.query.criteria.Criteria;
import com.huoyun.core.bo.query.impl.BoSpecificationImpl;
import com.huoyun.exception.BusinessException;

public class BusinessObjectServiceImpl implements BusinessObjectService {

	private BusinessObjectFacade boFacade;
	private BusinessObjectMapper boMapper;
	private CriteriaFactory criteriaFactory;

	public BusinessObjectServiceImpl(BusinessObjectFacade boFacade,
			BusinessObjectMapper boMapper, CriteriaFactory criteriaFactory) {
		this.boFacade = boFacade;
		this.boMapper = boMapper;
		this.criteriaFactory = criteriaFactory;
	}

	@Override
	public BusinessObject initBo(String namespace, String name)
			throws BusinessException {
		this.getBoMeta(namespace, name);
		return this.boFacade.newBo(namespace, name);
	}

	@Transactional
	@Override
	public Map<String, Object> createBo(String namespace, String name,
			Map<String, Object> data) throws BusinessException {
		BoMeta boMeta = this.getBoMeta(namespace, name);

		BusinessObject bo = this.boFacade.newBo(namespace, name);
		for (PropertyMeta propMeta : boMeta.getProperties()) {
			bo.setPropertyValue(propMeta.getName(),
					data.get(propMeta.getName()));
		}
		bo.create();

		return this.boMapper.converterTo(bo, boMeta);
	}

	@Override
	public Map<String, Object> load(String namespace, String name, Long id)
			throws BusinessException {
		BoMeta boMeta = this.getBoMeta(namespace, name);

		BusinessObject bo = this.boFacade.getBoRepository(namespace, name)
				.load(id);

		return this.boMapper.converterTo(bo, boMeta);
	}

	@Modifying
	@Transactional
	@Override
	public void delete(String namespace, String name, Long id)
			throws BusinessException {
		this.getBoMeta(namespace, name);

		BusinessObject bo = this.boFacade.getBoRepository(namespace, name)
				.load(id);
		if (bo == null) {
			throw new BusinessException(BoErrorCode.Bo_Record_Not_Found);
		}

		bo.delete();
	}

	@Modifying
	@Transactional
	@Override
	public BusinessObject updateBo(String namespace, String name, Long id,
			Map<String, Object> data) throws BusinessException {
		BoMeta boMeta = this.getBoMeta(namespace, name);

		BusinessObject bo = this.boFacade.getBoRepository(namespace, name)
				.load(id);
		if (bo == null) {
			throw new BusinessException(BoErrorCode.Bo_Record_Not_Found);
		}

		for (PropertyMeta propMeta : boMeta.getProperties()) {
			if (data.containsKey(propMeta.getName())) {
				bo.setPropertyValue(propMeta.getName(),
						data.get(propMeta.getName()));
			}
		}

		bo.update();

		return bo;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Page<Map<String, Object>> query(String namespace, String name,
			Pageable pageable, String query) throws BusinessException {
		BoMeta boMeta = this.getBoMeta(namespace, name);
		BoSpecification spec = this.getBoSpec(boMeta, query);
		Page<BusinessObject> pageData = this.boFacade.getBoRepository(
				namespace, name).query(spec, pageable);
		List<Map<String, Object>> resultList = new ArrayList<>();
		for (BusinessObject bo : pageData.getContent()) {
			resultList.add(this.boMapper.converterTo(bo, boMeta));
		}

		return new PageImpl<Map<String, Object>>(resultList, pageable,
				pageData.getTotalElements());

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Long count(String namespace, String name, String query)
			throws BusinessException {
		BoMeta boMeta = this.getBoMeta(namespace, name);
		BoSpecification spec = this.getBoSpec(boMeta, query);
		return this.boFacade.getBoRepository(namespace, name).count(spec);
	}

	@SuppressWarnings("rawtypes")
	private BoSpecification getBoSpec(BoMeta boMeta, String query)
			throws BusinessException {
		Criteria criterias = this.criteriaFactory.parse(boMeta, query);
		return BoSpecificationImpl.newInstance(boMeta.getBoType(), boMeta,
				criterias);
	}

	private BoMeta getBoMeta(String namespace, String name)
			throws BusinessException {
		BoMeta boMeta = this.boFacade.getMetadataRepository().getBoMeta(
				namespace, name);
		if (boMeta == null) {
			throw new BusinessException(BoErrorCode.Unkown_Bo_Entity);
		}

		return boMeta;
	}

}
