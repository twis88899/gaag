package com.twis.common.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DateTimeConverter;

/**
 * Map to javaBean
 * 
 * @author liufang
 *
 */
public class BeanUtils {

	public synchronized static void populate(Object bean,
			Map<String, ? extends Object> properties)
			throws IllegalAccessException, InvocationTargetException {
		DateTimeConverter dtConverter = new DateConverter();
		dtConverter.setPatterns(new String[] { "yyyy-MM-dd HH:mm:ss",
				"yyyy-MM-dd", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss",
				"yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd" });
		ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
		convertUtilsBean.deregister(Date.class);
		convertUtilsBean.register(dtConverter, Date.class);
		BeanUtilsBean beanUtilsBean = new BeanUtilsBean(convertUtilsBean,
				new PropertyUtilsBean());
		beanUtilsBean.populate(bean, properties);
	}
}
