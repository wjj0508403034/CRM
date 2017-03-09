package com.huoyun.core.bo;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.huoyun.exception.BusinessException;

public interface BusinessObjectService {

	BusinessObject initBo(String namespace, String name)
			throws BusinessException;

	Map<String, Object> createBo(String namespace, String name,
			Map<String, Object> data) throws BusinessException;

	Map<String, Object> load(String namespace, String name, Long id)
			throws BusinessException;

	void delete(String namespace, String name, Long id)
			throws BusinessException;

	BusinessObject updateBo(String namespace, String name, Long id,
			Map<String, Object> data) throws BusinessException;

	Page<Map<String, Object>> query(String namespace, String name,
			Pageable pageable, String query) throws BusinessException;

	Long count(String namespace, String name, String query)
			throws BusinessException;
}
