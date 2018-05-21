package com.ahajri.heaven.calendar.queries;

import com.ahajri.heaven.calendar.constants.enums.OperatorEnum;

public class QueryParam {

	private String fieldName;
	
	private OperatorEnum operator;
	
	private Object value;

	public QueryParam(String fieldName, OperatorEnum operator, Object value) {
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

	public OperatorEnum getOperator() {
		return operator;
	}

	public void setOperator(OperatorEnum operator) {
		this.operator = operator;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	
	
}
