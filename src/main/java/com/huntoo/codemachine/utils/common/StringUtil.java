package com.huntoo.codemachine.utils.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.huntoo.codemachine.pojo.Response;

public class StringUtil {

	/**
	 * 字符串首字母大写
	 * 
	 * @param name
	 * @return
	 */
	public static String firstToUpperCase(String name) {
		// 普通写法
		// name = name.substring(0, 1).toUpperCase() + name.substring(1);
		// return name;
		// 大神写法 效率高
		char[] cs = name.toCharArray();
		cs[0] -= 32;
		return String.valueOf(cs);

	}
	/**
	 * 字符串按长日期格式转为日期
	 * 
	 * @param qvalue
	 * @return
	 * @throws ParseException
	 */
	public static Date StringToLongDate(String qvalue,Response response) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = dateFormat.parse(qvalue);
		} catch (ParseException e) {
			e.printStackTrace();
			if (null != response) {
				response.setStatus(1);
				response.setMsg("日期格式解析错误");
			}
		}
		return date;
	}

	/**
	 * 字符串按短日期格式转为日期
	 * 
	 * @param qvalue
	 * @return
	 * @throws ParseException
	 */
	public static Date StringToShortDate(String qvalue,Response response){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = dateFormat.parse(qvalue);
		} catch (ParseException e) {
			e.printStackTrace();
			if (null != response) {
				response.setStatus(1);
				response.setMsg("日期格式解析错误");
			}
		}
		return date;
	}
	
	/**
	 * 根据日期格式自动将字符串转为相应的Date类型
	 * @param qvalue
	 * @param response
	 * @return
	 */
	public static Date StringToDate(String qvalue,Response response){
		Date date = null;
		if (qvalue.length() <= 10) {
			date = StringUtil.StringToShortDate(qvalue, response);
		} else if (qvalue.length() > 10) {
			date = StringUtil.StringToLongDate(qvalue, response);
		}
		return date;
		
	}
}
