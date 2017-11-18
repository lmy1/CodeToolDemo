package com.huntoo.codemachine.pojo;

import java.util.List;

public class QueryVo {
	
	private String dash;	//连接符   and/or/not
	private List<QueryModel> queryModels;	//封装多个查询条件
	private Integer page = 1;	//当前页数默认值为1
	private Integer rows = 10; 	//每页展示数默认值为10
	
	public QueryVo() {
		super();
	}

	public String getDash() {
		return dash;
	}

	public void setDash(String dash) {
		this.dash = dash;
	}

	public List<QueryModel> getQueryModels() {
		return queryModels;
	}

	public void setQueryModels(List<QueryModel> queryModels) {
		this.queryModels = queryModels;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}
	
	


	
}
