package com.huntoo.codemachine.utils.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.huntoo.codemachine.utils.common.StringUtil;

/**
 * 将前台日期数据转换成Date类型
 * @author huntto
 *
 */
//@Component
//public class StringToDateConverter/* implements Converter<String, Date> */{
	
	/*@Bean
	@Override
	public Date convert(String source) {
		
		try {
			// 把字符串转换为日期类型
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
			System.err.println("111111111111111111111111");
			Date date = simpleDateFormat.parse(source);

			return date;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 如果转换异常则返回空
//		Date date = StringUtil.StringToDate(source, null);
		return null;
	}*/
//	@Bean
//    public Converter<String, Date> addNewConvert() {
//        return new Converter<String, Date>() {
//            @Override
//            public Date convert(String source) {
//            	System.err.println("1111111111111111111111111");
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                Date date = null;
//                try {
//                    date = sdf.parse((String) source);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                return date;
//            }
//        };
//    }

//}
public class StringToDateConverter implements Converter<String, Date> {
    private static final String dateFormat      = "yyyy-MM-dd HH:mm:ss";
    private static final String shortDateFormat = "yyyy-MM-dd";

    /** 
     * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
     */
    @Override
    public Date convert(String source) {
    	System.out.println("222222222222222222222222222222222222222222222222");
        if (StringUtils.isBlank(source)) {
            return null;
        }
        source = source.trim();
        try {
            if (source.contains("-")) {
                SimpleDateFormat formatter;
                if (source.contains(":")) {
                    formatter = new SimpleDateFormat(dateFormat);
                } else {
                    formatter = new SimpleDateFormat(shortDateFormat);
                }
                Date dtDate = formatter.parse(source);
                return dtDate;
            } else if (source.matches("^\\d+$")) {
                Long lDate = new Long(source);
                return new Date(lDate);
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("parser %s to Date fail", source));
        }
        throw new RuntimeException(String.format("parser %s to Date fail", source));
    }

}