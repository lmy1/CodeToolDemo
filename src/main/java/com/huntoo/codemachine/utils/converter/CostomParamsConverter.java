package com.huntoo.codemachine.utils.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * 去除参数两端空格
 * @author huntto
 *
 */
@Component
public class CostomParamsConverter implements Converter<String, String>{

	@Override
	public String convert(String source) {
		if(source != null && !"".equals(source)){
			source = source.trim(); // 去空格     "       "
			if(!"".equals(source)){ // 用户可能输入一组空格
				return source;
			}
		}
		return null;
	}

}
