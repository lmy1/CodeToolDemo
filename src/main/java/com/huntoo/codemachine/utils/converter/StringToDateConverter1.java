package com.huntoo.codemachine.utils.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * 将前台日期数据转换成Date类型
 * @author huntto
 *
 */
@Component
public class StringToDateConverter1 implements Converter<String, Date> {

	@Override
	public Date convert(String source) {
		try {
			// 把字符串转换为日期类型
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = simpleDateFormat.parse(source);

			return date;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 如果转换异常则返回空
		return null;
	}
}