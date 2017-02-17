package com.huoyun.core.bo.query;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.huoyun.core.bo.query.criteria.CriteriaExpr;
import com.huoyun.core.bo.query.criteria.EqualCriteria;
import com.huoyun.core.bo.query.criteria.QueryCriteria;

public class QueryParam {

	private List<CriteriaExpr> criterias = new ArrayList<>();

	public List<CriteriaExpr> getCriterias() {
		return criterias;
	}

	public void setCriterias(List<CriteriaExpr> criterias) {
		this.criterias = criterias;
	}

	public List<QueryCriteria> parse() {
		List<QueryCriteria> list = new ArrayList<>();

		for (CriteriaExpr expr : this.criterias) {
			if (StringUtils.equalsIgnoreCase(expr.getOp(), "=")) {
				list.add(new EqualCriteria(expr));
			}
		}

		return list;
	}
}
