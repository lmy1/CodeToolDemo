package com.huntoo.codemachine.pojo;

import java.io.Serializable;

public class QueryModel implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String qname;
	private String qvalue;
	private String sign;
	
	public QueryModel() {
		super();
	}

	public QueryModel(String qname, String qvalue, String sign) {
		super();
		this.qname = qname;
		this.qvalue = qvalue;
		this.sign = sign;
	}

	public String getQname() {
		return qname;
	}

	public void setQname(String qname) {
		this.qname = qname;
	}

	public String getQvalue() {
		return qvalue;
	}

	public void setQvalue(String qvalue) {
		this.qvalue = qvalue;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Override
	public String toString() {
		return "QueryModel [qname=" + qname + ", qvalue=" + qvalue + ", sign=" + sign + "]";
	}
	

}
