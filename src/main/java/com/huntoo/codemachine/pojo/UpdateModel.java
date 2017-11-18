package com.huntoo.codemachine.pojo;

/**
 * 修改单个字段的信息封装类
 * @author huntto
 *
 */
public class UpdateModel {
	//要修改字段所属的ID
	private String id;
	///要修改字段名称
	private String fieldName;
	//要修改字段的值
	private String fieldValue;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
	
	
	
	
}
