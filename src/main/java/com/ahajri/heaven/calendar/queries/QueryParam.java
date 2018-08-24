package com.ahajri.heaven.calendar.queries;

import java.io.Serializable;

import com.ahajri.heaven.calendar.constants.enums.OperatorEnum;

public class QueryParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5614459467651122237L;

	private String fieldName;
	
	private String operator;
	
	private Object value;

	public QueryParam(String fieldName, String operator, Object value) {
		super();
		this.fieldName = fieldName;
		this.operator = operator;
		this.value = value;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	
	
}
