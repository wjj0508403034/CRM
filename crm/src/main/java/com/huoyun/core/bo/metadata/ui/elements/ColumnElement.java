package com.huoyun.core.bo.metadata.ui.elements;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "column")
public class ColumnElement implements Serializable {

	private static final long serialVersionUID = 3576131079830154565L;
	
	@XmlAttribute(name = "ref", required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	private String ref;

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}
}
