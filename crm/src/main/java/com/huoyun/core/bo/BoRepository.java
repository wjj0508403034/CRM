package com.huoyun.core.bo;

import javax.persistence.TypedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.huoyun.core.bo.query.BoSpecification;
import com.huoyun.exception.BusinessException;

public interface BoRepository<T extends BusinessObject> {

	T load(Long id);

	void save(T bo);

	void update(T bo);

	void delete(T bo);

	void flush();

	Long count(BoSpecification<T> spec) throws BusinessException;

	Page<T> query(BoSpecification<T> spec, Pageable pageable) throws BusinessException;
	
	TypedQuery<T> newQuery(String sql);
}
